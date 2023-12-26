package primitives;

import java.util.Objects;

/**
 * this class will present a point in the space and it will serve all primitives
 */
public class Point {
    public static final Point ZERO = new Point(Double3.ZERO);
    /**
     * the value of the point
     */
    protected final Double3 xyz;

    /**
     * this constructor gets Double3 and assign it as the point
     *
     * @param xyz the value of the point
     */
    Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * constructor that gets 3 doubles and make the matching point
     *
     * @param d1 first number value
     * @param d2 second number value
     * @param d3 third number value
     */
    public Point(double d1, double d2, double d3) {
        this.xyz = new Double3(d1, d2, d3);
    }

    /**
     * Subtract two points into a new point by subtracting their Double3 fields
     *
     * @param p right hand side operand for subtraction
     * @return result of subtraction
     */
    public Vector subtract(Point p) {
        return new Vector(xyz.subtract(p.xyz));
    }

    /**
     * Add two points into a new point by adding their Double3 fields
     *
     * @param p right hand side operand for addition
     * @return result of addition
     */
    public Point add(Point p) {
        return new Point(xyz.add(p.xyz));
    }

    /**
     * this function calculates the squared distance of the point from a given point by adding
     * the squared value of the subtraction of each number of the Double3 fields
     *
     * @param p the point we calculate the distance from
     * @return the squared distance
     */
    public double distanceSquared(Point p) {
        return (xyz.d1 - p.xyz.d1) * (xyz.d1 - p.xyz.d1) + (xyz.d2 - p.xyz.d2) * (xyz.d2 - p.xyz.d2) + (xyz.d3 - p.xyz.d3) * (xyz.d3 - p.xyz.d3);
    }

    /**
     * is function calculates the distance of the point from a given point by returning
     * the square root of the squared distance between them
     *
     * @param p the point we calculate the distance from
     * @return the distance
     */
    public double distance(Point p) {
        return Math.sqrt(distanceSquared(p));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point point)) return false;
        return Objects.equals(xyz, point.xyz);
    }

    @Override
    public String toString() {
        return "" + xyz;
    }

}
