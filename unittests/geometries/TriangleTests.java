package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Util;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * unit tests for {@link Triangle}
 * @author Elad and Amitay
 */
class TriangleTests {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;

    /**Test method for {@link geometries.Triangle#getNormal(primitives.Point)}*/
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: usual point
        assertEquals(1,new Triangle(new Point(1,2,3),new Point(2,3,8),new Point(-1,2,7)).getNormal(new Point(1,4,17)).length(),DELTA,"ERROR: the normal vector is not a unit vector");
        assertTrue(Util.isZero(new Triangle(new Point(1,2,3),new Point(2,3,8),new Point(-1,2,7)).getNormal(new Point(1,4,17)).dotProduct(new Vector(1,1,5))),"ERROR: the normal vector is not orthogonal to one of the vectors on the triangle");
        assertTrue(Util.isZero(new Triangle(new Point(1,2,3),new Point(2,3,8),new Point(-1,2,7)).getNormal(new Point(1,4,17)).dotProduct(new Vector(-2,0,4))),"ERROR: the normal vector is not orthogonal to one of the vectors on the triangle");
    }
}