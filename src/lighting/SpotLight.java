package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class SpotLight extends PointLight{
    private final Vector direction;
    private int narrowBeam = 1;

    @Override
    public SpotLight setKl(double kL) {
        super.setKl(kL);
        return this;
    }

    @Override
    public SpotLight setKq(double kQ) {
        super.setKq(kQ);
        return this;
    }

    @Override
    public SpotLight setKc(double kC) {
        super.setKc(kC);
        return this;
    }

    public SpotLight setNarrowBeam(int narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }

    /**
     * simple constructor
     * @param intensity the intensity
     * @param position the position
     * @param direction the direction vector
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        double deflection =  getL(p).dotProduct(direction);
        double max = deflection > 0 ? deflection : 0;
        if (deflection > 0)
            for(int i = 1; i<narrowBeam;i++)
                max *= deflection;
        return super.getIntensity(p).scale(max);
    }

}
