package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * unit tests for {@link Cylinder}
 *
 * @author Elad and Amitay
 */
class CylinderTests {

    /**
     * Test method for {@link geometries.Cylinder#getNormal(primitives.Point)}
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: on the tube
        assertEquals(new Vector(1, 0, 0), new Cylinder(3, new Ray(new Point(0, 0, -1), new Vector(0, 0, 1)), 5).getNormal(new Point(3, 0, 0)), "ERROR: Cylinder.getNormal() doesn't work correctly on the tube");
        //TC02: on the head base
        assertEquals(new Vector(0, 0, -1), new Cylinder(3, new Ray(new Point(0, 0, -1), new Vector(0, 0, 1)), 5).getNormal(new Point(1, 1, -1)), "ERROR: Cylinder.getNormal() doesn't work correctly on the head base");
        //TC03: on the second base
        assertEquals(new Vector(0, 0, 1), new Cylinder(3, new Ray(new Point(0, 0, -1), new Vector(0, 0, 1)), 5).getNormal(new Point(1, 1, 4)), "ERROR: Cylinder.getNormal() doesn't work correctly on the second base");
        // =============== Boundary Values Tests ==================
        //TC10:  point is the center of the head base
        assertEquals(new Vector(0, 0, -1), new Cylinder(3, new Ray(new Point(0, 0, -1), new Vector(0, 0, 1)), 5).getNormal(new Point(0, 0, -1)), "ERROR: Cylinder.getNormal() doesn't work correctly point is the center of the head base");
        //TC11: point is the center of the second base
        assertEquals(new Vector(0, 0, 1), new Cylinder(3, new Ray(new Point(0, 0, -1), new Vector(0, 0, 1)), 5).getNormal(new Point(0, 0, 4)), "ERROR: Cylinder.getNormal() doesn't work correctly point is the center of the second base");
    }
}