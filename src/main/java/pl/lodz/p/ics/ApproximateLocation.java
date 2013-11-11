package pl.lodz.p.ics;

import pl.lodz.p.ics.model.*;
import pl.lodz.p.ics.model.Point;
import pl.lodz.p.ics.model.Vector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
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

            Elipse referenceElipse = getReferenceElipse();

            ElipsesPositionsMap approximateElipsesPositions = new ElipsesPositionsMap(image, directionalMap, referenceElipse);
            double[][] votesMap = approximateElipsesPositions.getVotesMap();

            drawVotes(votesMap);

            List<Point> elipsesCenters = approximateElipsesPositions.findCenters(CENTERS_NUMBER);
            drawElipsesCenters(image, referenceElipse, elipsesCenters);
//            drawElipsesCenters(directionalMap.getDirectionalImage(), referenceElipse, elipsesCenters);

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

    public static void drawElipsesCenters(BufferedImage image, Elipse elipse, List<Point> elipsesCenters) throws IOException {

        for (Point point : elipsesCenters) {

            Graphics2D graphics2D = (Graphics2D) image.getGraphics();
            graphics2D.setColor(Color.RED);
            graphics2D.drawOval(point.getX(), point.getY(), (int) elipse.getSemiAxeWidth() * 2, (int) elipse.getSemiAxeHeight() * 2);

        }

        ImageIO.write(image, "JPG", new File(OUTPUT_ELIPSES_IMAGE));
    }

}
