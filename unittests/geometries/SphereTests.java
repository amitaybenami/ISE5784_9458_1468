package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Util;
import primitives.Vector;
import primitives.Ray;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * unit tests for {@link Sphere}
 *
 * @author Elad and Amitay
 */
class SphereTests {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: usual point
        assertEquals(1, new Sphere(5, new Point(1, 2, 3)).getNormal(new Point(1, 6, 6)).length(), DELTA, "ERROR: the normal vector is not a unit vector");
        assertThrows(IllegalArgumentException.class, () -> new Sphere(5, new Point(1, 2, 3)).getNormal(new Point(1, 6, 6)).crossProduct(new Point(1, 6, 6).subtract(new Point(1, 2, 3))), "ERROR: the normal vector is in the correct direction");
        assertFalse(Util.compareSign(new Sphere(5, new Point(1, 2, 3)).getNormal(new Point(1, 6, 6)).dotProduct(new Point(1, 6, 6).subtract(new Point(1, 2, 3))), -1), "ERROR: the normal vector is opposite to the correct one");
    }

    final Point p100 = new Point(1, 0, 0);
    final Point p200 = new Point(2,0,0);
    final Point pM100 = new Point(-1, 0, 0);
    final Point p000 = new Point(0,0,0);
    final Vector v110 = new Vector(1, 1, 0);
    final Vector v310 = new Vector(3, 1, 0);
    final Vector v100 = new Vector(1,0,0);
    Sphere sphere = new Sphere(1d,p100);

    /**
     * Test method for {@link Intersectable#findIntersections(Ray)}.
     */
    @Test
    public void testFindIntersections() {

        final Vector vM3M10 = new Vector(-3, -1, 0);
        final Vector v110 = new Vector(1, 1, 0);
        final Vector v001 = new Vector(0,0,1);
        final Point p0_500 = new Point(0.5,0,0);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(pM100, v110)), "Ray's line out of sphere");
        // TC02: Ray starts before and crosses the sphere (2 points)
        final List<Point> result1 = sphere.findIntersections(new Ray(pM100, v310));
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0.0651530771650466, 0.355051025721682, 0), new Point(1.53484692283495, 0.844948974278318, 0)), result1, "Ray crosses sphere");
        // TC03: Ray starts inside the sphere (1 point)
        final List<Point> result3 = sphere.findIntersections(new Ray(p0_500,v001));
        assertEquals(1, result3.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0.5,0,0.8660254037844386)), result3, "Ray starts inside the sphere");
        // TC04: Ray starts after the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(pM100, vM3M10)),"Ray starts after the sphere");
        // =============== Boundary Values Tests ==================
        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 points)
        final List<Point> result11 = sphere.findIntersections(new Ray(p000,v110));
        assertEquals(1,result11.size(),"Wrong number of points");
        assertEquals(List.of(new Point(1,1,0)),result11,"Ray starts at sphere and goes inside but not crosses the center");
        // TC12: Ray starts at sphere and goes outside (0 points)
        assertNull((sphere.findIntersections(new Ray(p000,vM3M10))),"Ray starts at sphere and goes outside and Ray's line not crosses the center");
        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        final List<Point> result13 = sphere.findIntersections(new Ray(pM100,v100));
        assertEquals(2,result13.size(),"Wrong number of points");
        assertEquals(List.of(p000,p200),result13,"Ray starts before the sphere and goes through the center");
        // TC14: Ray starts at sphere and goes inside (1 points)
        final List<Point> result14 = sphere.findIntersections(new Ray(p000,v100));
        assertEquals(1,result14.size(),"Wrong number of points");
        assertEquals(List.of(p200),result14,"Ray starts at sphere and goes through the center");
        // TC15: Ray starts inside (1 points)
        final List<Point> result15 = sphere.findIntersections(new Ray(p0_500,v100));
        assertEquals(1,result15.size(),"Wrong number of points");
        assertEquals(List.of(p200),result15,"Ray starts inside and goes through the center");
        // TC16: Ray starts at the center (1 points)
        final List<Point> result16 = sphere.findIntersections(new Ray(p100,v100));
        assertEquals(1,result16.size(),"Wrong number of points");
        assertEquals(List.of(p200),result16,"Ray starts at the center");
        // TC17: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(p200,v100)),"Ray starts at sphere and goes outside and Ray's line goes through the center");
        // TC18: Ray starts after sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(pM100,new Vector(-1,0,0))),"Ray starts after sphere and Ray's line goes through the center");
        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(0,1,0),v100)),"Ray starts before the tangent point");
        // TC20: Ray starts at the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(1,1,0),v100)),"Ray starts at the tangent point");
        // TC21: Ray starts after the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(2,1,0),v100)),"Ray starts after the tangent point");
        // **** Group: Special cases
        // TC22: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        assertNull(sphere.findIntersections(new Ray(pM100,v001)),"Ray's line is outside, ray is orthogonal to ray start to sphere's center line");
    }

    /**
     * Test method for {@link Sphere#findGeoIntersectionsHelper(Ray, double)}
     */
    @Test
    void testFindGeoIntersectionsHelper(){
        // ============ Equivalence Partitions Tests ==============
        // TC01: two points in the distance (2 points)
        final List<Point> result1 = sphere.findIntersections(new Ray(pM100, v310),4);
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0.0651530771650466, 0.355051025721682, 0), new Point(1.53484692283495, 0.844948974278318, 0)), result1, "Ray crosses sphere, 2 points in distance");
        // TC02: one point in distance (1 point)
        final List<Point> result2 = sphere.findIntersections(new Ray(pM100, v310),2);
        assertEquals(1, result2.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0.0651530771650466, 0.355051025721682, 0)), result2, "Ray crosses sphere, 1 point in distance");
        //TC03: no points in distance (0 points)
        assertNull(sphere.findIntersections(new Ray(pM100, v310),1),  "Ray crosses sphere, no points in distance");
        // =============== Boundary Values Tests ==================
        //TC11: ray starts at sphere (1 point)
        final List<Point> result11 = sphere.findIntersections(new Ray(p000,v110),3);
        assertEquals(1,result11.size(),"Wrong number of points");
        assertEquals(List.of(new Point(1,1,0)),result11,"Ray starts at sphere");
        //TC12: point is exactly on distance (1 point)
        final List<Point> result3 = sphere.findIntersections(new Ray(pM100,v100),1);
        assertEquals(1, result3.size(),"Wrong number of points");
        assertEquals(List.of(p000), result3,"point is exactly on distance");
    }
}