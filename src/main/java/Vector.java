import java.util.Arrays;

/**
 * User: maciek
 * Date: 15.10.13
 * Time: 12:27
 */
public class Vector {

    private Double[] tangent;
    private double direction;
    private Point origin;

    public Vector() {
    }

    public Vector(Point origin, Double[] tangent, double direction) {
        this.origin = origin;
        this.tangent = tangent;
        this.direction = direction;
    }

    public Double[] getTangent() {
        return tangent;
    }

    public void setTangent(Double[] tangent) {
        this.tangent = tangent;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public Point getOrigin() {
        return origin;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "{x,y=" + origin.getX() + "," + origin.getY() + "}[" + tangent[0] + ", " + String.format("%8.2f", tangent[1]) + "]{dir: " + String.format("%5.2f", direction) + "}";
    }
}
