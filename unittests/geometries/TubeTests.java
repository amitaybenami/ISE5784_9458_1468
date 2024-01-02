package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * unit tests for {@link Tube}
 * @author Elad and Amitay
 */
class TubeTests{

    /**Test method for {@link geometries.Tube#getNormal(primitives.Point)}*/
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: usual point
        assertEquals(new Vector(1,0,0),new Tube(3,new Ray(new Point(0,0,-1),new Vector(0,0,1))).getNormal(new Point(3,0,0)),"ERROR: Tube.getNormal() doesn't work correctly");
        // =============== Boundary Values Tests ==================
        //TC10: vector between point and Ray's head is orthogonal to Ray's axis
        assertEquals(new Vector(1,0,0),new Tube(3,new Ray(new Point(0,0,0),new Vector(0,0,1))).getNormal(new Point(3,0,0)),"ERROR: Tube.getNormal() doesn't work correctly when vector between point and Ray's head is orthogonal to Ray's axis");
    }
}