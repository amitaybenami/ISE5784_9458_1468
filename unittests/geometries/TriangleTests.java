package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

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

    /**Test method for {@link geometries.Triangle#findIntersections(Ray)}*/
    @Test
    void testFindIntersections() {
        final Point p100 = new Point(1,0,0);
        final Point p010 = new Point(0,1,0);
        final Point p110 = new Point(1,1,0);
        final Point p111 = new Point(1,1,1);
        final Point p0_70_70 = new Point(0.7,0.7,0);
        final Vector vM0_3M0_3M1 = new Vector(-0.3,-0.3,-1);
        final Vector vM0_7M0_7M1 = new Vector(-0.7,-0.7,-1);
        final Vector v0_20_2M1 = new Vector(0.2,0.2,-1);
        final Vector vM0_50M1 = new Vector(-0.5,0,-1);
        final Vector v0_50M1 = new Vector(0.5,0,-1);
        final  Vector v00M1 = new Vector(0,0,-1);
        Triangle triangle = new Triangle(p010, p110,p100);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Point is inside the triangle (1 point)
        List<Point> result1 = triangle.findIntersections(new Ray(p111,vM0_3M0_3M1));
        assertEquals(1,result1.size(),"Wrong number of points");
        assertEquals(List.of(p0_70_70),result1,"Point is inside the triangle");
        // **** Group: Point is outside the triangle (all tests 0 points)
        //TC02: Point is against edge
        assertNull(triangle.findIntersections(new Ray(p111,vM0_7M0_7M1)),"Point is outside against edge");
        //TC03: Point is against vertex
        assertNull(triangle.findIntersections(new Ray(p111,v0_20_2M1)),"Point is outside against vertex");
        // =============== Boundary Values Tests ==================
        //TC11: Point is on edge (0 points)
        assertNull(triangle.findIntersections(new Ray(p111,v00M1)),"Point is on edge");
        //TC11: Point is in vertex (0 points)
        assertNull(triangle.findIntersections(new Ray(p111,vM0_50M1)),"Point is in vertex");
        //TC11: Point is on edge's continuation (0 points)
        assertNull(triangle.findIntersections(new Ray(p111,v0_50M1)),"Point is on edge's continuation");
    }
}