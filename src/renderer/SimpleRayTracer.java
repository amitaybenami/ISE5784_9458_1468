package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * this class represents a simple ray tracer
 *
 * @author Elad and Amitay
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * max recursive deepness level
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    /**
     * minimal attenuation factor
     */
    private static final double MIN_CALC_COLOR_K = 0.001;
    /**
     * initial attenuation factor
     */
    private static final Double3 INITIAL_K = Double3.ONE;
    /**
     * constructor gets scene and sets it
     *
     * @param scene the scene
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null)
            return scene.background;

        return calcColor(ray.findClosestGeoPoint(intersections), ray);
    }

    /**
     * return the color of a given point recursively
     *
     * @param gp  the point
     * @param ray the ray being traced
     * @param level the recursive level
     * @param k the attenuation factor
     * @return the color of the point
     */

    private Color calcColor(GeoPoint gp, Ray ray, int level, Double3 k){
        Color color = calcLocalEffects(gp, ray, k);
        return 1 == level ? color
                : color.add(calcGlobalEffects(gp, ray, level, k));
    }

    /**
     * calculate the global effects (transparency and reflection) of light on a point
     * @param gp the point
     * @param ray the ray that intersects the point
     * @param level the recursive level
     * @param k the attenuation factor
     * @return the Color of the global effects on the point
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray,int level, Double3 k) {
        Vector n = gp.geometry.getNormal(gp.point);
        Material material= gp.geometry.getMaterial();
        return calcColorGlobalEffect(constructRefractedRay(gp, ray, n), material.kT,level, k)
                .add(calcColorGlobalEffect(constructReflectedRay(gp, ray, n), material.kR,level, k));
    }
    /**
     * construct reflection ray from light intersection with a point
     * @param gp the point
     * @param ray the ray intersected the point
     * @param n the normal to the geometry from the point
     * @return reflection ray
     */
        private Ray constructReflectedRay(GeoPoint gp, Ray ray,Vector n) {
            Vector l = ray.getDirection();
            double nl = alignZero(n.dotProduct(l));
            //r = reflection vector
            Vector r = l.subtract(n.scale(nl * 2));

            return new Ray(gp.point,r,n);
        }

    /**
     * construct refraction(transparency) ray from light intersection with a point
     * @param gp the point
     * @param ray the ray intersected the point
     * @param n the normal to the geometry from the point
     * @return refraction ray
     */
    private Ray constructRefractedRay(GeoPoint gp, Ray ray, Vector n) {
        return new Ray(gp.point,ray.getDirection(),n);
    }

    /**
     * calculate the global effects (transparency and reflection) of light on a point
     * @param ray the ray that intersects the point
     * @param kx the attenuation factor of material
     * @param level the recursive level
     * @param k the reduced attenuation factor
     * @return the Color of the global effects on the point
     */
    private Color calcColorGlobalEffect(Ray ray, Double3 kx,int level, Double3 k) {
        Double3 kkx= kx.product(k);
        if (kkx.lowerThan(MIN_CALC_COLOR_K) )return Color.BLACK;
        GeoPoint gp= findClosestIntersection(ray);
        return (gp == null ? scene.background: calcColor(gp, ray,level - 1, kkx))
                .scale(kx);
    }

    /**
     * finds the closest intersection to ray's head with ray
     * @param ray the ray
     * @return the intersection or null of there are no intersections
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        return ray.findClosestGeoPoint(scene.geometries.findGeoIntersections(ray));
    }

    /**
     * return the color of a given point
     *
     * @param gp  the point
     * @param ray the ray being traced
     * @return the color of the point
     */
    private Color calcColor(GeoPoint gp, Ray ray){
        return calcColor(gp,ray,MAX_CALC_COLOR_LEVEL,INITIAL_K)
                .add(scene.ambientLight.getIntensity());
    }


    /**
     * calculate the local effects of light on a point
     *
     * @param gp  the point
     * @param ray the ray being traced
     * @param k the attenuation factor
     * @return the color of the point taking only local effects into account
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
        //n = normal
        Vector n = gp.geometry.getNormal(gp.point);
        Vector v = ray.getDirection();
        double nv = alignZero(n.dotProduct(v));
        Color color = gp.geometry.getEmission();
        //if nv = 0 the ray is parallel to the object
        if (nv == 0) return color;
        Material material = gp.geometry.getMaterial();
        //calculate the effect for every light source in the scene
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl = alignZero(n.dotProduct(l));

            //if sign(nl) != sign(nv) the light is behind the object
            if (nl * nv > 0)  { // sign(nl) == sign(nv)
                Double3 ktr = transparency(gp,lightSource,l,n);
                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color iL = lightSource.getIntensity(gp.point).scale(ktr);
                    color = color.add(
                            iL.scale(calcDiffusive(material, nl)
                                    .add(calcSpecular(material, n, l, nl, v))));
                }
            }
        }
        return color;
    }


    /**
     * calculate the diffusive component of the reflection
     *
     * @param material for the diffusive factor of the material
     * @param nl       cosine of angle between normal and vector from light to object
     * @return the diffusive component of the reflection
     */
    private Double3 calcDiffusive(Material material, double nl) {
        // abs = |nl|
        double abs = nl > 0 ? nl : -nl;
        return material.kD.scale(abs);
    }

    /**
     * calculate the specular component of the reflection
     *
     * @param material for the shininess and specular factor of the material
     * @param n        the normal vector
     * @param l        the direction vector from light to object
     * @param nl       cosine of angle between normal and vector from light to object
     * @param v        the direction of the ray from the camera
     * @return the specular component of the reflection
     */
    private Double3 calcSpecular(Material material, Vector n, Vector l, double nl, Vector v) {
        //r = reflection vector
        Vector r = l.subtract(n.scale(nl * 2));
        double deflection = alignZero(-(v.dotProduct(r)));

        //if deflection < 0  - more than 90 degrees there will be not specular component
        double max = deflection > 0 ? deflection : 0;

        if (deflection > 0)
            //deflection ^ nShininess
            for (int i = 0; i < material.nShininess; i++)
                max *= deflection;

        return material.kS.scale(max);
    }

    /**
     * checks if a point is shaded from a light source
     *
     * @param gp          the point
     * @param l           direction vector from light source to point
     * @param n           normal vector of geometry from point
     * @param lightSource the light source
     * @return true if unshaded, false if shaded
     */
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, LightSource lightSource) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray ray = new Ray(gp.point, lightDirection, n);
        double distance = lightSource.getDistance(gp.point);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray, distance);
        if (intersections == null)
            return true;
        for (GeoPoint intersection : intersections)
            if (Double3.ZERO.equals(intersection.geometry.getMaterial().kT))
                return false;
        return true;
    }

    /**
     * calculates the transparency of a point getting light from a light source
     * @param gp the point
     * @param lightSource the light source
     * @param l direction vector from light source to point
     * @param n normal vector of geometry from point
     * @return the transparency of the point as Double3 (rgb)
     */
    private Double3 transparency(GeoPoint gp, LightSource lightSource, Vector l, Vector n){
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray ray = new Ray(gp.point, lightDirection, n);
        double lightSourceDistance = lightSource.getDistance(gp.point);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray, lightSourceDistance);

        Double3 ktr= Double3.ONE;
        if(intersections != null)
            for (GeoPoint intersection : intersections)
                ktr = ktr.product(intersection.geometry.getMaterial().kT);

        return ktr;
    }

}
