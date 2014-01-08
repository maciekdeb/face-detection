package pl.lodz.p.ics;

import pl.lodz.p.ics.model.*;
import pl.lodz.p.ics.model.Point;
import pl.lodz.p.ics.model.classification.Feature;
import pl.lodz.p.ics.model.classification.Field;
import pl.lodz.p.ics.model.classification.IntegralImage;
import pl.lodz.p.ics.model.classification.StrongClassifier;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
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

    public static int convertRGB(int a) {
        return (a >> 8) & 0xFF;
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

    public static void drawFeature(List<Feature> features, String prefix, int scale) {

        int i = 0;
        for (Feature f : features) {

            BufferedImage bufferedImage = new BufferedImage((f.getWidth()) * scale, (f.getHeight()) * scale, BufferedImage.TYPE_INT_BGR);

            Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, (f.getWidth()) * scale, (f.getHeight()) * scale);

            for (Field field : f.getFields()) {
                if (field.getWeight() == -1) {
                    g.setColor(Color.BLACK);
                } else if (field.getWeight() == 1) {
                    g.setColor(Color.WHITE);
                }

                Point a = field.getTopLeftPoint();
                Point b = field.getBottomRightPoint();

                g.fillRect(a.getX() * scale, a.getY() * scale, ((b.getX() - a.getX()) + 1) * scale, ((b.getY() - a.getY()) + 1) * scale);
            }

            File outputfile = new File(prefix + "-" + (i++) + ".jpg");

            try {
                ImageIO.write(bufferedImage, "jpg", outputfile);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

    }

    public static List<DataSample> loadDataSet(File directory, double classificationResponse, int numberOfFiles, boolean normalize) throws IOException {

        List<DataSample> dataSamples = new ArrayList<DataSample>();

        int i = 0;
        for (final File file : directory.listFiles()) {
            if (i++ == numberOfFiles) {
                break;
            }
            BufferedImage image = ImageIO.read(file);
            if (normalize) {
                image = HistogramUtils.equalizeHistogram(image);
            }
            IntegralImage integralImage = new IntegralImage(image, 255.0);
            dataSamples.add(new DataSample(integralImage, classificationResponse));
        }

        return dataSamples;
    }

    public static List<DataSample> loadDataSet(File directory, double classificationResponse, boolean normalize) throws IOException {

        List<DataSample> dataSamples = new ArrayList<DataSample>();

        int i = 0;
        for (final File file : directory.listFiles()) {
            BufferedImage image = ImageIO.read(file);
            if (normalize) {
                image = HistogramUtils.equalizeHistogram(image);
            }
            IntegralImage integralImage = new IntegralImage(image, 255.0);
            dataSamples.add(new DataSample(integralImage, classificationResponse));
        }

        return dataSamples;
    }

    public static List<Feature> loadFeatures(List<String> fileNames) {

        List<Feature> features = new ArrayList<Feature>();

        try {

            JAXBContext context = JAXBContext.newInstance(Feature.class);

            for (String name : fileNames) {
                Unmarshaller um = context.createUnmarshaller();
                features.add((Feature) um.unmarshal(new File(name)));
            }

        } catch (JAXBException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return features;
    }

    public static List<Feature> loadFeatures(File directory) {

        List<Feature> features = new ArrayList<Feature>();

        for (final File file : directory.listFiles()) {

            try {

                JAXBContext context = JAXBContext.newInstance(Feature.class);

                Unmarshaller um = context.createUnmarshaller();
                features.add((Feature) um.unmarshal(file));

            } catch (JAXBException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }


        return features;
    }

    public static void saveClassifier(StrongClassifier strongClassifier, String fileName) {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(fileName + new Date().getTime());
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(strongClassifier);
            out.close();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static StrongClassifier loadClassifier(String fileName) {
        FileInputStream fileIn = null;
        StrongClassifier strongClassifier = null;
        try {
            fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            strongClassifier = (StrongClassifier) in.readObject();
            in.close();
            fileIn.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return strongClassifier;
    }
}
