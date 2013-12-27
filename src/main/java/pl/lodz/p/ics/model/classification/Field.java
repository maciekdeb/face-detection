package pl.lodz.p.ics.model.classification;

import pl.lodz.p.ics.model.Point;

/**
 * User: maciek
 * Date: 27.12.13
 * Time: 22:38
 */
public class Field {

    public Point topLeftPoint;
    public Point bottomRightPoint;

    public int weight;

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
}
