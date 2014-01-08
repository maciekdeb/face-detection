package pl.lodz.p.ics;

import java.awt.image.BufferedImage;

/**
 * User: maciek
 * Date: 06.01.14
 * Time: 19:06
 */
public class HistogramUtils {

    public static BufferedImage equalizeHistogram(BufferedImage image) {
        int[] histogram = createHistogram(image);
        double[][] cdf = calculateCDF(calculatePMF(histogram));

        //CDF * (Levels-1)
        for (int i = 0; i < cdf[1].length; i++) {
            cdf[1][i] *= 255;
        }

        //Map new values, now histogram stores new grayscale value mapped to old values
        int j = 0;
        for (int i = 0; i < histogram.length; i++) {
            if (histogram[i] > 0) {
                histogram[i] = (int) cdf[1][j++];
            }
        }

        for (int a = 0; a < image.getWidth(); a++) {
            for (int b = 0; b < image.getHeight(); b++) {
                int color = histogram[(image.getRGB(a, b) >> 8 & 0xFF)];
                color |= color << 8;
                color |= color << 16;
                image.setRGB(a, b, color);
            }
        }

        return image;
    }

    public static int[] createHistogram(BufferedImage bufferedImage) {
        int[] histogram = new int[256];
        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                histogram[((bufferedImage.getRGB(i, j) >> 8) & 0xFF)]++;
            }
        }
        return histogram;
    }

    /**
     * Probability mass function
     *
     * @return the probability mass function array
     */
    public static double[][] calculatePMF(int[] histogram) {
        double sum = 0;
        int nonZero = 0;
        for (int i = 0; i < histogram.length; i++) {
            if (histogram[i] > 0) {
                sum += histogram[i];
                nonZero++;
            }
        }

        double[][] pmf = new double[2][nonZero];
        int j = 0;
        for (int i = 0; i < histogram.length; i++) {
            if (histogram[i] > 0) {
                pmf[0][j] = i;
                pmf[1][j] = histogram[i] / sum;
                j++;
            }
        }
        return pmf;
    }

    /**
     * Cumulative distributive function
     *
     * @return the cumulative distributive function array
     */
    public static double[][] calculateCDF(double[][] input) {
        for (int i = 0; i < input[0].length - 1; i++) {
            input[1][i + 1] += input[1][i];
        }
        return input;
    }

}
