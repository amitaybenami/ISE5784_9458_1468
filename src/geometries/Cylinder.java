package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

/**
 * this class will represent a cylinder
 * @author Elad and Amitay
 */
public class Cylinder extends Tube {
    private final double height;

    /**
     * constructor that gets a ray in the direction of the axis and the radius and the height and creates tube
     *
     * @param radius the radius
     * @param axis   a ray in the direction of the axis
     * @param height the height of the tube
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        // point is on the first base
        if (point.equals(axis.getHead())|| Util.isZero(point.subtract(axis.getHead()).dotProduct(axis.getDirection()))){
            return axis.getDirection().scale(-1).normalize();
        }
        //point is on the second base
        if (point.equals(axis.getHead().add(axis.getDirection().scale(height)))|| Util.isZero(point.subtract(axis.getHead().add(axis.getDirection().scale(height))).dotProduct(axis.getDirection()))){
            return axis.getDirection().normalize();
        }
        //point is on the tube
        return super.getNormal(point);
    }
}
