package pl.lodz.p.ics.model.classification;

import pl.lodz.p.ics.model.Point;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * User: maciek
 * Date: 11.01.14
 * Time: 23:00
 */
public class Detector {

    public static final int POSITIVE = 1;

    private StrongClassifier strongClassifier;

    private int windowSize;

    public Detector(StrongClassifier strongClassifier) {
        this.strongClassifier = strongClassifier;
        this.windowSize = this.strongClassifier.getWeakClassifiers()[0].getFeature().getHeight();
    }

    public Map<Integer, Point> scan(BufferedImage bufferedImage, SlidingWindow sw) {
        Map<Integer, Point> wSizeFacePositions = new HashMap<Integer, Point>();
        List<IntegralImage> integralPyramid = buildIntegralPyramid(bufferedImage, sw.getScale(), sw.getScalingSteps());

        for (int step = 0; step < sw.getScalingSteps(); step++) {
            int size = (int) (windowSize * Math.pow(1.0 / sw.getScale(), step));
            IntegralImage ii = integralPyramid.get(step);
            for (int i = 0; i + windowSize < ii.getIntegralImage()[0].length; i += sw.getX()) {
                for (int j = 0; j + windowSize < ii.getIntegralImage().length; j += sw.getY()) {
                    Point relativePoint = new Point(i, j);
                    if (strongClassifier.detect(ii, relativePoint) == POSITIVE) {
                        Point point = new Point((int) (i * Math.pow(1.0 / sw.getScale(), step)), (int) (j * Math.pow(1.0 / sw.getScale(), step)));
                        wSizeFacePositions.put(size, point);
                    }
                }
            }
        }

        return wSizeFacePositions;
    }

    public List<IntegralImage> buildIntegralPyramid(BufferedImage bi, double scale, int steps) {
        List<IntegralImage> integralPyramid = new ArrayList<IntegralImage>();

        for (int step = 0; step < steps; step++) {
            double newScale = Math.pow(scale, step);
            BufferedImage newBufferedImage = getScaledImage(bi, newScale, newScale);
            IntegralImage ii = new IntegralImage(newBufferedImage, 255.0);
            integralPyramid.add(ii);
        }

        return integralPyramid;
    }

    public static BufferedImage getScaledImage(BufferedImage image, double scaleX, double scaleY) {
        int imageWidth  = image.getWidth();
        int imageHeight = image.getHeight();

        int width = (int) (scaleX * imageWidth);
        int height = (int) (scaleY * imageHeight);

        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
    }

}
