package pl.lodz.p.ics;

import pl.lodz.p.ics.model.Elipse;
import pl.lodz.p.ics.model.Point;
import pl.lodz.p.ics.model.Vector;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.*;
import static java.lang.Math.PI;
import static java.lang.Math.atan;

/**
 * User: maciek
 * Date: 16.10.13
 * Time: 22:31
 */
public class HoughTransform {

    /**
     * reset A
     * for v in vectors:
     * x0,y0 = origin(v)
     * direction = direction(v)
     * modulus = modulus(d)
     * T = current_template([x0,y0], direction)
     * for pixel in T:
     * A[x,y]=A[x,y] + modulus*weightT([x,y])
     */
    public static double[][] prepareCandidatesForElipseCenter(BufferedImage bufferedImage, Elipse referenceElipse, List<List<Vector>> vectors) {

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        /**
         * Maio, Maltoni: A
         */
        double[][] candidatesMap = new double[height][width];


        for (List<Vector> vectorList : vectors) {
            for (Vector vector : vectorList) {

                double beta = atan(-referenceElipse.getSemiAxeHeight() / (referenceElipse.getSemiAxeWidth() * tan(vector.getDirection())));
                //TODO theta
                double theta = 0.8;

                List<Point> currentTemplate = currentTemplate(bufferedImage, vector.getOrigin(), referenceElipse, beta, theta);

                for (Point point : currentTemplate) {
                    int x = point.getX();
                    int y = point.getY();

                    //todo modulus??
                    candidatesMap[y][x] += abs(vector.getTangent()[1]) * weightT(point, vector.getOrigin(), theta, beta);
                }
            }
        }

        return candidatesMap;
    }

    public static List<Point> currentTemplate(BufferedImage bufferedImage, Point origin, Elipse referenceElipse, double theta, double beta) {
        List<Point> currentTemplate = new ArrayList<Point>();

        double pr = referenceElipse.getReductionCoefficient();
        double pe = referenceElipse.getReductionCoefficient();
        double a = referenceElipse.getSemiAxeWidth();
        double b = referenceElipse.getSemiAxeHeight();
        double x0 = origin.getX();
        double y0 = origin.getY();

        if (x0 - a - 1 < 1 || x0 + a + 1 > bufferedImage.getWidth() || y0 - b - 1 < 1 || y0 + b + 1 > bufferedImage.getHeight()) {
            return Collections.emptyList();
        }

        //TODO xy
        for (int x = (int) (x0 - a - 1); x < (int) (x0 + a + 1); x++) {
            for (int y = (int) (y0 - b - 1); y < (int) (y0 + b + 1); y++) {

                double validityExpression = (Math.pow((x - x0) / a, 2) + Math.pow((y - y0) / b, 2));
                if ((pr * pr) <= validityExpression && validityExpression <= (pe * pe)) {

                    if (angleFromDirections(atan((y - y0) / (x - x0)), beta) <= (theta / 2.0)) {
                        currentTemplate.add(new Point(x, y));
                    }
                }
            }
        }

        return currentTemplate;
    }

    /**
     * @return smaller angle from directions alfa beta
     */
    public static double angleFromDirections(double alfa, double beta) {
        double delta = abs(alfa - beta);
        if (delta > PI) {
            return 2 * PI - delta;
        } else {
            return delta;
        }
    }

    public static double weightT(Point point, Point origin, double theta, double beta) {
        int x0 = origin.getX();
        int y0 = origin.getY();
        int x = point.getX();
        int y = point.getY();

        if (x0 == x || y0 == y) {
            return 0;
        }

        double angle = angleFromDirections(atan((y - y0) / (x - x0)), beta);
        return 1.0 - 2.0 * angle / theta;
    }

}
