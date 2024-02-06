package geometries;

import primitives.Ray;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * this class is used to create a list of geometries
 *
 * @author Elad and Amitay
 */
public class Geometries extends Intersectable {
    //the list itself
    private final List<Intersectable> geometries = new ArrayList<>();

    /**
     * default constructor does nothing
     */
    public Geometries() {
    }

    /**
     * constructor that gets array of geometries and assign it as the list
     *
     * @param geometries an array of geometries
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * gets an array of geometries and ass it to the list
     *
     * @param geometries an array of geometries
     */
    public void add(Intersectable... geometries) {
        this.geometries.addAll(List.of(geometries));
    }

    /**
     * returns the intersections of a ray and geometries up to a maximum distance using composite design pattern
     *
     * @param ray         the ray
     * @param maxDistance the maximum distance
     * @return list of intersections as geoPoints
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        List<GeoPoint> intersections = null;
        for (Intersectable intersectable : geometries) {
            List<GeoPoint> geoIntersections = intersectable.findGeoIntersections(ray, maxDistance);
            if (geoIntersections != null)
                if (intersections == null)
                    intersections = new LinkedList<>(geoIntersections);
                else
                    intersections.addAll(geoIntersections);
        }
        return intersections;
    }

}
