package pl.lodz.p.ics;

import pl.lodz.p.ics.model.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maciek
 * Date: 08.10.13
 * Time: 18:13
 */
public class ApproximateLocation {

    public static int ALFA = 10;

    public static void main(String[] args) {
        try {

            URL url = ApproximateLocation.class.getResource("/face.jpg");
            BufferedImage image = ImageIO.read(url);

            int width = image.getWidth();
            int height = image.getHeight();

            BufferedImage directionalImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            int[][] rgb = new int[height][width];
            Utils.fillArray(rgb, 0xFFFFFFFF);
            Utils.fillImage(directionalImage, rgb);

            List<List<pl.lodz.p.ics.model.Vector>> vectors = new ArrayList<List<pl.lodz.p.ics.model.Vector>>();
            for (int j = 1; j < height - ALFA; j += ALFA) {
                vectors.add(new ArrayList<pl.lodz.p.ics.model.Vector>());
            }

            for (int i = 1; i < width - ALFA; i += ALFA) {
                int index = 0;
                for (int j = 1; j < height - ALFA; j += ALFA) {
                    Double[] abc = DirectionalMap.computeABC(image, i, j, ALFA);
                    Double[] tangent = DirectionalMap.getTangent(abc);
                    double direction = DirectionalMap.getDirection(tangent);

                    vectors.get(index).add(new pl.lodz.p.ics.model.Vector(new pl.lodz.p.ics.model.Point(i, j), tangent, direction));
                    index++;

                    Utils.drawAngles(directionalImage, direction, i, j, ALFA);
                }
            }

            ImageIO.write(directionalImage, "JPG", new File("directional-image.jpg"));

            Elipse referenceElipse = new Elipse(70, 80, 1.25, 0.75);
            double[][] candidatesForElipse = HoughTransform.prepareCandidatesForElipseCenter(image, referenceElipse, vectors);
            int[][] centers = Utils.convertRGB(candidatesForElipse);
            BufferedImage centerImage = new BufferedImage(centers[0].length, centers.length, BufferedImage.TYPE_3BYTE_BGR);
            Utils.fillImage(centerImage, centers);

            List<pl.lodz.p.ics.model.Point> maxCenters = Utils.findMaxValues(centers, 1);

            for (pl.lodz.p.ics.model.Point point : maxCenters) {
                Graphics2D graphics2D = (Graphics2D) centerImage.getGraphics();
                graphics2D.setColor(Color.RED);
                graphics2D.drawOval(point.getX(), point.getY(), (int) referenceElipse.getSemiAxeWidth() * 2, (int) referenceElipse.getSemiAxeHeight() * 2);
            }

            ImageIO.write(centerImage, "JPG", new File("centers-image.jpg"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
