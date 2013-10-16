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
}
