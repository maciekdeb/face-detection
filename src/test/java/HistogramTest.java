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

        BufferedImage image = ImageIO.read(new File("/home/maciek/FACE/histogram/audrey2.jpg"));

        image = HistogramUtils.stretchHistogram(image);

        ImageIO.write(image, "JPG", new File("/home/maciek/FACE/histogram/audrey2_stretched.jpg"));

    }

}
