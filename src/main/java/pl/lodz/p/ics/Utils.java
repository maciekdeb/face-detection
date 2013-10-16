package pl.lodz.p.ics;

import java.awt.*;
import java.awt.image.BufferedImage;
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

    public static List<pl.lodz.p.ics.model.Point> findMaxValues(int[][] rgb, int number) {
        List<pl.lodz.p.ics.model.Point> result = new ArrayList<pl.lodz.p.ics.model.Point>();

        int width = rgb[0].length;
        int height = rgb.length;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pl.lodz.p.ics.model.Point point = new pl.lodz.p.ics.model.Point(x, y);
                point.setColor(rgb[y][x]);
                result.add(point);
            }
        }

        Collections.sort(result);

        return result.subList(0, number);
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
