package renderer;
import geometries.Geometry;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * integration tests for renderer
 * @author Elad and Amitay
 */
class IntegrationTest {

    private static int getIntersections(Geometry geometry, Camera camera) {
        int intersections = 0;
        for (int j = 0; j < 3; j += 1)
            for (int i = 0; i < 3; i++) {
                List<Point> result = geometry.findIntersections(camera.constructRay(3, 3, j, i));
                if (result != null)
                    intersections += result.size();
            }
        return intersections;

    }

    Camera camera = Camera.getBuilder()
            .setLocation(Point.ZERO)
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVpSize(3, 3)
            .setVpDistance(1)
            .build();

    int intersections;

    /**
     * Test method for camera rays intersections with sphere integration
     */
    @Test
    void testSphereIntegration() {
        //TC01: intersects through one pixel (2 points)


        Sphere sphere1 = new Sphere(1, new Point(0, 0, -3));

        intersections = getIntersections(sphere1, camera);

        assertEquals(2, intersections, "sphere intersects through one pixel");

        //TC02: maximum intersections (18 points)
        Sphere sphere2 = new Sphere(2.5, new Point(0, 0, -2.5));
        camera = Camera.getBuilder()
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .setLocation(new Point(0, 0, 0.5))
                .build();

        intersections = getIntersections(sphere2, camera);
        assertEquals(18, intersections, "sphere maximum intersections");

        //TC03: partial intersections (10 points)
        Sphere sphere3 = new Sphere(2, new Point(0, 0, -2));
        intersections = getIntersections(sphere3, camera);
        assertEquals(10, intersections, "sphere partial intersections");

        //TC04: view plane is inside the sphere (9 points)
        Sphere sphere4 = new Sphere(4, new Point(0, 0, -1));
        camera = Camera.getBuilder()
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .setLocation(Point.ZERO)
                .build();
        intersections = getIntersections(sphere4, camera);
        assertEquals(9, intersections, "view plane is inside the sphere");

        //TC05: sphere behind the camera (0 points)
        Sphere sphere5 = new Sphere(0.5, new Point(0, 0, 1));
        intersections = getIntersections(sphere5, camera);
        assertEquals(0, intersections, "sphere behind the camera");
    }

    /**
     * Test method for camera rays intersections with plane integration
     */
    @Test
    void testPlaneIntegration() {
        //TC01: plane is parallel to the view plane (9 points)
        Plane plane1 = new Plane(new Point(0, 0, -2), new Vector(0, 0, 1));
        intersections = getIntersections(plane1, camera);
        assertEquals(9, intersections, "plane is parallel to the view plane");

        //TC02: plane maximum intersections (not parallel) (9 points)
        Plane plane2 = new Plane(new Point(0, 0, -2), new Vector(0, -1, 3));
        intersections = getIntersections(plane2, camera);
        assertEquals(9, intersections, "plane maximum intersections");

        //TC03:plane partial intersection - rays parallel to plane (6 points)
        Plane plane3 = new Plane(new Point(0, 0, -1.5), new Vector(0, 1, -1));
        intersections = getIntersections(plane3, camera);
        assertEquals(6, intersections, "plane partial intersection - rays parallel to plane");
    }

    /**
     * Test method for camera rays intersections with triangle integration
     */
    @Test
    void testTriangleIntegration() {
        //TC01: one intersection (1 point)
        Triangle triangle1 = new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2));
        intersections = getIntersections(triangle1, camera);
        assertEquals(1, intersections, "one intersection");

        //TC02: two intersections (2 points)
        Triangle triangle2 = new Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2));
        intersections = getIntersections(triangle2, camera);
        assertEquals(2, intersections, "two intersections");
    }
}
