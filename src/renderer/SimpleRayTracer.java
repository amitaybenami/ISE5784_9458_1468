package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;
import java.util.List;

import static primitives.Util.alignZero;

/**
 * this class represents a simple ray tracer
 * @author Elad and Amitay
 */
public class SimpleRayTracer extends RayTracerBase{

    /**
     * constructor gets scene and sets it
     * @param scene the scene
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if(intersections == null)
            return scene.background;

        return calcColor(ray.findClosestGeoPoint(intersections), ray);
    }

    /**
     * return the color of a given point
     * @param gp the point
     * @param ray the ray being traced
     * @return the color of the point
     */

    private Color calcColor(GeoPoint gp, Ray ray){
        return scene.ambientLight.getIntensity().add(calcLocalEffects(gp,ray));
    }

    /**
     * calculate the local effects of light on a point
     * @param gp the point
     * @param ray the ray being traced
     * @return the color of the point taking only local effects into account
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray) {
        //n = normal
        Vector n = gp.geometry.getNormal(gp.point);
        Vector v = ray.getDirection();
        double nv = alignZero(n.dotProduct(v));
        Color color= gp.geometry.getEmission();
        //if nv = 0 the ray is parallel to the object
        if (nv == 0) return color;
        Material material = gp.geometry.getMaterial();

        //calculate the effect for every light source in the scene
        for (LightSource lightSource: scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl= alignZero(n.dotProduct(l));

            //if sign(nl) != sign(nv) the light is behind the object
            if (nl* nv> 0) { // sign(nl) == sign(nv)
                Color iL= lightSource.getIntensity(gp.point);
                color = color.add(
                        iL.scale(calcDiffusive(material, nl)
                                .add(calcSpecular( material,n, l, nl, v))));
            }
        }
        return color;
    }

    /**
     * calculate the diffusive component of the reflection
     * @param material for the diffusive factor of the material
     * @param nl cosine of angle between normal and vector from light to object
     * @return the diffusive component of the reflection
     */
    private Double3 calcDiffusive(Material material, double nl) {
        // abs = |nl|
        double abs = nl > 0 ? nl : -nl;
        return material.kS.scale(abs);
    }

    /**
     * calculate the specular component of the reflection
     * @param material for the shininess and specular factor of the material
     * @param n the normal vector
     * @param l the direction vector from light to object
     * @param nl cosine of angle between normal and vector from light to object
     * @param v the direction of the ray from the camera
     * @return the specular component of the reflection
     */
    private Double3 calcSpecular(Material material,Vector n, Vector l, double nl, Vector v) {
        //r = reflection vector
        Vector r = l.subtract(n.scale(nl * 2));
        double deflection = alignZero(-(v.dotProduct(r)));

        //if deflection < 0  - more than 90 degrees there will be not specular component
        double max = deflection > 0 ? deflection : 0;

        if (deflection > 0)
            //deflection ^ nShininess
            for (int i = 0; i<material.nShininess;i++)
                max *= deflection;

        return material.kS.scale(max);
    }


}
