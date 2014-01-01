import junit.framework.Assert;
import org.junit.Test;
import pl.lodz.p.ics.model.classification.IntegralImage;
import pl.lodz.p.ics.model.Point;

import java.awt.image.BufferedImage;

/**
 * User: maciek
 * Date: 15.12.13
 * Time: 22:14
 */
public class IntegralImageTest {

    int a[][] = new int[][]{
            new int[]{1, 1, 1, 1, 1, 1, 1},
            new int[]{2, 2, 2, 2, 2, 2, 2},
            new int[]{3, 3, 3, 3, 3, 3, 3},
            new int[]{4, 4, 4, 4, 4, 4, 4}
    };

    @Test
    public void testPasses() {
        BufferedImage image = new BufferedImage(7, 4, BufferedImage.TYPE_4BYTE_ABGR);

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                image.setRGB(j, i, a[i][j]);
            }
        }

        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                System.out.print(image.getRGB(i, j) + " ");
            }
            System.out.print("\n");
        }

        IntegralImage integralImage = new IntegralImage(image, 1d);

        for (int i = 0; i < integralImage.getIntegralImage().length; i++) {
            for (int j = 0; j < integralImage.getIntegralImage()[i].length; j++) {
                System.out.print(integralImage.getIntegralImage()[i][j] + " ");
            }
            System.out.println("\n");
        }

        Assert.assertEquals(1.0, integralImage.getRectangleValue(new Point(0, 0), new Point(0, 0)));
        Assert.assertEquals(6.0, integralImage.getRectangleValue(new Point(0, 0), new Point(1, 1)));
        Assert.assertEquals(9.0, integralImage.getRectangleValue(new Point(1, 2), new Point(3, 2)));
        Assert.assertEquals(10.0, integralImage.getRectangleValue(new Point(1, 1), new Point(2, 2)));

    }

}
