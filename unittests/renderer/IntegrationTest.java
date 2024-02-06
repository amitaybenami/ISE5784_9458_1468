package renderer;

import geometries.Geometry;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * integration tests for renderer
 *
 * @author Elad and Amitay
 */
class IntegrationTest {

    private static int countIntersections(Geometry geometry, Camera camera, int nY, int nX) {
        int intersections = 0;

        for (int j = 0; j < nY; j += 1)
            for (int i = 0; i < nX; i++) {
                List<Point> result = geometry.findIntersections(camera.constructRay(nX, nY, j, i));
                if (result != null)
                    intersections += result.size();
            }
        return intersections;

    }

    Camera camera = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(new Scene("Test")))
            .setImageWriter(new ImageWriter("Test", 1, 1))
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

        intersections = countIntersections(sphere1, camera, 3, 3);

        assertEquals(2, intersections, "sphere intersects through one pixel");

        //TC02: maximum intersections (18 points)
        Sphere sphere2 = new Sphere(2.5, new Point(0, 0, -2.5));
        camera = Camera.getBuilder()
                .setRayTracer(new SimpleRayTracer(new Scene("Test")))
                .setImageWriter(new ImageWriter("Test", 1, 1))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .setLocation(new Point(0, 0, 0.5))
                .build();

        intersections = countIntersections(sphere2, camera, 3, 3);
        assertEquals(18, intersections, "sphere maximum intersections");

        //TC03: partial intersections (10 points)
        Sphere sphere3 = new Sphere(2, new Point(0, 0, -2));
        intersections = countIntersections(sphere3, camera, 3, 3);
        assertEquals(10, intersections, "sphere partial intersections");

        //TC04: view plane is inside the sphere (9 points)
        Sphere sphere4 = new Sphere(4, new Point(0, 0, -1));
        camera = Camera.getBuilder()
                .setRayTracer(new SimpleRayTracer(new Scene("Test")))
                .setImageWriter(new ImageWriter("Test", 1, 1))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .setLocation(Point.ZERO)
                .build();
        intersections = countIntersections(sphere4, camera, 3, 3);
        assertEquals(9, intersections, "view plane is inside the sphere");

        //TC05: sphere behind the camera (0 points)
        Sphere sphere5 = new Sphere(0.5, new Point(0, 0, 1));
        intersections = countIntersections(sphere5, camera, 3, 3);
        assertEquals(0, intersections, "sphere behind the camera");
    }

    /**
     * Test method for camera rays intersections with plane integration
     */
    @Test
    void testPlaneIntegration() {
        //TC01: plane is parallel to the view plane (9 points)
        Plane plane1 = new Plane(new Point(0, 0, -2), new Vector(0, 0, 1));
        intersections = countIntersections(plane1, camera, 3, 3);
        assertEquals(9, intersections, "plane is parallel to the view plane");

        //TC02: plane maximum intersections (not parallel) (9 points)
        Plane plane2 = new Plane(new Point(0, 0, -2), new Vector(0, -1, 3));
        intersections = countIntersections(plane2, camera, 3, 3);
        assertEquals(9, intersections, "plane maximum intersections");

        //TC03:plane partial intersection - rays parallel to plane (6 points)
        Plane plane3 = new Plane(new Point(0, 0, -1.5), new Vector(0, 1, -1));
        intersections = countIntersections(plane3, camera, 3, 3);
        assertEquals(6, intersections, "plane partial intersection - rays parallel to plane");
    }

    /**
     * Test method for camera rays intersections with triangle integration
     */
    @Test
    void testTriangleIntegration() {
        //TC01: one intersection (1 point)
        Triangle triangle1 = new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2));
        intersections = countIntersections(triangle1, camera, 3, 3);
        assertEquals(1, intersections, "one intersection");

        //TC02: two intersections (2 points)
        Triangle triangle2 = new Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2));
        intersections = countIntersections(triangle2, camera, 3, 3);
        assertEquals(2, intersections, "two intersections");
    }
}
