package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

/**
 * this class will represent a plane in 3D space
 * @author Elad and Amitay
 */
public class Plane implements Geometry {
    private final Point q;
    private final Vector normal;

    /**
     * constructor that gets a point and a normal vector and creates the plane
     *
     * @param q      a point on the plane
     * @param normal the normal vector of the plane
     */
    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }

    /**
     * constructor that gets three points, find the normal and creates the plane
     *
     * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     */
    public Plane(Point p1, Point p2, Point p3) {
        if (p1.equals(p2))
            throw new IllegalArgumentException("ERROR: constructing a plane with coalesces points is impossible");
        q = p1;
        normal = (p1.subtract(p2).crossProduct(p2.subtract(p3))).normalize();
    }

    /**
     * returns the plane's normal
     *
     * @return the plane's normal
     */
    public Vector getNormal() {
        return normal;
    }

    @Override
    public Vector getNormal(Point point) {
        return normal;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
