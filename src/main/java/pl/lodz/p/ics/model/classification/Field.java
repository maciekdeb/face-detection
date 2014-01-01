package pl.lodz.p.ics.model.classification;

import pl.lodz.p.ics.model.Point;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: maciek
 * Date: 27.12.13
 * Time: 22:38
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Field {

    @XmlElement
    private Point bottomRightPoint;

    @XmlElement
    private Point topLeftPoint;

    @XmlElement
    private int weight;

    @XmlElement
    private int rowX;

    @XmlElement
    private int columnY;

    public Field() {

    }

    public Field(Point topLeftPoint, Point bottomRightPoint, int weight) {
        this.topLeftPoint = topLeftPoint;
        this.bottomRightPoint = bottomRightPoint;
        this.weight = weight;
    }

    public Point getTopLeftPoint() {
        return topLeftPoint;
    }

    public void setTopLeftPoint(Point topLeftPoint) {
        this.topLeftPoint = topLeftPoint;
    }

    public Point getBottomRightPoint() {
        return bottomRightPoint;
    }

    public void setBottomRightPoint(Point bottomRightPoint) {
        this.bottomRightPoint = bottomRightPoint;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getRowX() {
        return rowX;
    }

    public void setRowX(int rowX) {
        this.rowX = rowX;
    }

    public int getColumnY() {
        return columnY;
    }

    public void setColumnY(int columnY) {
        this.columnY = columnY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (weight != field.weight) return false;
        if (!bottomRightPoint.equals(field.bottomRightPoint)) return false;
        if (!topLeftPoint.equals(field.topLeftPoint)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = bottomRightPoint.hashCode();
        result = 31 * result + topLeftPoint.hashCode();
        result = 31 * result + weight;
        return result;
    }

    @Override
    public String toString() {
        return "Field{" +
                "bottomRightPoint=" + bottomRightPoint +
                ", topLeftPoint=" + topLeftPoint +
                ", weight=" + weight +
                '}';
    }
}
