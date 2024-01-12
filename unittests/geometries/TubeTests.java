package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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

    /**Test method for {@link geometries.Tube#findIntersections(Ray)}*/
    @Test
    void testFindIntersections() {
        final Point p100 = new Point(1,0,0);
        final Point p402 = new Point(4,0,2);
        final Point p1_502 = new Point(1.5,0,2);
        final Point p2_512 = new Point(2.5,0,2);
        final Point p200 = new Point(2,0,0);
        final Point p102 = new Point(1,0,2);
        final Vector v001 = new Vector(0,0,1);
        final Vector v110 = new Vector(1,1,0);
        final Point p0_500 = new Point(0.5,0,0);
        final Point p30_51 = new Point(3,0.5,1);
        Tube tube = new Tube(1d,new Ray(p100,v001));
        // ============ Equivalence Partitions Tests ==============
        //TC01: doesn't intersect (0 points)
        assertNull(tube.findIntersections(new Ray(p402,v110)),"doesn't intersect");
        //TC02: starts inside and intersect once (1 point)
        List<Point> result2 = tube.findIntersections(new Ray(p1_502,v110));
        assertEquals(1,result2.size(),"Wrong number of points");
        assertEquals(List.of(new Point(1.91,0.41,2)),result2,"starts inside and intersect once");
        //TC03: starts outside and intersect twice (2 points)
        List<Point> result3 = tube.findIntersections(new Ray(p30_51,new Vector(-4,-0.5,-1)));
        assertEquals(result3.size(),2,"Wrong number of points");
        assertEquals(result3,List.of(new Point(1.93,0.37,0.73),new Point(0.01,0.13,0.25)),"starts outside and intersect twice");
        //TC04: ray's line intersects the tube (0 points)
        assertNull(tube.findIntersections(new Ray(p2_512,v110)),"ray's line intersects the tube");
        // =============== Boundary Values Tests ==================
        // **** Group: Ray's line parallel to the axis (all tests 0 points)
        //TC11: ray's line is inside the tube
        assertNull(tube.findIntersections(new Ray(p0_500,v001)),"ray's line is parallel to axis and inside the tube");
        //TC12: ray's line is outside the tube
        assertNull(tube.findIntersections(new Ray(p402,v001)),"ray's line is parallel to axis and outside the tube");
        //TC13: ray's line is included in the tube
        assertNull(tube.findIntersections(new Ray(p200,v001)),"ray's line is parallel to axis and included in the tube");
        //TC14: ray is included in the axis
        assertNull(tube.findIntersections(new Ray(p102,v001)),"ray is included in the axis");
        //TC15: ray starts at axis's head
        assertNull(tube.findIntersections(new Ray(p100,v001)),"ray's line is parallel to axis and ray starts at axis's head");
        // **** Group: Ray's line orthogonal to the tube
        //TC16: starts outside and intersect twice (2 points)
        //TC17: starts on the tube (1 point)


    }
}