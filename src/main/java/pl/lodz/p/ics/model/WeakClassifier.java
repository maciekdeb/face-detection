package pl.lodz.p.ics.model;

/**
 * User: maciek
 * Date: 21.12.13
 * Time: 17:49
 */
public class WeakClassifier {

    private double internalWeight;
    private Feature feature;

    public WeakClassifier(double internalWeight) {
        this.internalWeight = internalWeight;
    }

    public double value(double x) {
        return activateFunction(internalWeight * x);
    }

    public double activateFunction(double x) {
        if (x > 0) {
            return 1;
        }
        return 0;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public double getInternalWeight() {
        return internalWeight;
    }

    public void setInternalWeight(double internalWeight) {
        this.internalWeight = internalWeight;
    }
}
