package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * unit tests for {@link Geometries}
 *
 * @author Elad and Amitay
 */
class GeometriesTests {

    Plane plane = new Plane(new Point(1, -5, 0), new Vector(25, -27.5, -20));
    Sphere sphere = new Sphere(1d, new Point(4, 1, 0));
    Triangle triangle = new Triangle(new Point(1, 2, -1), new Point(1, 0, 0), new Point(0.5, 1, 2));
    Geometries geometries = new Geometries(plane, sphere, triangle);

    /**
     * Test method for {@link Intersectable#findIntersections(Ray)}
     */
    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: Some of the geometries intersected
        assertEquals(3, geometries.findIntersections(new Ray(new Point(6, 1.5, 0), new Vector(-1, 0, 0))).size(), "Some of the geometries intersected");
        // =============== Boundary Values Tests ==================
        //TC11: Empty geometries list
        assertNull(new Geometries().findIntersections(new Ray(new Point(1, 0, 0), new Vector(0, 1, 0))), "Empty geometries list");
        //TC12: No geometry intersected
        assertNull(geometries.findIntersections(new Ray(new Point(0, 3, 0), new Vector(-4, -3, 0))), "No geometry intersected");
        //TC13: One geometry intersected
        assertEquals(1, geometries.findIntersections(new Ray(new Point(2, 0, 0), new Vector(0, -6, 0))).size(), "One geometry intersected");
        //TC14: All the geometries intersected
        assertEquals(4, geometries.findIntersections(new Ray(new Point(0, 1.5, 0), new Vector(1, 0, 0))).size(), "All the geometries intersected");
    }

    /**
     * Test method for {@link Geometries#findGeoIntersectionsHelper(Ray, double)}
     */
    @Test
    void testFindGeoIntersectionsHelper() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: Some of the intersections are inside the distance (2 points)
        assertEquals(2, geometries.findIntersections(new Ray(new Point(6, 1.5, 0), new Vector(-1, 0, 0)),4).size(), "Some of the intersections are inside the distance");
        //TC02: all the intersections are inside the distance (3 points)
        assertEquals(3, geometries.findIntersections(new Ray(new Point(6, 1.5, 0), new Vector(-1, 0, 0)),6).size(), "all the intersections are inside the distance");
        //TC03: no intersections inside the distance (0 points)
        assertNull(geometries.findIntersections(new Ray(new Point(6, 1.5, 0), new Vector(-1, 0, 0)),1), "no intersections inside the distance");
        // =============== Boundary Values Tests ==================
        //TC11: an intersection is on distance (3 points)
        assertEquals(3, geometries.findIntersections(new Ray(new Point(6, 1.5, 0), new Vector(-1, 0, 0)),5.150000000000001).size(), "an intersection is on distance");
    }
}