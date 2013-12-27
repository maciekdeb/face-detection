package pl.lodz.p.ics.model.classification;

import pl.lodz.p.ics.model.Point;

import java.awt.image.BufferedImage;

/**
 * User: maciek
 * Date: 15.12.13
 * Time: 17:20
 */
public class IntegralImage {

    private double[][] integralImage;

    public IntegralImage(BufferedImage bufferedImage) {

        this.integralImage = new double[bufferedImage.getHeight()][bufferedImage.getWidth()];

        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                for (int i = 0; i <= x; i++) {
                    for (int j = 0; j <= y; j++) {
                        //TODO
                        integralImage[y][x] += bufferedImage.getRGB(i, j);
                    }
                }
            }
        }
    }

    public double getFieldValue(Field field) {
        return getRectangleValue(field.getTopLeftPoint(), field.getBottomRightPoint());
    }

    public double getRectangleValue(Point topLeft, Point bottomRight) {

        if (topLeft.getX() > bottomRight.getX() || topLeft.getY() > bottomRight.getY()) {
            throw new IllegalArgumentException("Rectangle not valid");
        }

        double result = integralImage[bottomRight.getY()][bottomRight.getX()];

        if (topLeft.getX() > 0) {
            result -= integralImage[bottomRight.getY()][topLeft.getX() - 1];
        }

        if (topLeft.getY() > 0) {
            result -= integralImage[topLeft.getY() - 1][bottomRight.getX()];
        }

        if (topLeft.getX() > 0 && topLeft.getY() > 0) {
            result += integralImage[topLeft.getY() - 1][topLeft.getX() - 1];
        }

        return result;
    }

    public double[][] getIntegralImage() {
        return integralImage;
    }
}
