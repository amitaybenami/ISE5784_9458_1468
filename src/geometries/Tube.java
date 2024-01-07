package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

/**
 * this class represents a tube
 * @author Elad and Amitay
 */
public class Tube extends RadialGeometry {
    protected final Ray axis;

    /**
     * constructor that gets a ray in the direction of the axis and the radius and creates tube
     *
     * @param radius the radius
     * @param axis   a ray in the direction of the axis
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point point) {
        //if vector between point and Ray's head is orthogonal to Ray's axis
        if (Util.isZero(point.subtract(axis.getHead()).dotProduct(axis.getDirection())))
            return point.subtract(axis.getHead()).normalize();
        //t is the distance between head to the parallel point on the axis
        Double t = point.subtract(axis.getHead()).dotProduct(axis.getDirection());
        //O is the parallel point on the axis
        Point O = axis.getHead().add(axis.getDirection().scale(t));
        return point.subtract(O).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
