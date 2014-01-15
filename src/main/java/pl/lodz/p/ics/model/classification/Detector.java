package pl.lodz.p.ics.model.classification;

import pl.lodz.p.ics.model.Point;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: maciek
 * Date: 11.01.14
 * Time: 23:00
 */
public class Detector {

    private StrongClassifier strongClassifier;
    private SlidingWindow slidingWindow;

    public Detector(StrongClassifier strongClassifier) {
        this.strongClassifier = strongClassifier;
    }

    public Map<Integer, Point> scan(IntegralImage integralImage) {

        Map<Integer, Point> facePositions = new HashMap<Integer, Point>();

        for (int i = 0; i < slidingWindow.getScalingSteps(); i++) {
            //todo
            Point relativePoint = new Point();
            strongClassifier.detect(integralImage, relativePoint);
        }

        return facePositions;
    }

    public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
        int imageWidth  = image.getWidth();
        int imageHeight = image.getHeight();

        double scaleX = (double)width/imageWidth;
        double scaleY = (double)height/imageHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
    }

}
