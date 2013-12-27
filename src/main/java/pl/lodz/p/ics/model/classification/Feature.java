package pl.lodz.p.ics.model.classification;

import pl.lodz.p.ics.model.Point;

import java.util.List;

/**
 * User: maciek
 * Date: 27.12.13
 * Time: 15:53
 */
public class Feature {

    private List<Field> fields;

    public Feature() {
    }

    public Feature(List<Field> fields) {
        this.fields = fields;
    }

    public double value(IntegralImage integralImage, Point relativePoint) {

        double value = 0;

        for (Field field : fields) {

            Point pointA = field.getTopLeftPoint().add(relativePoint);
            Point pointB = field.getBottomRightPoint().add(relativePoint);

            value += field.getWeight() * integralImage.getRectangleValue(pointA, pointB);
        }

        return value;
    }
}

