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
     * @param scene
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
     * @param gp
     * @return the color of the point
     */

    private Color calcColor(GeoPoint gp, Ray ray){
        return scene.ambientLight.getIntensity().add(calcLocalEffects(gp,ray));
    }

    private Color calcLocalEffects(GeoPoint gp, Ray ray) {
        Vector n = gp.geometry.getNormal(gp.point);
        Vector v = ray.getDirection();
        double nv = alignZero(n.dotProduct(v));
        Color color= gp.geometry.getEmission();
        if (nv == 0) return color;
        Material material = gp.geometry.getMaterial();

        for (LightSource lightSource: scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl= alignZero(n.dotProduct(l));
            if (nl* nv> 0) { // sign(nl) == sign(nv)
                Color iL= lightSource.getIntensity(gp.point);
                color = color.add(
                        iL.scale(calcDiffusive(material, nl)
                                .add(calcSpecular( material,n, l, nl, v))));
            }
        }
        return color;
    }

    private Double3 calcDiffusive(Material material, double nl) {
        double abs = nl > 0 ? nl : -nl;
        return material.kS.scale(abs);
    }

    private Double3 calcSpecular(Material material,Vector n, Vector l, double nl, Vector v) {
        Vector r = l.subtract(n.scale(nl * 2));
        double deflection = alignZero(-(v.dotProduct(r)));
        double max = deflection > 0 ? deflection : 0;
        if (deflection > 0)
            for (int i = 0; i<material.nShininess;i++)
                max *= deflection;
        return material.kS.scale(max);
    }


}
