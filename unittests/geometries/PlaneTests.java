package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Util;
import primitives.Vector;

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
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: usual point
        assertEquals(1,new Plane(new Point(1,2,3),new Point(2,3,8),new Point(-1,2,7)).getNormal(new Point(1,4,17)).length(),DELTA,"ERROR: the normal vector is not a unit vector");
        assertTrue(Util.isZero(new Plane(new Point(1,2,3),new Point(2,3,8),new Point(-1,2,7)).getNormal(new Point(1,4,17)).dotProduct(new Vector(1,1,5))),"ERROR: the normal vector is not orthogonal to one of the vectors on the plane");
        assertTrue(Util.isZero(new Plane(new Point(1,2,3),new Point(2,3,8),new Point(-1,2,7)).getNormal(new Point(1,4,17)).dotProduct(new Vector(-2,0,4))),"ERROR: the normal vector is not orthogonal to one of the vectors on the plane");
    }
}