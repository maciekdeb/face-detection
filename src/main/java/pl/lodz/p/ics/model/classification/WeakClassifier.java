package pl.lodz.p.ics.model.classification;

import pl.lodz.p.ics.model.Point;

/**
 * User: maciek
 * Date: 21.12.13
 * Time: 17:49
 */
public class WeakClassifier {

    private Feature feature;

    public WeakClassifier() {}

    public WeakClassifier(Feature feature) {
        this.feature = feature;
    }

    public double value(IntegralImage integralImage) {
        return activateFunction(feature.value(integralImage, new Point(0, 0)));
    }

    public double value(IntegralImage integralImage, Point relativePoint) {
        return activateFunction(feature.value(integralImage, relativePoint));
    }

    public double activateFunction(double x) {
        if (x > 0) {
            return 1;
        }
        return -1;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

}
