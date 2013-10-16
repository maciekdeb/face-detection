import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

import static java.lang.Math.*;

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

            URL url = ApproximateLocation.class.getResource("/image.jpg");
            BufferedImage image = ImageIO.read(url);

            int width = image.getWidth();
            int height = image.getHeight();

            BufferedImage directionalImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            int[][] rgb = new int[height][width];
            for (int i = 0; i < rgb.length; i++) {
                Arrays.fill(rgb[i], 0xFFFFFFFF);
            }
            fillImage(directionalImage, rgb);

            List<List<Vector>> vectors = new ArrayList<List<Vector>>();
            for (int j = 1; j < height - ALFA; j += ALFA) {
                vectors.add(new ArrayList<Vector>());
            }

            for (int i = 1; i < width - ALFA; i += ALFA) {
                int index = 0;
                for (int j = 1; j < height - ALFA; j += ALFA) {
                    Double[] abc = computeABC(image, i, j, ALFA);
                    Double[] tangent = getTangent(abc);
                    double direction = getDirection(tangent);

                    vectors.get(index).add(new Vector(new Point(i, j), tangent, direction));
                    index++;

                    drawAngles(directionalImage, direction, i, j, ALFA);
                }
            }

            ImageIO.write(directionalImage, "JPG", new File("directional-image.jpg"));

            Elipse referenceElipse = new Elipse(50, 60, 1.25, 0.75);
            double[][] candidatesForElipse = prepareCandidatesForElipseCenter(image, referenceElipse, vectors);
            int[][] centers = convertRGB(candidatesForElipse);
            BufferedImage centerImage = new BufferedImage(centers[0].length, centers.length, BufferedImage.TYPE_3BYTE_BGR);
            fillImage(centerImage, centers);

            List<Point> maxCenters = findMaxValues(centers, 6);

            for (Point point : maxCenters) {
                Graphics2D graphics2D = (Graphics2D) centerImage.getGraphics();
                graphics2D.setColor(Color.RED);
                graphics2D.drawOval(point.getX(), point.getY(), (int) referenceElipse.getSemiAxeWidth() * 2, (int) referenceElipse.getSemiAxeHeight() * 2);
            }

            ImageIO.write(centerImage, "JPG", new File("centers-image.jpg"));

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static List<Point> findMaxValues(int[][] rgb, int number) {
        List<Point> result = new ArrayList<Point>();
        int width = rgb[0].length;
        int height = rgb.length;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Point point = new Point(x, y);
                point.setColor(rgb[y][x]);
                result.add(point);
            }
        }

        Collections.sort(result);

        return result.subList(0, number);
    }

    public static int[][] convertRGB(double[][] input) {
        int width = input[0].length;
        int height = input.length;
        int[][] rgb = new int[height][width];

        double max = input[0][0];
        double min = input[0][0];

        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (input[i][j] > max) {
                    max = input[i][j];
                }
                if (input[i][j] < min) {
                    min = input[i][j];
                }
            }
        }
        double unit = 255.0 / (max - min);

        for (int i = 0; i < rgb.length; i++) {
            for (int j = 0; j < rgb[i].length; j++) {
                rgb[i][j] = (int) ((input[i][j] + abs(min)) * unit);
                rgb[i][j] = (rgb[i][j] << 8) | rgb[i][j];
                rgb[i][j] = (rgb[i][j] << 8) | rgb[i][j];
            }
        }

        return rgb;
    }

    public static void fillImage(BufferedImage bufferedImage, int[][] rgb) {
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                bufferedImage.setRGB(x, y, rgb[y][x]);
            }
        }
    }

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

    public static Double[] computeABC(BufferedImage bufferedImage, int x_start, int y_start, int alfa) {
        double A = 0, B = 0, C = 0, a1, a2, a3, a4, a, b;

        for (int x = x_start; x < x_start + alfa; x++) {
            for (int y = y_start; y < y_start + alfa; y++) {

                a1 = (bufferedImage.getRGB(x + 1, y + 1) >> 16) & 0xFF;
                a2 = (bufferedImage.getRGB(x - 1, y + 1) >> 16) & 0xFF;
                a3 = (bufferedImage.getRGB(x - 1, y - 1) >> 16) & 0xFF;
                a4 = (bufferedImage.getRGB(x + 1, y - 1) >> 16) & 0xFF;

                a = (-a1 + a2 + a3 - a4) / 4;
                b = (-a1 - a2 + a3 + a4) / 4;

                A += a * a;
                B += b * b;
                C += a * b;
            }
        }
        return new Double[]{A, B, C};
    }

    public static Double[] getTangent(Double[] abc) {

        double A = abc[0], B = abc[1], C = abc[2];

        if (C == 0 && A <= B) {
            return new Double[]{1.0, 0.0};
        } else if (C == 0 && A > B) {
            return new Double[]{0.0, 1.0};
        } else {
            double frac = (B - A) / (2 * C);
            return new Double[]{1.0, frac - (Math.copySign(1, C) * Math.sqrt(frac * frac + 1))};
        }

    }

    public static double getDirection(Double[] t) {
        if (t[0] != 0) {
            return (Math.atan(t[1] / t[0]) >= 0 ? Math.atan(t[1] / t[0]) : 2 * Math.PI + Math.atan(t[1] / t[0]));
        } else {
            return Math.PI / 2.0;
        }
    }

    public static void drawAngles(BufferedImage directionalImage, double radians, int x_start, int y_start, int alfa) {
        int distance = alfa / 2;
        int x = distance + 1 + x_start;
        int y = distance + 1 + y_start;

        Graphics2D graphics = (Graphics2D) directionalImage.getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.drawLine(x, y, (int) (x + distance * Math.cos(radians)), (int) (y + distance * Math.sin(radians)));
        graphics.drawLine(x, y, (int) (x + distance * Math.cos(radians + Math.PI)), (int) (y + distance * Math.sin(radians + Math.PI)));
    }

}
