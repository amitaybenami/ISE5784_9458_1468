package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * unit tests for {@link Vector}
 * @author Elad and Amitay
 */
public class VectorTests {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;

    /**
     * Test method for {@link Vector#Vector(double, double, double)}
     * and for {@link Vector#Vector(Double3)}
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: usual vectors
        assertDoesNotThrow(() -> new Vector(1, 2, 3), "Failed constructing a correct vector");
        assertDoesNotThrow(() -> new Vector(new Double3(1, 2, 3)),"Failed constructing a correct vector");
        // =============== Boundary Values Tests ==================
        // TC01: the zero vector
        assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0), "Constructed zero vector");
        assertThrows(IllegalArgumentException.class, () -> new Vector(Double3.ZERO), "Constructed zero vector");
    }

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: usual vectors:
        assertEquals(new Vector(-1, -2, -3), new Vector(1, 2, 3).add(new Vector(-2, -4, -6)), "ERROR: Vector + Vector does not work correctly");
        // =============== Boundary Values Tests ==================
        //TC10: opposite vectors
        assertThrows(IllegalArgumentException.class, () -> new Vector(1, 2, 3).add(new Vector(-1, -2, -3)), "ERROR: Vector + -itself does not throw an exception");
    }

    /**
     * Test method for {@link Vector#subtract(Point)}
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: usual vectors:
        assertEquals(new Vector(3, 6, 9), new Vector(1, 2, 3).subtract(new Vector(-2, -4, -6)), "ERROR: Vector - Vector does not work correctly");
        // =============== Boundary Values Tests ==================
        //TC10: same vector
        assertThrows(IllegalArgumentException.class, () -> new Vector(1, 2, 3).subtract(new Vector(1, 2, 3)), "ERROR: Vector - itself does not throw an exception");
    }


    /**
     * Test method for {@link primitives.Vector#scale(double)}
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: usual vector
        assertEquals(new Vector(2,4,6),new Vector(1,2,3).scale(2),"ERROR: scale() wrong value");
        // =============== Boundary Values Tests ==================
        //TC10: scaling with zero
        assertThrows(IllegalArgumentException.class,()->new Vector(1,2,3).scale(0),"ERROR: Scaling vector with zero doesn't throw exception");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(Vector)}
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: usual vectors:
        assertEquals(-28, new Vector(1, 2, 3).dotProduct(new Vector(-2, -4, -6)), DELTA,"ERROR: dotProduct() wrong value");
        // =============== Boundary Values Tests ==================
        //TC10: orthogonal vectors
        assertTrue(Util.isZero(new Vector(1,2,3).dotProduct(new Vector(0,3,-2))) ,"ERROR: dotProduct() for orthogonal vectors is not zero");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(Vector)}
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: usual vectors:
        Vector vr = new Vector(1,2,3).crossProduct(new Vector(0,3,-2));
        assertEquals(new Vector(1,2,3).length()*new Vector(0,3,-2).length(),vr.length(),DELTA,"ERROR: crossProduct() wrong result length");
        assertTrue(Util.isZero(vr.dotProduct(new Vector(1,2,3))),"ERROR: crossProduct() result is not orthogonal to its 1st operand");
        assertTrue(Util.isZero(vr.dotProduct(new Vector(0,3,-2))),"ERROR: crossProduct() result is not orthogonal to its 2nd operand");
        // =============== Boundary Values Tests ==================
        //TC10: parallel vectors
        assertThrows(IllegalArgumentException.class,()->new Vector(1,2,3).crossProduct(new Vector(-2,-4,-6)),"ERROR: crossProduct() for parallel vectors does not throw an exception");

    }


    /**
     * Test method for {@link Vector#lengthSquared()}
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: usual vectors
        assertEquals(9.0, new Vector(1, 2, 2).lengthSquared(), DELTA, "ERROR: lengthSquared() wrong value");
    }

    /**
     * Test method for {@link Vector#length()}
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: usual vectors
        assertEquals(3, new Vector(1, 2, 2).length(), DELTA, "ERROR: length() wrong value");
    }

    /**
     * Test method for {@link Vector#normalize()}
     */
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: usual vector
        assertEquals(1,new Vector(1,2,3).normalize().length(),DELTA,"ERROR: the normalized vector is not a unit vector");
        assertThrows(IllegalArgumentException.class,()->new Vector(1,2,3).crossProduct(new Vector(1,2,3).normalize()),"ERROR: the normalized vector is not parallel to the original one");
        assertFalse(Util.compareSign(new Vector(1,2,3).dotProduct(new Vector(1,2,3).normalize()),-1),"ERROR: the normalized vector is opposite to the original one");
    }


}