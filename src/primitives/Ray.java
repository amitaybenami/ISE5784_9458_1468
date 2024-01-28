package primitives;

import java.util.List;
import java.util.Objects;
import geometries.Intersectable.GeoPoint;

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

    /**
     * gets list of points and returns the point that closest to ray's head
     * @param points list of points
     * @return the point that closest to ray's head
     */
    public Point findClosestPoint(List<Point> points){
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream()
                .map(p -> new GeoPoint(null, p)).toList()
        ).point;
    }

    /**
     * gets list of geoPoints and returns the geoPoint that closest to ray's head
     * @param points
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> points){
        if (points == null || points.isEmpty())
            return null;

        GeoPoint closest = points.get(0);
        for(int i = 1; i<points.size();i+=1)
            if(points.get(i).point.distanceSquared(head)<closest.point.distanceSquared(head))
                closest = points.get(i);

        return closest;
    }

}
