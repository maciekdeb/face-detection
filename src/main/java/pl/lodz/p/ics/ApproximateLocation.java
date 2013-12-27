package pl.lodz.p.ics;

import pl.lodz.p.ics.model.Point;
import pl.lodz.p.ics.model.transform.DirectionalMap;
import pl.lodz.p.ics.model.transform.Ellipse;
import pl.lodz.p.ics.model.transform.EllipsesPositionsMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static pl.lodz.p.ics.model.ConfigurationValues.*;

/**
 * User: maciek
 * Date: 08.10.13
 * Time: 18:13
 */
public class ApproximateLocation {

    public static void main(String[] args) {
        try {
            BufferedImage image = ImageIO.read(getURLS().get(0));

            DirectionalMap directionalMap = new DirectionalMap(image, ALFA).build();
            ImageIO.write(directionalMap.getDirectionalImage(), "JPG", new File(OUTPUT_DIRECTIONAL_IMAGE));

            Ellipse referenceEllipse = getReferenceElipse();
            EllipsesPositionsMap approximateElipsesPositions = new EllipsesPositionsMap(image, directionalMap, referenceEllipse);
            double[][] votesMap = approximateElipsesPositions.getVotesMap();

            drawVotes(votesMap);

            List<Point> elipsesCenters = approximateElipsesPositions.findCenters(CENTERS_NUMBER);
            drawElipsesCenters(image, referenceEllipse, elipsesCenters);
//            drawElipsesCenters(directionalMap.getDirectionalImage(), referenceEllipse, elipsesCenters);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void drawVotes(double[][] votes) throws IOException {
        int[][] votesRGB = Utils.convertRGB(votes);
        BufferedImage centerImage = new BufferedImage(votesRGB[0].length, votesRGB.length, BufferedImage.TYPE_3BYTE_BGR);
        Utils.fillImage(centerImage, votesRGB);
        ImageIO.write(centerImage, "JPG", new File(OUTPUT_VOTES_IMAGE));
    }

    public static void drawElipsesCenters(BufferedImage image, Ellipse ellipse, List<Point> elipsesCenters) throws IOException {

        for (Point point : elipsesCenters) {

            Graphics2D graphics2D = (Graphics2D) image.getGraphics();
            graphics2D.setColor(Color.RED);
            graphics2D.drawOval(point.getX(), point.getY(), (int) ellipse.getSemiAxeWidth() * 2, (int) ellipse.getSemiAxeHeight() * 2);

        }

        ImageIO.write(image, "JPG", new File(OUTPUT_ELIPSES_IMAGE));
    }

}
