package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * this class represent a two-dimensional sphere in 3D space
 * @author Elad and Amitay
 */
public class Sphere extends RadialGeometry {
    private final Point center;

    /**
     * constructor that gets the center point and the radius and creates sphere
     *
     * @param radius the radius
     * @param center the center point of the sphere
     */
    public Sphere(double radius, Point center) {
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
