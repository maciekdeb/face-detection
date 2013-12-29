package pl.lodz.p.ics.model;

/**
 * User: maciek
 * Date: 15.10.13
 * Time: 13:14
 */
public class Point implements Comparable {

    int x;
    int y;

    int color;

    public Point(){}

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Point add(int x, int y) {
        return new Point(this.getX() + x, this.getY() + y);
    }

    public Point add(Point other) {
        return new Point(this.getX() + other.getX(), this.getY() + other.getY());
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", color=" + color +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Point point = (Point) o;

        int a = (point.getColor() >> 16) & 0xFF;
        int b = (this.getColor() >> 16) & 0xFF;

        return a > b ? +1 : a < b ? -1 : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (color != point.color) return false;
        if (x != point.x) return false;
        if (y != point.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + color;
        return result;
    }
}
