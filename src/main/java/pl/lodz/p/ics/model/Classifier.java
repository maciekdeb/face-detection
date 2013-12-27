package pl.lodz.p.ics.model;

import com.google.common.primitives.Doubles;

import java.util.Arrays;
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

    public Classifier(List<Feature> features, double[] x, double[] y) {

        if (x.length != y.length) {
            throw new IllegalArgumentException();
        }
        this.m = x.length;
        this.D = new double[m];

        for (int i = 0; i < features.size(); i++) {
            WeakClassifier weakClassifier = new WeakClassifier(0.5);
            weakClassifier.setFeature(features.get(i));
            weakClassifiers.add(weakClassifier);
            D[i] = 1d / m;
        }
    }

    public void learn(int numberOfIteration) {

        for (int t = 0; t < numberOfIteration; t++) {

            double[] errors = new double[weakClassifiers.size()];

            for (int j = 0; j < weakClassifiers.size(); j++) {
                WeakClassifier weakClassifier = weakClassifiers.get(j);
                for (int i = 0; i < m; i++) {
                    if (weakClassifier.value(x[i]) != y[i]) {
                        errors[j] += D[i];
                    }
                }
                errors[j] = Math.abs(0.5 - errors[j]);
            }

            Doubles.max(errors);

        }
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

    public WeakClassifier findClassifier() {
        return null;
    }

}
