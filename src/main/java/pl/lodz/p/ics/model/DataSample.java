package pl.lodz.p.ics.model;

import pl.lodz.p.ics.model.classification.IntegralImage;

import java.util.List;

/**
 * User: maciek
 * Date: 02.01.14
 * Time: 00:52
 */
public class DataSample {

    private IntegralImage integralImage;
    private double Y;

    public DataSample(IntegralImage integralImage, double Y) {
        this.integralImage = integralImage;
        this.Y = Y;
    }

    public IntegralImage getIntegralImage() {
        return integralImage;
    }

    public void setIntegralImage(IntegralImage integralImage) {
        this.integralImage = integralImage;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }
}
