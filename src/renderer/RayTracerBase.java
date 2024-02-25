package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * this class represents an abstract ray tracer
 * @author Elad and Amitay
 */
public abstract class RayTracerBase {
    protected Scene scene;

    /**
     * constructor gets scene and sets it
     * @param scene
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * gets a ray and return the color of the intersection point with the ray
     * @param ray
     * @param amountOfSamples the amount of samples for super-sampling
     * @return the color of the intersection point with the ray
     */
    public abstract Color traceRay(Ray ray, int amountOfSamples);
}
