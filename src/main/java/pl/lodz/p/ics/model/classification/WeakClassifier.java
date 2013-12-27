package pl.lodz.p.ics.model.classification;

import pl.lodz.p.ics.model.Point;

/**
 * User: maciek
 * Date: 21.12.13
 * Time: 17:49
 */
public class WeakClassifier {

    private Feature feature;

    public double value(IntegralImage integralImage, Point topLeftPoint, Point bottomRightPoint) {
        return activateFunction(feature.value(integralImage, topLeftPoint, bottomRightPoint));
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
