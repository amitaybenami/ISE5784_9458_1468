package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * unit tests for {@link Geometries}
 * @author Elad and Amitay
 */
class GeometriesTests {

    /**Test method for {@link geometries.Geometries#findIntersections(primitives.Ray)}*/
    @Test
    void testFindIntersections() {
        Plane plane = new Plane(new Point(1,-5,0),new Vector(25,-27.5,-20));
        Sphere sphere = new Sphere(1d,new Point(4,1,0));
        Triangle triangle = new Triangle(new Point(1,2,-1),new Point(1,0,0),new Point(0.5,1,2));
        Geometries geometries = new Geometries(plane,sphere,triangle);
        // ============ Equivalence Partitions Tests ==============
        //TC01: Some of the geometries intersected
        assertEquals(3,geometries.findIntersections(new Ray(new Point(6,1.5,0),new Vector(-1,0,0))).size(),"Some of the geometries intersected");
        // =============== Boundary Values Tests ==================
        //TC11: Empty geometries list
        assertNull(new Geometries().findIntersections(new Ray(new Point(1,0,0),new Vector(0,1,0))),"Empty geometries list");
        //TC12: No geometry intersected
        assertNull(geometries.findIntersections(new Ray(new Point(0,3,0),new Vector(-4,-3,0))),"No geometry intersected");
        //TC13: One geometry intersected
        assertEquals(1,geometries.findIntersections(new Ray(new Point(2,0,0),new Vector(0,-6,0))).size(),"One geometry intersected");
        //TC14: All the geometries intersected
        assertEquals(4,geometries.findIntersections(new Ray(new Point(0,1.5,0),new Vector(1,0,0))).size(),"All the geometries intersected");
    }
}