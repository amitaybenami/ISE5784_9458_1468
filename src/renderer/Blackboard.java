package renderer;

import primitives.Point;
import primitives.Util;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * represents the target area of super-sampling ray tracing
 */
public class Blackboard {
    /**
     * the center of the blackboard
     */
    private Point center;

    /**
     * the up direction of the blackboard
     */
    private Vector Vup;

    /**
     * the right direction of the blackboard
     */
    private Vector Vright;

    /**
     * size of the blackboard (both length and width)
     */
    private double size;

    /**
     * boolean variable determining if the blackboard shape is circle (otherwise its square)
     */
    private boolean circle = false;

    /**
     * simple constructor
     * @param center - the center of the blackBoard
     * @param Vup,Vright - define the plane which the blackBoard is on
     * @param size - the length of the edge of the blackBoard
     */
    public Blackboard(Point center, Vector Vup, Vector Vright, double size) {
        this.center = center;
        this.Vup = Vup;
        this.Vright = Vright;
        this.size = size;
    }

    /**
     * returns the points on the blackBoard jittered way
     * @param amountOfSamples the amount of samples
     * @return the points
     */
    private List<Point> getPointsSquare(int amountOfSamples) {
        double subPixelSize = size / amountOfSamples;
        double y, x;
        Point Pij;
        List<Point> list = new ArrayList<>();
        for (int i = 0; i < amountOfSamples; i++)
            for (int j = 0; j < amountOfSamples; j++) {
                y = (-(i - (amountOfSamples - 1.0) / 2.0) * subPixelSize) + (Math.random() - 0.5) * subPixelSize;
                x = ((j - (amountOfSamples - 1.0) / 2.0) * subPixelSize) + (Math.random() - 0.5) * subPixelSize;
                //Pij - random point in the sub-pixel i,j
                if (!Util.isZero(x) && !Util.isZero(y))
                    Pij = center.add(Vright.scale(x).add(Vup.scale(y)));
                else if (!Util.isZero(x))
                    Pij = center.add(Vright.scale(x));
                else if (!Util.isZero(y))
                    Pij = center.add(Vup.scale(y));
                else
                    Pij = center;

                list.add(Pij);
            }
        return list;
    }


    public List<Point> getPoints(int amountOfSamples) {
        if (!circle)
            return getPointsSquare(amountOfSamples);

        List<Point> listSquare = getPointsSquare((int)(amountOfSamples * 1.27324));

        List<Point> list  = new ArrayList<>();
        double radiusSquared = (size/2) * (size/2);
        for (Point point : listSquare)
            if(point.distanceSquared(center) < radiusSquared)
                list.add(point);

        return list;
    }

    public Blackboard setCircle(boolean circle) {
        this.circle = circle;
        return this;
    }
}