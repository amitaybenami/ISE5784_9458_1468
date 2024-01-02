package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * unit tests for {@link Point}
 * @author Elad and Amitay
 */
class PointTest {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;


    /** Test method for {@link primitives.Point#Point(double, double, double)}
     * and for {@link Point#Point(Double3)}
     */
    @Test
    void TestConstructor(){
        // ============ Equivalence Partitions Tests ==============
        //TC01: usual point:
        assertDoesNotThrow(() -> new Point(1, 2, 3), "Failed constructing a correct point");
        assertDoesNotThrow(() -> new Point(new Double3(1, 2, 3)),"Failed constructing a correct point");
    }

    /** Test method for {@link primitives.Point#subtract(primitives.Point)}*/
    @Test
    void subtract() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: usual points:
        assertEquals(new Vector(1,2,3),new Point(2,4,6).subtract(new Point(1,2,3)),"ERROR: (point2 - point1) does not work correctly");
        // =============== Boundary Values Tests ==================
        //TC10: same point:
        assertThrows(IllegalArgumentException.class,()->new Point(1,2,3).subtract(new Point(1,2,3)),"ERROR: (point - itself) does not throw an exception");
    }

    /** Test method for {@link primitives.Point#add(primitives.Vector)}*/
    @Test
    void add() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: usual points:
        assertEquals(new Point(2,4,6),new Point(1,2,3).add(new Vector(1,2,3)),"ERROR: (point + vector) = other point does not work correctly");
        // =============== Boundary Values Tests ==================
        //TC10: result = center of coordinates:
        assertEquals(new Point(Double3.ZERO),new Point(1,2,3).add(new Vector(-1,-2,-3)),"ERROR: (point + vector) = center of coordinates does not work correctly");
    }

    /** Test method for {@link primitives.Point#distanceSquared(Point)}*/
    @Test
    void distanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: usual points:
        assertEquals(9,new Point(1,2,3).distanceSquared(new Point(2,4,5)), DELTA,"ERROR: squared distance between points is wrong");
        assertEquals(9,new Point(2,4,5).distanceSquared(new Point(1,2,3)), DELTA,"ERROR: squared distance between points is wrong");
        // =============== Boundary Values Tests ==================
        //TC10: same point:
        assertTrue(Util.isZero(new Point(1,2,3).distanceSquared(new Point(1,2,3))),"ERROR: point squared distance to itself is not zero");
    }

    /** Test method for {@link primitives.Point#distance(Point)}*/
    @Test
    void distance() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: usual points:
        assertEquals(3,new Point(1,2,3).distance(new Point(2,4,5)), DELTA,"ERROR: distance between points is wrong");
        assertEquals(3,new Point(2,4,5).distance(new Point(1,2,3)), DELTA,"ERROR: distance between points is wrong");
        // =============== Boundary Values Tests ==================
        //TC10: same point:
        assertTrue(Util.isZero(new Point(1,2,3).distance(new Point(1,2,3))),"ERROR: point distance to itself is not zero");
    }
}