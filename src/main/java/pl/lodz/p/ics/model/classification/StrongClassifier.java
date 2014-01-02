package pl.lodz.p.ics.model.classification;

import pl.lodz.p.ics.model.DataSample;
import pl.lodz.p.ics.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * User: maciek
 * Date: 21.12.13
 * Time: 17:48
 */
public class StrongClassifier {

    private List<WeakClassifier> candidateWeakClassifiers;

    private List<DataSample> dataSamples;

    /**
     * weights
     */
    private double[] w;

    /**
     * number o examples (negative (m) + positive (l))
     */
    private int n;
    private int m;
    private int l;

    private WeakClassifier[] weakClassifiers;
    private double[] alfaFactor;

    public StrongClassifier(List<Feature> features, List<DataSample> positiveSamples, List<DataSample> negativeSamples) {

        this.l = positiveSamples.size();
        this.m = negativeSamples.size();
        this.n = l + m;

        this.w = new double[n];

        this.dataSamples = new ArrayList<>();
        this.dataSamples.addAll(positiveSamples);
        this.dataSamples.addAll(negativeSamples);

        this.candidateWeakClassifiers = new ArrayList<>();
        for (int i = 0; i < features.size(); i++) {
            candidateWeakClassifiers.add(new WeakClassifier(features.get(i), 0));
        }

    }

    public void learn(int numberOfIteration) {

        weakClassifiers = new WeakClassifier[numberOfIteration];
        alfaFactor = new double[numberOfIteration];

        initializeWeights(w);

        for (int t = 1; t <= numberOfIteration; t++) {
            System.out.println("\nEpoka " + t);

            normalizeWeights(w);

            // 2. SELECT WEAK CLASSIFIER

            // 3. DEFINE h_t(x)

            double[] errors = new double[candidateWeakClassifiers.size()];
            double[] eta = new double[candidateWeakClassifiers.size()];

            for (int j = 0; j < candidateWeakClassifiers.size(); j++) {
                WeakClassifier weakClassifier = candidateWeakClassifiers.get(j);
                for (int i = 0; i < n; i++) {
                    DataSample ds = dataSamples.get(i);
                    errors[j] += w[i] * indicatorFunction(weakClassifier.value(ds.getIntegralImage()), ds.getY());
                }
                eta[j] = Math.abs(0.5 - errors[j]);
            }

            int classifierNumber = findMaxIndex(eta);
            WeakClassifier weakClassifier = candidateWeakClassifiers.get(classifierNumber);

            alfaFactor[t - 1] = Math.log((1 - errors[classifierNumber]) / errors[classifierNumber]);
            weakClassifiers[t - 1] = weakClassifier;

            // 4. UPDATE THE WEIGHTS
            updateWeights(w, errors[classifierNumber], weakClassifier);

        }
    }

    public void updateWeights(double[] w, double error, WeakClassifier weakClassifier) {
        for (int i = 0; i < n; i++) {

            DataSample ds = dataSamples.get(i);
            double value = weakClassifier.value(ds.getIntegralImage());

            double base = error / (1.0 - error);
            double exponent = 1.0 - (value == ds.getY() ? 0.0 : 1.0);

            w[i] *= Math.pow(base, exponent);

        }
    }

    public void initializeWeights(double[] w) {
        for (int i = 0; i < l; i++) {
            w[i] = 1.0 / (2.0 * l);
        }
        for (int i = l; i < m + l; i++) {
            w[i] = 1.0 / (2.0 * m);
        }
    }

    public void normalizeWeights(double[] t) {
        double sum = 0;
        for (double d : t) {
            sum += d;
        }
        for (int i = 0; i < t.length; i++) {
            t[i] /= sum;
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
}
