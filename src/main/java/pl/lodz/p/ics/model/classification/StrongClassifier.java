package pl.lodz.p.ics.model.classification;

import org.apache.commons.collections.ListUtils;
import pl.lodz.p.ics.model.DataSample;
import pl.lodz.p.ics.model.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: maciek
 * Date: 21.12.13
 * Time: 17:48
 */
public class StrongClassifier implements Serializable {

    private transient List<WeakClassifier> candidateWeakClassifiers;
    private transient List<Feature> candidateFeatures;
    private transient Example [] examples;
//    private transient List<DataSample> dataSamples;
//    private double[] w;

    /**
     * number o examples
     */
    private int n;

    private WeakClassifier[] weakClassifiers;
    private double[] alfaFactor;

    public static final int USE_CONSTANT_THRESHOLD = 0;
    public static final int FIND_THRESHOLD = 1;
    public int thresholdOption = USE_CONSTANT_THRESHOLD;

    public StrongClassifier(List<Feature> features, List<DataSample> positiveSamples, List<DataSample> negativeSamples, int thresholdOption) {

        this.n = positiveSamples.size() + negativeSamples.size();

        List<DataSample> dataSamples = ListUtils.union(positiveSamples, negativeSamples);
        double[] w = initializeWeights(positiveSamples.size(), negativeSamples.size(), dataSamples);

        examples = new Example[n];
        for (int i = 0; i < n; i++) {
            examples[i] = new Example(dataSamples.get(i), w[i]);
        }

        this.candidateFeatures = features;
        this.thresholdOption = thresholdOption;
    }

    public void learn(int numberOfIteration) {

        weakClassifiers = new WeakClassifier[numberOfIteration];
        alfaFactor = new double[numberOfIteration];

        for (int t = 1; t <= numberOfIteration; t++) {
            System.out.println("\nWybór klasyfikatora słabego " + t);

            candidateWeakClassifiers = new ArrayList<WeakClassifier>();

            normalizeWeights();

            // 2. SELECT WEAK CLASSIFIER a) find optimal threshold
            if (thresholdOption == FIND_THRESHOLD) {

                double totalPositive = sumWeightBelow(n, 1);
                double totalNegative = sumWeightBelow(n, 0);

                for (int i = 0; i < candidateFeatures.size(); i++) {

                    for (int j = 0; j < n; j++) {
                        examples[j].setActualResponse(candidateFeatures.get(i).value(examples[j].getDs().getIntegralImage(), new Point(0, 0)));
                    }

                    Arrays.sort(examples);

                    double[] belowPositive = new double[n];
                    double[] belowNegative = new double[n];
                    for (int j = 0; j < n; j++) {
                        belowPositive[j] = sumWeightBelow(j, 1);
                        belowNegative[j] = sumWeightBelow(j, 0);
                    }

                    double[] e = new double[n];
                    for (int j = 0; j < n; j++) {
                        double left = belowPositive[j] + totalNegative - belowNegative[j];
                        double right = belowNegative[j] + totalPositive - belowPositive[j];
                        e[j] = Math.min(left, right);
                    }

                    double[] thresh =/* new double[]{sign(e[index]), e[index]}; */findThreshold(e);
                    double polarity = thresh[0];
                    double threshold = thresh[1];

                    WeakClassifier newCandidate = new WeakClassifier(candidateFeatures.get(i), threshold);
                    newCandidate.setPolarity(polarity);

                    if (threshold != 0) {
                        candidateWeakClassifiers.add(newCandidate);
                    }
                }
            }

            // 2. SELECT WEAK CLASSIFIER b) select weak classifier

            double[] errors = new double[candidateWeakClassifiers.size()];
            double[] eta = new double[candidateWeakClassifiers.size()];

            for (int j = 0; j < candidateWeakClassifiers.size(); j++) {
                WeakClassifier weakClassifier = candidateWeakClassifiers.get(j);
                for (int i = 0; i < n; i++) {
                    DataSample ds = examples[i].getDs();
                    errors[j] += examples[i].getW() * indicatorFunction(weakClassifier.value(ds.getIntegralImage()), ds.getY());
                }
                eta[j] = Math.abs(0.5 - errors[j]);
            }

            // 3. DEFINE h_t(x)

            int classifierNumber = findMaxIndex(eta);
            WeakClassifier weakClassifier = candidateWeakClassifiers.get(classifierNumber);
            System.out.println("\tthreshold " + weakClassifier.getThreshold() + " polarity " + weakClassifier.getPolarity());

            double error = errors[classifierNumber];
            alfaFactor[t - 1] = Math.log((1.0 - error) / error);
            weakClassifiers[t - 1] = weakClassifier;

            updateWeights(error, weakClassifier);

        }
    }

