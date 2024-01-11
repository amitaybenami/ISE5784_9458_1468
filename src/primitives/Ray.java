package primitives;

import java.util.Objects;

/**
 * this class represents a ray in 3D space
 * @author Elad and Amitay
 */
public class Ray {
    private final Point head;
    private final Vector direction;

    public Vector getDirection() {
        return direction;
    }

    public Point getHead() {
        return head;
    }

    /**
     * this constructor gets point and vector and creates the matching ray
     *
     * @param head      the head of the ray
     * @param direction the direction of the ray
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ray ray)) return false;
        return head.equals(ray.head) && direction.equals(ray.direction);
    }

    @Override
    public String toString() {
        return "Ray{" +
                "head=" + head +
                ", direction=" + direction +
                '}';
    }

    /**
     * calculates a point on the ray's line at a given distance from the head
     * @param t the distance
     * @return the point
     */
    public Point getPoint(double t){
        if (Util.isZero(t))//point is the head
            return head;

        return head.add(direction.scale(t));
    }
}
