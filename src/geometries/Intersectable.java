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
     * returns list of the intersection points of the object with a given ray
     * @param ray the given ray
     * @return the list of the intersection points
     */
    public List<Point> findIntersections(Ray ray){

        List<GeoPoint> points = findGeoIntersectionsHelper(ray);
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
         * @param geometry
         * @param point
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
     * find intersections of a ray and a geometry
     * using NVI design pattern
     * @param ray
     * @return the intersections as GeoPoints
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray){
        return findGeoIntersectionsHelper(ray);
    }
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);

}
