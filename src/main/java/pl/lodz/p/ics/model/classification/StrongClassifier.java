package pl.lodz.p.ics.model.classification;

import pl.lodz.p.ics.model.DataSample;
import pl.lodz.p.ics.model.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: maciek
 * Date: 21.12.13
 * Time: 17:48
 */
public class StrongClassifier {

    private List<WeakClassifier> weakClassifiers;

    private List<DataSample> dataSamples;

    private double[] D;
    private int m;

    private WeakClassifier[] outputClassifiers;
    private double[] outputAlfa;

    public StrongClassifier(List<Feature> features, List<DataSample> dataSamples) {
        this.m = dataSamples.size();
        this.D = new double[m];
        this.dataSamples = dataSamples;
        this.weakClassifiers = new ArrayList<>();

        for (int i = 0; i < features.size(); i++) {
            WeakClassifier weakClassifier = new WeakClassifier();
            weakClassifier.setFeature(features.get(i));
            weakClassifiers.add(weakClassifier);
        }

        for (int i = 0; i < m; i++) {
            D[i] = 1d / m;
        }
    }

    public void learn(int numberOfIteration) {

        outputClassifiers = new WeakClassifier[numberOfIteration];
        outputAlfa = new double[numberOfIteration];

        for (int t = 0; t < numberOfIteration; t++) {

            System.out.println("\nEpoka " + t);

            double[] errors = new double[weakClassifiers.size()];
            double[] eta = new double[weakClassifiers.size()];

            for (int j = 0; j < weakClassifiers.size(); j++) {
                WeakClassifier weakClassifier = weakClassifiers.get(j);
                for (int i = 0; i < m; i++) {
                    errors[j] += D[i] * indicatorFunction(weakClassifier.value(dataSamples.get(i).getIntegralImage()), dataSamples.get(i).getY());
                }
                eta[j] = Math.abs(0.5 - errors[j]);
            }

            int classifierNumber = findMaxIndex(eta);
            WeakClassifier weakClassifier = weakClassifiers.get(classifierNumber);

            outputAlfa[t] = 1 / 2 * Math.log((1 - errors[classifierNumber]) / errors[classifierNumber]);
            outputClassifiers[t] = weakClassifier;

            for (int i = 0; i < m; i++) {
                D[i] = D[i] * Math.exp(outputAlfa[t] * (2 * indicatorFunction(dataSamples.get(i).getY(), weakClassifier.value(dataSamples.get(i).getIntegralImage())) - 1));
            }
            double den = 0;
            for (double d : D) {
                den += d;
            }
            for (int i = 0; i < m; i++) {
                D[i] = D[i] / den;
            }
        }
    }

    //TODO skalowalne feature
    public int detect(IntegralImage integralImage, Point relativePoint) {
        double value = 0;

        for (int i = 0; i < outputClassifiers.length; i++) {
            value += outputAlfa[i] * outputClassifiers[i].value(integralImage, relativePoint);
        }

        return (int) Math.copySign(1.0, value);
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

    public List<WeakClassifier> getWeakClassifiers() {
        return weakClassifiers;
    }

    public void setWeakClassifiers(List<WeakClassifier> weakClassifiers) {
        this.weakClassifiers = weakClassifiers;
    }

    public double[] getD() {
        return D;
    }

    public void setD(double[] d) {
        D = d;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public WeakClassifier[] getOutputClassifiers() {
        return outputClassifiers;
    }

    public void setOutputClassifiers(WeakClassifier[] outputClassifiers) {
        this.outputClassifiers = outputClassifiers;
    }

    public double[] getOutputAlfa() {
        return outputAlfa;
    }

    public void setOutputAlfa(double[] outputAlfa) {
        this.outputAlfa = outputAlfa;
    }
}
