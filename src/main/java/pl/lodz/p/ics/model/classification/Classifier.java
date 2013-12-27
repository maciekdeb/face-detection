package pl.lodz.p.ics.model.classification;

import pl.lodz.p.ics.model.Point;

import java.util.List;

/**
 * User: maciek
 * Date: 21.12.13
 * Time: 17:48
 */
public class Classifier {

    private List<WeakClassifier> weakClassifiers;
    private double[] x;
    private double[] y;
    private double[] D;
    private int m;

    private WeakClassifier[] outputClassifiers;
    private double[] outputAlfa;

    public Classifier(List<Feature> features, double[] x, double[] y) {

        if (x.length != y.length) {
            throw new IllegalArgumentException();
        }
        this.m = x.length;
        this.D = new double[m];

        for (int i = 0; i < features.size(); i++) {
            WeakClassifier weakClassifier = new WeakClassifier();
            weakClassifier.setFeature(features.get(i));
            weakClassifiers.add(weakClassifier);
            D[i] = 1d / m;
        }
    }

    public void learn(int numberOfIteration) {

        for (int t = 0; t < numberOfIteration; t++) {

            double[] errors = new double[weakClassifiers.size()];
            double[] eta = new double[weakClassifiers.size()];
            outputClassifiers = new WeakClassifier[numberOfIteration];
            outputAlfa = new double[numberOfIteration];

            for (int j = 0; j < weakClassifiers.size(); j++) {
                WeakClassifier weakClassifier = weakClassifiers.get(j);
                for (int i = 0; i < m; i++) {
                    errors[j] += D[i] * indicatorFunction(weakClassifier.value(x[i]), y[i]);
                }
                eta[j] = Math.abs(0.5 - errors[j]);
            }

            int classifierNumber = findMaxIndex(eta);
            WeakClassifier weakClassifier = weakClassifiers.get(classifierNumber);

            outputAlfa[t] = 1 / 2 * Math.log((1 - errors[classifierNumber]) / errors[classifierNumber]);
            outputClassifiers[t] = weakClassifier;

            for (int i = 0; i < m; i++) {

                //TODO dodac dzielnik?
                D[i] = D[i] * Math.exp(outputAlfa[t] * (2 * indicatorFunction(y[i], weakClassifier.value(x[i])) - 1));

            }
        }
    }

    public int value(IntegralImage integralImage, Point topLeftPoint, Point bottomRightPoint) {
        double value = 0;

        for (int i = 0; i < outputClassifiers.length; i++) {
            value += outputAlfa[i] * outputClassifiers[i].value(x);
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

}
