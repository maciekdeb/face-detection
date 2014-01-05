package pl.lodz.p.ics.model.classification;

import pl.lodz.p.ics.model.Point;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * User: maciek
 * Date: 27.12.13
 * Time: 15:53
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Feature implements Serializable{

    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "field")
    private List<Field> fields;

    @XmlElement
    private int width;

    @XmlElement
    private int height;

    public Feature() {}

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

    public int getMaxXRow() {
        int max = 1;
        for (Field f : fields) {
            if (f.getRowX() > max) {
                max = f.getRowX();
            }
        }
        return max;
    }

    public int getMaxYColumn() {
        int max = 1;
        for (Field f : fields) {
            if (f.getColumnY() > max) {
                max = f.getColumnY();
            }
        }
        return max;
    }

    public Point getFeatureMaxPoint() {

        Point maxPoint = new Point(0, 0);
        for (Field field : fields) {

            Point bottom = field.getBottomRightPoint();
            if (bottom.getX() > maxPoint.getX()) {
                maxPoint.setX(bottom.getX());
            }
            if (bottom.getY() > maxPoint.getY()) {
                maxPoint.setY(bottom.getY());
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

