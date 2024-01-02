package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * this interface is a general interface for every geometric object
 * @author Elad and Amitay
 */
public interface Geometry {
    /**
     * returns the normal vector to the geometry object from a given point
     *
     * @param point on the geometry object
     * @return the normal vector to the geometry object from the given point
     */
    public Vector getNormal(Point point);
}
