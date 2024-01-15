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

    private Camera() {
    }

    /**
     * returns the builder of the camera
     * @return the builder
     */
    public Builder getBuilder(){
        return new Builder();
    }

    /**
     * construct a ray that goes through center of a given pixel
     * @param nX,nY the resolution of the view plane
     * @param j,i the pixel on the view plane
     * @return the ray that goes through the center of the pixel i,j
     */
    public Ray constructRay(int nX, int nY, int j, int i){
        return null;
    }

    /**
     * the builder of the camera
     * @author Elad and Amitay
     */
    public static class Builder{
        private final Camera camera = new Camera();

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
         * sets the vectors from the camera (to, up, right)
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
            camera.Vright = camera.Vto.crossProduct(camera.Vup);
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

        public Camera build(){
            final String MISSING_RENDERING_ARGUMENT = "Missing rendering argument";
            final String CAMERA = "Camera";
            final String MUST_BE_NORMALIZED = " must be normalized";
            if (camera.p0 == null)
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT,CAMERA ,"p0");
            if (camera.Vto == null)
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT,CAMERA ,"Vto");
            if (camera.Vup == null)
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT,CAMERA ,"Vup");
            if (camera.Vright == null)
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT,CAMERA ,"Vright");
            if (!Util.isZero(camera.Vto.dotProduct(camera.Vup)))
                throw new IllegalArgumentException("Vto and Vup are not orthogonal");
            if (!Util.isZero(camera.Vto.dotProduct(camera.Vright)))
                throw new IllegalArgumentException("Vto and Vright are not orthogonal");
            if (!Util.isZero(camera.Vright.dotProduct(camera.Vup)))
                throw new IllegalArgumentException("Vright and Vup are not orthogonal");
            if (Util.isZero(camera.Vto.lengthSquared()-1))
                throw new IllegalArgumentException("Vto" + MUST_BE_NORMALIZED);
            if (Util.isZero(camera.Vup.lengthSquared()-1))
                throw new IllegalArgumentException("Vup" + MUST_BE_NORMALIZED);
            if (Util.isZero(camera.Vright.lengthSquared()-1))
                throw new IllegalArgumentException("Vright" + MUST_BE_NORMALIZED);
            if(Util.isZero(camera.width))
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT,CAMERA,"width");
            if(Util.isZero(camera.height))
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT,CAMERA,"height");
            if(Util.isZero(camera.distance))
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT,CAMERA,"distance");

            return (Camera) camera.clone();
        }

    }
}
