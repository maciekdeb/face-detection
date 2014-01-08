package pl.lodz.p.ics.model.classification;

import pl.lodz.p.ics.model.Point;

import java.io.Serializable;

/**
 * User: maciek
 * Date: 21.12.13
 * Time: 17:49
 */
public class WeakClassifier implements Serializable{

    private Feature feature;
    private double threshold;
    private double polarity;

    public WeakClassifier() {}

    public WeakClassifier(Feature feature, double threshold) {
        this.feature = feature;
        this.threshold = threshold;
    }

    public double value(IntegralImage integralImage) {
        return activateFunction(feature.value(integralImage, new Point(0, 0)));
    }

    public double value(IntegralImage integralImage, Point relativePoint) {
        return activateFunction(feature.value(integralImage, relativePoint));
    }

    public double activateFunction(double x) {
        return (polarity * x < polarity * threshold ? 1 : 0);
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double getPolarity() {
        return polarity;
    }

    public void setPolarity(double polarity) {
        this.polarity = polarity;
    }
}
