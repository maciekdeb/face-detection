package pl.lodz.p.ics;

import java.awt.image.BufferedImage;

/**
 * User: maciek
 * Date: 16.10.13
 * Time: 22:47
 */
public class DirectionalMap {

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

}
