import junit.framework.Assert;
import org.junit.Test;
import pl.lodz.p.ics.HistogramUtils;
import pl.lodz.p.ics.model.Point;
import pl.lodz.p.ics.model.classification.IntegralImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * User: maciek
 * Date: 15.12.13
 * Time: 22:14
 */
public class HistogramTest {

    @Test
    public void testPasses() throws IOException {

        BufferedImage image = ImageIO.read(new File("/home/maciek/face00016.png"));

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                System.out.print("\t" + (image.getRGB(i, j) >> 8 & 0xFF));
            }
            System.out.println("");
        }

        System.out.println("\n\n");

        image = HistogramUtils.equalizeHistogram(image);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                System.out.print("\t" + (image.getRGB(i, j) >> 8 & 0xFF));
            }
            System.out.println("");
        }

        ImageIO.write(image, "PNG", new File("/home/maciek/face00016new.png"));

    }

}
