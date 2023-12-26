package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Tube extends RadialGeometry {
    private final Ray axis;

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
        return null;
    }
}
