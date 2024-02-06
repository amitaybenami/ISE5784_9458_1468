package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;
import java.util.Objects;

/**
 * this interface is a general interface for every geometric object that will be intersected
 * @author Elad and Amitay
 */
public abstract class Intersectable {
    /**
     * returns list of all the intersection points of the object with a given ray
     * @param ray the given ray
     * @return the list of the intersection points ; null if there are no intersections
     */
    public List<Point> findIntersections(Ray ray){
        return findIntersections(ray, Double.POSITIVE_INFINITY);
    }
    /**
     * returns list of the intersection points of the object with a given ray up to a maximum distance
     * @param ray the given ray
     * @param maxDistance the maximum distance
     * @return the list of the intersection points ; null if there are no intersections
     */
    public List<Point> findIntersections(Ray ray, double maxDistance){
        List<GeoPoint> points = findGeoIntersectionsHelper(ray, maxDistance);
        if (points == null)
            return null;
        return points.stream().map(gp -> gp.point).toList();
    }

    /**
     * represents a point on a geometry
     */
    public static class GeoPoint {
        /**
         * the geometry the point on
         */
        public Geometry geometry;
        /**
         * the point
         */
        public Point point;

        /**
         * simple constructor
         * @param geometry the geometry
         * @param point the point
         */
        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GeoPoint geoPoint)) return false;
            return Objects.equals(geometry, geoPoint.geometry) && Objects.equals(point, geoPoint.point);
        }

        @Override
        public String toString() {
            return "GeoPoint{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }

    /**
     * find all the intersections of an intersectable with a ray
     * @param ray the ray
     * @return list of the intersections as geo points ; null if there are no intersections
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray) {
        return findGeoIntersections(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * find intersections of an intersectable with a ray up to a maximum distance
     * using NVI design pattern
     * @param ray the ray
     * @param maxDistance the maximum distance
     * @return list of the intersections as geo points ; null if there are no intersections
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray, double maxDistance) {
        return findGeoIntersectionsHelper(ray, maxDistance);
    }

    /**
     * find intersections of an intersectable with a ray up to a maximum distance
     * using NVI design pattern
     * @param ray the ray
     * @param maxDistance the maximum distance
     * @return list of the intersections as geo points ; null if there are no intersections
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance);

}
