package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * this interface is a general interface for every geometric object
 * @author Elad and Amitay
 */
public abstract class Geometry extends Intersectable{

    protected Color emission = Color.BLACK;

    private Material material = new Material();

    /**
     * @return the emission color of the geometry
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * setter for emission in builder template
     * @param emission
     * @return the emission
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * returns the normal vector to the geometry object from a given point
     *
     * @param point on the geometry object
     * @return the normal vector to the geometry object from the given point
     */
    abstract public Vector getNormal(Point point);

    public Material getMaterial() {
        return material;
    }

    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}
