package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.MissingResourceException;

/**
 * represents the camera
 * @author Elad and Amitay
 */
public class Camera implements Cloneable{
    private Point p0;
    private Vector Vto;
    private Vector Vup;
    private Vector Vright;
    private double width = 0.0;
    private double height = 0.0;
    private double distance = 0.0;

    public Point getP0() {
        return p0;
    }

    public Vector getVto() {
        return Vto;
    }

    public Vector getVup() {
        return Vup;
    }

    public Vector getVright() {
        return Vright;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDistance() {
        return distance;
    }
    private Point center;

    private Camera() {
    }

    /**
     * returns the builder of the camera
     * @return the builder
     */
    public static Builder getBuilder(){
        return new Builder();
    }

    /**
     * construct a ray that goes through center of a given pixel
     * @param nX,nY the resolution of the view plane
     * @param j,i the pixel on the view plane
     * @return the ray that goes through the center of the pixel i,j
     */
    public Ray constructRay(int nX, int nY, int j, int i){
        double y = -(i - (nY - 1.0) / 2.0) * (height / nY);
        double x = (j - (nX - 1.0) / 2.0) * (width / nX);
        //Pij - center of pixel i,j
        Point Pij;
        if (!Util.isZero(x) && !Util.isZero(y))
            Pij = center.add(Vright.scale(x).add(Vup.scale(y)));
        else if (!Util.isZero(x))
            Pij = center.add(Vright.scale(x));
        else if (!Util.isZero(y))
            Pij = center.add(Vup.scale(y));
        else
            Pij = center;

        return new Ray(p0,Pij.subtract(p0));
    }

    /**
     * the builder of the camera
     * @author Elad and Amitay
     */
    public static class Builder{
        private final Camera camera = new Camera();
        private Point Pto = null;

        /**
         * sets the location of the camera
         * @param p0 the location
         * @return the updated builder
         */
        public Builder setLocation(Point p0){
            camera.p0 = p0;
            return this;
        }

        /**
         * sets the vectors from the camera (to, up)
         * @param Vto the vector to the view
         * @param Vup the vector up
         * @throws IllegalArgumentException if Vto and Vup aren't orthogonal
         * @return the updated builder
         */
        public Builder setDirection(Vector Vto, Vector Vup){
            if (!Util.isZero(Vto.dotProduct(Vup)))
                throw new IllegalArgumentException("Vto and Vup must be orthogonal");
            camera.Vto = Vto.normalize();
            camera.Vup = Vup.normalize();
            return this;
        }

        /**
         * sets the vectors from the camera towards a given point
         * @param Pto the direction point
         * @param Vup the general up direction (of view plane)
         * @return the updated builder
         */
        public Builder setDirection(Point Pto, Vector Vup ){
            camera.Vup = Vup;
            this.Pto = Pto;
            return this;
        }

        /**
         * set the width and the height of the view plane
         * @param width
         * @param height
         * @return the updated builder
         */
        public Builder setVpSize(double width ,double height){
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * set the distance from the camera to the view plane
         * @param distance
         * @return the updated builder
         */
        public Builder setVpDistance(double distance){
            camera.distance = distance;
            return this;
        }

        /**
         * checks that all of camera's fields are well assigned
         * @throws MissingResourceException if a field is null or zero
         * @throws IllegalCallerException if the vectors aren't orthogonal or aren't normalized
         * sets Vright and center
         * @return a clone of the object with valid values
         */
        public Camera build(){
            //const strings for exceptions throwing
            final String MISSING_RENDERING_ARGUMENT = "Missing rendering argument";
            final String CAMERA = "Camera";
            final String MUST_BE_NORMALIZED = " must be normalized";
            //if a field is null
            if (camera.p0 == null)
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT,CAMERA ,"p0");
            if (Pto == null && camera.Vto == null)
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT,CAMERA ,"Vto");
            if (camera.Vup == null)
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT,CAMERA ,"Vup");
            // check if Vto and Vup are orthogonal
            if (Pto == null && !Util.isZero(camera.Vto.dotProduct(camera.Vup)))
                throw new IllegalArgumentException("Vto and Vup are not orthogonal");
            // check if Vup and Vto are normalized
            if (Pto == null && !Util.isZero(camera.Vto.lengthSquared()-1))
                throw new IllegalArgumentException("Vto" + MUST_BE_NORMALIZED);
            if (!Util.isZero(camera.Vup.lengthSquared()-1))
                throw new IllegalArgumentException("Vup" + MUST_BE_NORMALIZED);
            // setting Vright and center
            if(Pto ==null)
                camera.Vright = camera.Vto.crossProduct(camera.Vup);
            else
                camera.Vto = Pto.subtract(camera.p0);

            camera.center = camera.p0.add(camera.Vto.scale(camera.distance));
            //if a field is zero
            if(Util.isZero(camera.width))
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT,CAMERA,"width");
            if(Util.isZero(camera.height))
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT,CAMERA,"height");
            if(Util.isZero(camera.distance))
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT,CAMERA,"distance");
            // return clone of the object
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
