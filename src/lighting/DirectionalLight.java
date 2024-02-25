package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * represents a light source with a direction
 * @author Elad and Amitay
 */
public class DirectionalLight extends Light implements LightSource{
    private Vector direction;

    /**
     * simple constructor
     * @param intensity
     * @param direction
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }

    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double getRadius() {
        return 0;
    }

    @Override
    public Point getPosition() {
        return null;
    }
}
