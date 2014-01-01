import org.junit.Test;
import pl.lodz.p.ics.FeatureGenerator;
import pl.lodz.p.ics.Utils;
import pl.lodz.p.ics.model.classification.Feature;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * User: maciek
 * Date: 15.12.13
 * Time: 22:14
 */
public class ClassificationTest {

    @Test
    public void testPasses() throws IOException {

        BufferedImage bufferedImage = ImageIO.read(new File("syntetic_test.jpg"));

        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {

                System.out.print((bufferedImage.getRGB(j, i) >> 8 & 0xFF) + " " + (bufferedImage.getRGB(j, i) >> 8 & 0xFF) + " " + ((bufferedImage.getRGB(j, i) >> 16) & 0xFF) + "\n");

            }
            System.out.println(" ");
        }

    }

}
