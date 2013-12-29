package pl.lodz.p.ics.model.classification;

import pl.lodz.p.ics.model.Point;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * User: maciek
 * Date: 27.12.13
 * Time: 15:53
 */
@XmlRootElement
public class Feature {

    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "field")
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

    public Point getFeatureMaxPoint() {

        Point maxPoint = new Point(0, 0);
        for (Field field : fields) {

            Point bottom = field.getBottomRightPoint();
            if (bottom.getX() > maxPoint.getX()) {
                maxPoint.setX(bottom.getX());
                if (bottom.getY() > maxPoint.getY()) {
                    maxPoint.setY(bottom.getY());
                }
            }

        }
        return maxPoint;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feature feature = (Feature) o;

        if (!fields.equals(feature.fields)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return fields.hashCode();
    }

}

