package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

/**
 * this class represent a sphere in 3D space
 *
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
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        if (center.equals(ray.getHead()))//ray starts at center
            return List.of(new GeoPoint(this, ray.getPoint(radius)));

        Vector headToCenter = center.subtract(ray.getHead());
        //tm = distance from ray's head to point in ray that is closest to sphere's center
        double tm = Util.alignZero(ray.getDirection().dotProduct(headToCenter));
        //d = distance from sphere's center to the closest point in the ray
        double d = Util.alignZero(Math.sqrt(headToCenter.lengthSquared() - tm * tm));

        if (d >= radius || (tm < 0 && headToCenter.lengthSquared() >= radius * radius)) // there are no intersections
            return null;

        //th = distance between intersection and the closest point in the ray to sphere's center
        double th = Util.alignZero(Math.sqrt(radius * radius - d * d));

        if (tm - th > 0 && Util.alignZero((tm - th) - maxDistance) <= 0)//two intersections
            if (Util.alignZero(tm + th - maxDistance) <= 0)
                return List.of(new GeoPoint(this, ray.getPoint(tm - th)), new GeoPoint(this, ray.getPoint(tm + th)));
            else return List.of(new GeoPoint(this, ray.getPoint(tm - th)));

        //one intersection
        if (Util.alignZero(tm + th - maxDistance) <= 0)
            return List.of(new GeoPoint(this, ray.getPoint(tm + th)));
        return null;
    }
}
