package geometries;

import primitives.Ray;

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

}
