package primitives;

import geometries.Intersectable.GeoPoint;

import java.util.List;

/**
 * this class represents a ray in 3D space
 *
 * @author Elad and Amitay
 */
public class Ray {
    /**
     * for moving heads of secondary rays
     */
    private static final double DELTA = 0.1;
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

    /**
     * constructor for secondary rays, the head will be moved a little on the normal vector
     *
     * @param head      the head point
     * @param direction the direction vector
     * @param normal    the normal vector to the object the ray starts from
     */
    public Ray(Point head, Vector direction, Vector normal) {
        this.direction = direction.normalize();
        double nDir = this.direction.dotProduct(normal);
        this.head = Util.isZero(nDir) ? head :
                nDir > 0 ? head.add(normal.scale(DELTA)) : head.add(normal.scale(-DELTA));
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
     *
     * @param t the distance
     * @return the point
     */
    public Point getPoint(double t) {
        if (Util.isZero(t))//point is the head
            return head;

        return head.add(direction.scale(t));
    }

    /**
     * gets list of points and returns the point that closest to ray's head
     *
     * @param points list of points
     * @return the point that closest to ray's head
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream()
                .map(p -> new GeoPoint(null, p)).toList()
        ).point;
    }

    /**
     * gets list of geoPoints and returns the geoPoint that closest to ray's head
     *
     * @param points the list
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> points) {
        if (points == null || points.isEmpty())
            return null;

        GeoPoint closest = points.get(0);
        double minDistance = closest.point.distanceSquared(head);
        double distance = 0;

        for (int i = 1; i < points.size(); i += 1) {
            distance = points.get(i).point.distanceSquared(head);
            if (distance < minDistance) {
                closest = points.get(i);
                minDistance = distance;
            }
        }

        return closest;
    }

}
