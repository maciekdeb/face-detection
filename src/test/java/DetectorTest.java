import org.junit.Test;
import pl.lodz.p.ics.Utils;
import pl.lodz.p.ics.model.Point;
import pl.lodz.p.ics.model.classification.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: maciek
 * Date: 15.12.13
 * Time: 22:14
 */
public class DetectorTest {

    @Test
    public void testPasses() throws IOException {

        BufferedImage bufferedImage = new BufferedImage(640, 480, BufferedImage.TYPE_INT_BGR);

        StrongClassifier strongClassifier = Utils.loadClassifier("strongClassifier-100-1389636715456");

        Detector detector = new Detector(strongClassifier);

//        List<IntegralImage> integralImages = detector.buildIntegralPyramid(bufferedImage, 0.75, 5);
//        for (int i = 0; i < 5; i++) {
//            System.out.println(integralImages.get(i).getIntegralImage()[0].length + " " + integralImages.get(i).getIntegralImage().length);
//        }

        Map<Integer, Point> positions = detector.scan(bufferedImage, new SlidingWindow(1, 1, 10, 0.75));

        for (int i : positions.keySet()) {
            Point b = positions.get(i).add(i, i);
            Utils.drawRectangle(positions.get(i), b, bufferedImage);
        }

        File outputfile = new File("asd.jpg");
        ImageIO.write(bufferedImage, "jpg", outputfile);
    }

}