    public double[] findThreshold(double[] e) {

        int index = 0;
        double eMin = e[index];
        for (int i = 1; i < e.length; i++) {
            if (eMin > e[i]) {
                eMin = e[i];
                index = i;
            }
        }

        double threshold, polarity;
        Example a, b;

        if (index - 1 >= 0) {
            if (examples[index - 1].getDs().getY() != examples[index].getDs().getY()) {
                a = examples[index - 1];
                b = examples[index];
            } else if (examples[index].getDs().getY() != examples[index + 1].getDs().getY()) {
                a = examples[index];
                b = examples[index + 1];
            } else {
                return new double[]{0.0, 1.0};
            }
        } else {
            if (examples[index].getDs().getY() != examples[index + 1].getDs().getY()) {
                a = examples[index];
                b = examples[index + 1];
            } else {
                return new double[]{0.0, 1.0};
            }
        }

        polarity = (a.getDs().getY() == 0 && b.getDs().getY() == 1 ? -1.0 : 1.0);
        threshold = a.getActualResponse() - b.getActualResponse();
        threshold = Math.abs(threshold) / 2.0;
        threshold += a.getActualResponse();

        return new double[]{polarity, threshold};

    }

    public static double sign(final double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }
        return (x == 0.0) ? 0.0 : (x > 0.0) ? 1.0 : -1.0;
    }

    /*public void sortExamplesByFeatureValue() {

        int length = examples.length;
        Example tmpExample;

        for (int i = 0; i < length; i++) {
            for (int j = (length - 1); j >= (i + 1); j--) {
                if (examples[j].getActualResponse() < examples[j - 1].getActualResponse()) {
                    tmpExample = examples[j];
                    examples[j] = examples[j - 1];
                    examples[j - 1] = tmpExample;
                }
            }
        }
    }*/

    public double sumWeightBelow(int belowIndex, int response) {
        double sum = 0;
        for (int i = 0; i < belowIndex; i++) {
            Example ex = examples[i];
            if (ex.getDs().getY() == response){
                sum += ex.getW();
            }
        }
        return sum;
    }

    public double[] initializeWeights(int l, int m, List<DataSample> ds) {

        double[] w = new double[l + m];

        double positiveWeight = 1.0 / (2.0 * l);
        double negativeWeight = 1.0 / (2.0 * m);

        for (int i = 0; i < l + m; i++) {
            w[i] = (ds.get(i).getY() == 1 ? positiveWeight : negativeWeight);
        }

        return w;
    }

    public void normalizeWeights() {
        double sum = 0;
        for (Example example : examples) {
            sum += example.w;
        }
        for (int i = 0; i < examples.length; i++) {
            examples[i].setW(examples[i].getW() / sum);
        }
    }

    public void updateWeights(double error, WeakClassifier weakClassifier) {
        for (int i = 0; i < n; i++) {
            Example ex = examples[i];
            DataSample ds = ex.getDs();

            double value = weakClassifier.value(ds.getIntegralImage());

            double base = error / (1.0 - error);
            double exponent = 1.0 - (value == ds.getY() ? 0.0 : 1.0);

            ex.setW(ex.getW() * Math.pow(base, exponent));
        }
    }

    public int detect(IntegralImage integralImage, Point relativePoint) {
        double value = 0;
        double alfaSum = 0;

        for (int i = 0; i < weakClassifiers.length; i++) {
            value += alfaFactor[i] * weakClassifiers[i].value(integralImage, relativePoint);
            alfaSum += alfaFactor[i];
        }

        return (value >= (0.5 * alfaSum) ? 1 : 0);
    }

    public int findMaxIndex(double[] array) {
        double maxValue = array[0];
        int maxIndex = 0;

        for (int i = 0; i < array.length; i++) {
            double value = array[i];
            if (value > maxValue) {
                maxValue = value;
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    public double indicatorFunction(double a, double b) {
        if (a != b) {
            return 1d;
        }
        return 0d;
    }

    public WeakClassifier[] getWeakClassifiers() {
        return weakClassifiers;
    }

    public void setWeakClassifiers(WeakClassifier[] weakClassifiers) {
        this.weakClassifiers = weakClassifiers;
    }

    public double[] getAlfaFactor() {
        return alfaFactor;
    }

    public void setAlfaFactor(double[] alfaFactor) {
        this.alfaFactor = alfaFactor;
    }

    class Example implements Comparable<Example>{

        private DataSample ds;
        private double w;
        private double actualResponse;

        public Example(DataSample ds, double w) {
            this.ds = ds;
            this.w = w;
        }

        DataSample getDs() {
            return ds;
        }

        void setDs(DataSample ds) {
            this.ds = ds;
        }

        double getW() {
            return w;
        }

        void setW(double w) {
            this.w = w;
        }

        double getActualResponse() {
            return actualResponse;
        }

        void setActualResponse(double actualResponse) {
            this.actualResponse = actualResponse;
        }

        @Override
        public int compareTo(Example o) {

            if (this.actualResponse < o.getActualResponse())
                return -1;
            else if (this.actualResponse > o.getActualResponse()) {
                return 1;
            } else {
                return 0;
            }

        }

    }

}
