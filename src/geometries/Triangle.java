package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;


/**
 * Triangle class represents two-dimensional triangle
 * @author Elad and Amitay
 */

public class Triangle extends Polygon {
    /**
     * constructor that gets 3 points and creates the triangle
     *
     * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<Point> planeIntersection = plane.findIntersections(ray);
        if (planeIntersection == null)//no intersections with the plane
            return null;
        // vectors from ray's head to vertices
        Vector v1 = vertices.get(0).subtract(ray.getHead());
        Vector v2 = vertices.get(1).subtract(ray.getHead());
        Vector v3 = vertices.get(2).subtract(ray.getHead());

        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        //if plane's intersection is inside the triangle
        if(Util.compareSign(ray.getDirection().dotProduct(n1),ray.getDirection().dotProduct(n2)) &&
        Util.compareSign(ray.getDirection().dotProduct(n2),ray.getDirection().dotProduct(n3)) &&
        Util.compareSign(ray.getDirection().dotProduct(n3),ray.getDirection().dotProduct(n1)))
            return List.of(new GeoPoint(this,planeIntersection.get(0)));

        return null;
    }
}
