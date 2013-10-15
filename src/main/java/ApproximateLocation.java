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

    public static void main(String[] args) {
        try {

            URL url = ApproximateLocation.class.getResource("/image.jpg");
            BufferedImage bufferedImage = ImageIO.read(url);

            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

            int ALFA = 10;

            BufferedImage directionalImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

            for (int y = 0; y < width; y++) {
                for (int x = 0; x < height; x++) {
                    directionalImage.setRGB(y, x, 0xFFFFFFFF);
                }
            }

           /* angles = [[]]
            # n = 0
            for i in range(1, width - ALFA, ALFA):
            angles.append([])
            for j in range(1, height - ALFA, ALFA):
            abc = computeABC(i, j, ALFA)
            t = getTangent(abc)
            fi = getDirection(t)
            draw.drawAngles(fi, directionalImage, i, j, ALFA)
            # angle = fi * 180 / math.pi
            # angles[n].append(angle)
            # n += 1*/

            List<List<Vector>> vectors = new ArrayList<List<Vector>>();
            for (int j = 1; j < height - ALFA; j += ALFA) {
                vectors.add(new ArrayList<Vector>());
            }

            for (int i = 1; i < width - ALFA; i += ALFA) {
                int index = 0;
                for (int j = 1; j < height - ALFA; j += ALFA) {
                    Double[] abc = computeABC(bufferedImage, i, j, ALFA);
                    Double[] tangent = getTangent(abc);
                    double direction = getDirection(tangent);

                    vectors.get(index).add(new Vector(new Point(i, j), tangent, direction));
                    index++;

                    drawAngles(directionalImage, direction, i, j, ALFA);
                }
            }

            for (List vector : vectors) {
                System.out.println(vector.toString());
            }

            ImageIO.write(directionalImage, "JPG", new File("directional-image.jpg"));

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
    public static Double[][] prepareCandidatesForElipseCenter(BufferedImage bufferedImage, List<List<Vector>> vectors) {
        Double[][] candidatesMap = new Double[bufferedImage.getHeight()][bufferedImage.getWidth()];

        Elipse referenceElipse = new Elipse(40, 50, 1.25, 0.75);

        for (List<Vector> vectorList : vectors) {
            for (Vector vector : vectorList) {
                List<Point> currentTemplate = currentTemplate(vector.getOrigin(), vector.getDirection(), referenceElipse);


            }
        }

        return candidatesMap;
    }

    public static List<Point> currentTemplate(Point origin, double direction, Elipse referenceElipse) {
        List<Point> currentTemplate = new ArrayList<Point>();

        double pr = referenceElipse.getReductionCoefficient();
        double pe = referenceElipse.getReductionCoefficient();
        double a = referenceElipse.getSemiAxeWidth();
        double b = referenceElipse.getSemiAxeHeight();

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {

                double validityExpression = (Math.pow((x - origin.getX()) / a, 2) + Math.pow((y - origin.getY()) / b, 2));
                if ((pr * pr) <= validityExpression && validityExpression <= pe * pe) {
                    if (true) {
                    }
                }
            }
        }


        return currentTemplate;
    }

    public static double smallerAngleFromDirections(double alfa, double beta) {
        return 0;
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
            return Math.atan(t[1] / t[0]);
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
