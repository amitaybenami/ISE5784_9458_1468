package geometries;

public abstract class RadialGeometry implements Geometry {
    /**
     * the radius of the object
     */
    protected double radius;

    /**
     * constructor that gets the radius and creates the radial geometry object
     *
     * @param radius the radius of the object
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
    }
}
