package pl.lodz.p.ics;

import pl.lodz.p.ics.model.*;
import pl.lodz.p.ics.model.Point;
import pl.lodz.p.ics.model.classification.Feature;
import pl.lodz.p.ics.model.classification.Field;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.abs;

/**
 * User: maciek
 * Date: 16.10.13
 * Time: 22:32
 */
public class Utils {

    public static void fillArray(int [][] rgb, int value) {
        for (int i = 0; i < rgb.length; i++) {
            Arrays.fill(rgb[i], value);
        }
    }

    public static void fillImage(BufferedImage bufferedImage, int[][] rgb) {
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                bufferedImage.setRGB(x, y, rgb[y][x]);
            }
        }
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

    public static void drawVector(BufferedImage directionalImage, double radians, int x_start, int y_start, int alfa) {
        int distance = alfa / 2;
        int x = distance + 1 + x_start;
        int y = distance + 1 + y_start;

        Graphics2D graphics = (Graphics2D) directionalImage.getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.drawLine(x, y, (int) (x + distance * Math.cos(radians)), (int) (y + distance * Math.sin(radians)));
        graphics.drawLine(x, y, (int) (x + distance * Math.cos(radians + Math.PI)), (int) (y + distance * Math.sin(radians + Math.PI)));
    }

    public static void drawFeature(List<Feature> features, int scale) {

        int i = 0;
        for (Feature f : features) {

            BufferedImage bufferedImage = new BufferedImage((f.getWidth() + 1) * scale, (f.getHeight() + 1) * scale, BufferedImage.TYPE_INT_BGR);

            Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
//            g.setBackground(Color.GRAY);
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, (f.getWidth() + 1) * scale, (f.getHeight() + 1) * scale);

            for (Field field : f.getFields()) {
                if (field.getWeight() == -1) {
                    g.setColor(Color.BLACK);
                } else if (field.getWeight() == 1) {
                    g.setColor(Color.WHITE);
                }

                Point a = field.getTopLeftPoint();
                Point b = field.getBottomRightPoint();

                g.fillRect(a.getX() * scale, a.getY() * scale, (b.getX() - a.getX() + 1) * scale - 1, (b.getY() - a.getY() + 1) * scale - 1);
            }

            File outputfile = new File("image" + (i++) + ".jpg");

            try {
                ImageIO.write(bufferedImage, "jpg", outputfile);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

    }

}
