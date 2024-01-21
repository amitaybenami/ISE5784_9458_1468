package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * this class represents a simple ray tracer
 * @author Elad and Amitay
 */
public class SimpleRayTracer extends RayTracerBase{

    /**
     * constructor gets scene and sets it
     * @param scene
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersections = scene.geometries.findIntersections(ray);

        if(intersections == null)
            return scene.background;

        Point closest = ray.findClosestPoint(intersections);
        return calcColor(closest);
    }

    /**
     * return the color of a given point
     * @param point
     * @return the color of the point
     */

    private Color calcColor(Point point){
        return scene.ambientLight.getIntensity();
    }
}
