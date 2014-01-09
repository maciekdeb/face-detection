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

    public IntegralImage(BufferedImage bufferedImage, double denominator) {

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        this.integralImage = new double[height][width];

        //  skopiuj wartosci pikseli do obrazu scalkowanego
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int color = bufferedImage.getRGB(x, y);
                integralImage[y][x] = ((color >> 8) & 0xFF) + ((color >> 16) & 0xFF) + ((color >> 24) & 0xFF);
                integralImage[y][x] /= denominator;
            }
        }

        //  oblicz krawedz gorna
        for (int x = 1; x < width; x++) {
            integralImage[0][x] += integralImage[0][x - 1];
        }
        //  oblicz krawedz lewa
        for (int y = 1; y < height; y++) {
            integralImage[y][0] += integralImage[y - 1][0];
        }

        //  oblicz srodek
        for (int x = 1; x < width; x++) {
            for (int y = 1; y < height; y++) {
                integralImage[y][x] += (integralImage[y][x - 1] + integralImage[y - 1][x]) - integralImage[y - 1][x - 1];
            }
        }

    }

    public double getRectangleValue(Point topLeft, Point bottomRight) {

        int bX = bottomRight.getX();
        int bY = bottomRight.getY();

        int tX = topLeft.getX();
        int tY = topLeft.getY();

        double result = integralImage[bY][bX];

        boolean isTopSpace = topLeft.getY() > 0;
        boolean isLeftSpace = topLeft.getX() > 0;

        tX--;
        tY--;

        if (isLeftSpace) {
            result -= integralImage[bY][tX];
        }

        if (isTopSpace) {
            result -= integralImage[tY][bX];
        }

        if (isLeftSpace && isTopSpace) {
            result += integralImage[tY][tX];
        }

        return result;
    }

    public double[][] getIntegralImage() {
        return integralImage;
    }
}
