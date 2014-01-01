import org.junit.Test;
import pl.lodz.p.ics.FeatureGenerator;
import pl.lodz.p.ics.Utils;
import pl.lodz.p.ics.model.Point;
import pl.lodz.p.ics.model.classification.Feature;
import pl.lodz.p.ics.model.classification.Field;
import pl.lodz.p.ics.model.classification.IntegralImage;
import pl.lodz.p.ics.model.classification.WeakClassifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: maciek
 * Date: 15.12.13
 * Time: 22:14
 */
public class WeakClassifierTest {

    @Test
    public void testPasses() throws IOException {

        BufferedImage bufferedImage = ImageIO.read(new File("syntetic_test.jpg"));

        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                System.out.print((bufferedImage.getRGB(j, i) >> 8 & 0xFF) + "\t");
            }
            System.out.println(" ");
        }

        IntegralImage integralImage = new IntegralImage(bufferedImage, 255d);
        double[][] integral = integralImage.getIntegralImage();

        System.out.println("\n\n\n");

        for (int i = 0; i < integral.length; i++) {
            for (int j = 0; j < integral[i].length; j++) {
                System.out.print(integral[i][j] + "\t");
            }
            System.out.println(" ");
        }
        System.out.println("\n\n\n");

        WeakClassifier weakClassifier = new WeakClassifier();
        List<Field> fields = new ArrayList<>();
        fields.add(new Field(new Point(1, 1), new Point(3, 3), -1));
        fields.add(new Field(new Point(4, 1), new Point(6, 3), 1));
        fields.add(new Field(new Point(1, 4), new Point(3, 6), 1));
        fields.add(new Field(new Point(4, 4), new Point(6, 6), -1));

        Feature feature = new Feature(fields);
        System.out.println(feature.value(integralImage, new Point(0, 0)));

        weakClassifier.setFeature(feature);
        System.out.println(weakClassifier.value(integralImage));

    }

}
