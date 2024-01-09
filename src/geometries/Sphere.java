package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Util;

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
        if(center.equals(ray.getHead()))//ray starts at center
            return List.of(ray.getHead().add(ray.getDirection().scale(radius)));

        Vector headToCenter = center.subtract(ray.getHead());
        //tm = distance from ray's head to point in ray that is closest to sphere's center
        double tm = Util.alignZero(ray.getDirection().dotProduct(headToCenter));
        //d = distance from sphere's center to the closest point in the ray
        double d = Util.alignZero(Math.sqrt(headToCenter.lengthSquared() - tm*tm));

        if (d >= radius || tm < 0 ) // there are no intersections
            return null;

        //th = distance between intersection and the closest point in the ray to sphere's center
        double th = Util.alignZero(Math.sqrt(radius*radius - d*d));

        if (tm - th > 0)//two intersections
            return List.of(ray.getHead().add(ray.getDirection().scale(tm-th)),ray.getHead().add(ray.getDirection().scale(tm+th)));
        //one intersection
        return List.of(ray.getHead().add(ray.getDirection().scale(tm+th)));
    }
}
