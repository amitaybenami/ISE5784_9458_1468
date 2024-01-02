package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Util;

import static org.junit.jupiter.api.Assertions.*;

/**
 * unit tests for {@link Sphere}
 * @author Elad and Amitay
 */
class SphereTests {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;

    /**Test method for {@link geometries.Sphere#getNormal(primitives.Point)}*/
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: usual point
        assertEquals(1,new Sphere(5,new Point(1,2,3)).getNormal(new Point(1,6,6)).length(),DELTA,"ERROR: the normal vector is not a unit vector");
        assertThrows(IllegalArgumentException.class,()->new Sphere(5,new Point(1,2,3)).getNormal(new Point(1,6,6)).crossProduct(new Point(1,6,6).subtract(new Point(1,2,3))),"ERROR: the normal vector is in the correct direction");
        assertFalse(Util.compareSign(new Sphere(5,new Point(1,2,3)).getNormal(new Point(1,6,6)).dotProduct(new Point(1,6,6).subtract(new Point(1,2,3))),-1),"ERROR: the normal vector is opposite to the correct one");    }
}