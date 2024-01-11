package primitives;

import static org.junit.jupiter.api.Assertions.*;

import geometries.Triangle;
import org.junit.jupiter.api.Test;

/**
 * unit tests for {@link Ray}
 * @author Elad and Amitay
 */
class RayTests {

    /**Test method for {@link Ray#getPoint(double)}*/
    @Test
    void testGetPoint() {
        Ray ray = new Ray(new Point(1,0,0),new Vector(1,0,0));
        // ============ Equivalence Partitions Tests ==============
        //TC01: positive t
        assertEquals(new Point(3,0,0),ray.getPoint(2));
        //TC02: negative t
        assertEquals(new Point(-1,0,0),ray.getPoint(-2));
        // =============== Boundary Values Tests ==================
        //TC11: head
        assertEquals(new Point(1,0,0),ray.getPoint(0));
    }
}