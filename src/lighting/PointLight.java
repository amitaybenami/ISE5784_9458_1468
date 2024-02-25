package lighting;
import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * represents a pointed light source
 * @author Elad and Amitay
 */

public class PointLight extends Light implements LightSource{
    protected Point position;
    private double kC = 1.0;
    private double kL = 0.0;
    private double kQ = 0.0;

    /**
     * radius of the light source for soft shadows
     */
    double radius = 0;

    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     *
     * @param radius define the area for the  shadows
     * @return updated point light
     */
    public PointLight setRadius(double radius){
        this.radius = radius;
        return this;
    }
    /**
     * simple constructor
     * @param intensity
     * @param position
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity.scale(1/(kC + kL * getDistance(p) + kQ * p.distanceSquared(position)));
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }

    @Override
    public double getDistance(Point point) {
        return point.distance(position);
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public Point getPosition() {
        return position;
    }
}
