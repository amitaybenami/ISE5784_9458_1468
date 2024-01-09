package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * unit tests for {@link Plane}
 * @author Elad and Amitay
 */
class PlaneTests {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;

    /**Test method for {@link Plane#Plane(Point, Point, Point)}*/
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: usual points
        assertDoesNotThrow(()->new Plane(new Point(1,2,3),new Point(2,3,8),new Point(-1,2,7)),"ERROR: Failed constructing a correct plane");
        // =============== Boundary Values Tests ==================
        //TC10: same two first points
        assertThrows(IllegalArgumentException.class,()->new Plane(new Point(1,2,3),new Point(1,2,3),new Point(-1,2,7)),"ERROR: constructing a plane with coalesces points doesn't throw an exception");
        //TC11: all three points on the same line
        assertThrows(IllegalArgumentException.class,()->new Plane(new Point(1,2,3),new Point(-1,-2,-3),new Point(2,4,6)),"ERROR: constructing a plane with all three points on the same line doesn't throw an exception");
    }

    /**Test method for {@link geometries.Plane#getNormal(primitives.Point)}*/
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: usual point
        assertEquals(1,new Plane(new Point(1,2,3),new Point(2,3,8),new Point(-1,2,7)).getNormal(new Point(1,4,17)).length(),DELTA,"ERROR: the normal vector is not a unit vector");
        assertTrue(Util.isZero(new Plane(new Point(1,2,3),new Point(2,3,8),new Point(-1,2,7)).getNormal(new Point(1,4,17)).dotProduct(new Vector(1,1,5))),"ERROR: the normal vector is not orthogonal to one of the vectors on the plane");
        assertTrue(Util.isZero(new Plane(new Point(1,2,3),new Point(2,3,8),new Point(-1,2,7)).getNormal(new Point(1,4,17)).dotProduct(new Vector(-2,0,4))),"ERROR: the normal vector is not orthogonal to one of the vectors on the plane");
    }

    /**Test method for {@link geometries.Plane#findIntersections(Ray)}*/
    @Test
    public void testFindIntersections() {
        final Point p200 = new Point(2,0,0);
        final Point p100 = new Point(1,0,0);
        final Vector v1M10 = new Vector(1,-1,0);
        final Vector v111 = new Vector(1,1,1);
        final Point p001 = new Point(0,0,1);
        Plane plane = new Plane (p001,v111);
        // ============ Equivalence Partitions Tests ==============
        //TC0: Ray intersects with the plane (1 point)
        final List<Point> result0 = plane.findIntersections(new Ray(p200,new Vector(-1,0,0)));
        assertEquals(1,result0.size(),"Wrong number of points");
        assertEquals(result0,List.of(p100),"Ray intersects with the plane");
        //TC1: Ray does not intersect with the plane (0 points)
        assertNull(plane.findIntersections(new Ray(p200,new Vector(1,0,0))),"Ray does not intersect with the plane");

        // =============== Boundary Values Tests ==================
        // **** Group: Ray is parallel to the plane
        //TC11: Ray included in the plane
        assertNull(plane.findIntersections(new Ray(p100,v1M10)),"Ray included in the plane");
        //TC12: Ray does not included in the plane
        assertNull(plane.findIntersections(new Ray(p200,v1M10)),"Ray is parallel to the plane and not included in it");
        // **** Group: Ray is orthogonal to the plane
        //TC13: Ray starts before the plane
        final List<Point> result13 = plane.findIntersections(new Ray(new Point(-1,0,0),v111));
        assertEquals(1,result13.size(),"Wrong number of points");
        assertEquals(List.of(new Point(-0.3333333,0.66666667,0.666666667)),result13,"Ray is orthogonal to the plane and starts before it");
        //TC14: Ray starts in the plane
        assertNull(plane.findIntersections(new Ray(p100,v111)),"Ray is orthogonal to the plane and starts in it");
        //TC15: Ray starts after the plane
        assertNull(plane.findIntersections(new Ray(new Point(3,3,3),v111)),"Ray is orthogonal to the plane and starts after it");
        // **** Group: special cases
        //TC16: Ray starts in the plane and neither parallel nor orthogonal to it
        assertNull(plane.findIntersections(new Ray(p100,new Vector(1,2,0))),"Ray starts in the plane and neither parallel nor orthogonal to it");
        //TC17: Ray starts in the plane on the given point of it and neither parallel nor orthogonal to the plane
        assertNull(plane.findIntersections(new Ray(p001,new Vector(1,2,0))),"Ray starts in the plane on the given point of it and neither parallel nor orthogonal to the plane");
    }
}