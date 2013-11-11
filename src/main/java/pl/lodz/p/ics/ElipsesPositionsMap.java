package pl.lodz.p.ics;

import pl.lodz.p.ics.model.Elipse;
import pl.lodz.p.ics.model.Point;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: maciek
 * Date: 11.11.13
 * Time: 22:01
 */
public class ElipsesPositionsMap {

    private double[][] votesMap;

    public ElipsesPositionsMap(BufferedImage image, DirectionalMap directionalMap, Elipse referenceElipse) {

        votesMap = HoughTransform.prepareCandidatesForElipseCenter(image, referenceElipse, directionalMap.getVectors());

    }

    public double[][] getVotesMap() {
        return votesMap;
    }

    public List<Point> findCenters(int centersNumber) {
        List<Point> result = new ArrayList<Point>();

        int width = votesMap[0].length;
        int height = votesMap.length;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Point point = new Point(x, y);
                point.setColor((int) votesMap[y][x]);
                result.add(point);
            }
        }

        Collections.sort(result);

        return result.subList(0, centersNumber);
    }
}
