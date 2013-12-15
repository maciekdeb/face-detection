package pl.lodz.p.ics.model;

import java.awt.image.BufferedImage;

/**
 * User: maciek
 * Date: 15.12.13
 * Time: 17:20
 */
public class IntegralImage {

    private BufferedImage bufferedImage;
    private double[][] integralImage;

    public IntegralImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;

        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {

                //TODO

            }
        }
    }


}
