package lighting;
import primitives.Color;
import primitives.Point;
import primitives.Vector;


/**
 * represents a light source
 * @author Elad and Amitay
 */
public interface LightSource {
    /**
     * gets a point and returns the intensity of this light source on the point
     * @param p the point
     * @return the intensity of this light source on the point
     */
    public Color getIntensity(Point p);

    /**
     * gets a point and returns the direction vector from this light source to the point
     * @param p the point
     * @return the direction vector from this light source to the point
     */
    public Vector getL(Point p);
}
