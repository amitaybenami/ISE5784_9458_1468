package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * this interface is a general interface for every geometric object that will be intersected
 * @author Elad and Amitay
 */
public interface Intersectable {
    /**
     * returns list of the intersection points of the object with a given ray
     * @param ray the given ray
     * @return the list of the intersection points
     */
    List<Point> findIntersections(Ray ray);
}
