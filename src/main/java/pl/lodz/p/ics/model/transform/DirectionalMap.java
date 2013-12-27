package pl.lodz.p.ics.model.transform;

import pl.lodz.p.ics.Utils;
import pl.lodz.p.ics.model.Point;
import pl.lodz.p.ics.model.Vector;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maciek
 * Date: 16.10.13
 * Time: 22:47
 */
public class DirectionalMap {

    private int alfa;
    private int width;
    private int height;
    private BufferedImage image;

    private List<List<Vector>> vectors;
    private BufferedImage directionalImage;


    public DirectionalMap(BufferedImage image, int alfa) {
        this.alfa = alfa;
        this.height = image.getHeight();
        this.width = image.getWidth();
        this.directionalImage = prepareDirectionalImage(width, height);
        this.image = image;
        this.vectors = new ArrayList<List<Vector>>();
        for (int j = 1; j < height - alfa; j += alfa) {
            this.vectors.add(new ArrayList<Vector>());
        }
    }

    public DirectionalMap build() {

        for (int i = 1; i < width - alfa; i += alfa) {

            for (int j = 1, index = 0; j < height - alfa; j += alfa, index++) {

                Double[] abc = DirectionalMap.computeABC(image, i, j, alfa);
                Double[] tangent = DirectionalMap.getTangent(abc);
                double direction = DirectionalMap.getDirection(tangent);

                vectors.get(index).add(new Vector(new Point(i, j), tangent, direction));

                Utils.drawVector(directionalImage, direction, i, j, alfa);

            }

        }

        return this;
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

    public static BufferedImage prepareDirectionalImage(int width, int height) {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        int[][] rgb = new int[height][width];
        Utils.fillArray(rgb, 0xFFFFFFFF);
        Utils.fillImage(result, rgb);
        return result;
    }

    public int getAlfa() {
        return alfa;
    }

    public void setAlfa(int alfa) {
        this.alfa = alfa;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public List<List<Vector>> getVectors() {
        return vectors;
    }

    public void setVectors(List<List<Vector>> vectors) {
        this.vectors = vectors;
    }

    public BufferedImage getDirectionalImage() {
        return directionalImage;
    }

    public void setDirectionalImage(BufferedImage directionalImage) {
        this.directionalImage = directionalImage;
    }
}
