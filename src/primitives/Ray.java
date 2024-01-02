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
        return Objects.equals(head, ray.head) && Objects.equals(direction, ray.direction);
    }

    @Override
    public String toString() {
        return "Ray{" +
                "head=" + head +
                ", direction=" + direction +
                '}';
    }
}
