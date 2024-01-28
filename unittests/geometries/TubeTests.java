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

    /**Test method for {@link Intersectable#findIntersections(Ray)}*/
    @Test
    void testFindIntersections() {
        final Point p100 = new Point(1,0,0);
        final Point p402 = new Point(4,0,2);
        final Point p1_502 = new Point(1.5,0,2);
        final Point p2_512 = new Point(2.5,0,2);
        final Point p200 = new Point(2,0,0);
        final Point p102 = new Point(1,0,2);
        final Point p202 = new Point(2,0,2);
        final Point p002 = new Point(0,0,2);
        final Point p0_500 = new Point(0.5,0,0);
        final Point p400 = new Point(4,0,0);
        final Point p000 = new Point(0,0,0);
        final Point p30_51 = new Point(3,0.5,1);
        final Point p302 = new Point(3,0,2);
        final Point p201 = new Point(2,0,1);
        final Point p00M1 = new Point(0,0,-1);
        final Point p1_500_5 = new Point(1.5,0,0.5);
        final Point p2M10 = new Point(2,-1,0);
        final Vector v001 = new Vector(0,0,1);
        final Vector v110 = new Vector(1,1,0);
        final Vector vM100 = new Vector(-1,0,0);
        final Vector v100 = new Vector(1,0,0);
        final Vector vM20M2 = new Vector(-2,0,-2);
        final Vector v202 = new Vector(2,0,2);
        final Vector v011 = new Vector(0,1,1);
        final Vector v0M1M1 = new Vector(0,-1,-1);
        final Vector v010 = new Vector(0,1,0);
        final Vector v0M10 = new Vector(0,-1,0);
        final Tube tube = new Tube(1d,new Ray(p100,v001));
        // ============ Equivalence Partitions Tests ==============
        //TC01: doesn't intersect (0 points)
        //assertNull(tube.findIntersections(new Ray(p402,v110)),"doesn't intersect");
        //TC02: starts inside and intersect once (1 point)
       // assertEquals(List.of(new Point(1.9114378277661477,0.4114378277661476,2)),tube.findIntersections(new Ray(p1_502,v110)),"starts inside and intersect once");
        //TC03: starts outside and intersect twice (2 points)
        assertEquals(List.of(new Point(1.93,0.37,0.73),new Point(0.01,0.13,0.25)),tube.findIntersections(new Ray(p30_51,new Vector(-4,-0.5,-1))),"starts outside and intersect twice");
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
        assertEquals(List.of(p202,p002),tube.findIntersections(new Ray(p402,vM100)),"Ray is orthogonal to tube and intersects twice");
        //TC17: starts outside and doesn't intersect (0 points)
        assertNull(tube.findIntersections(new Ray(p402,v100)),"starts outside and ray's line intersects axis");
        //TC18: starts on the tube and intersects once(1 point)
        assertEquals(List.of(p002),tube.findIntersections(new Ray(p202,vM100)),"Ray is orthogonal to tube and starts on the tube and intersects once");
        //TC19: starts on the tube and doesn't intersect (0 points)
        assertNull(tube.findIntersections(new Ray(p202,v100)),"starts on the tube and ray's line intersects axis");
        //TC20: starts inside the tube after axis (1 point)
        assertEquals(List.of(p202),tube.findIntersections(new Ray(p1_502,v100)),"ray starts inside the tube after axis, orthogonal to tube");
        //TC21: starts inside the tube and intersects axis (1 point)
        assertEquals(List.of(p002),tube.findIntersections(new Ray(p1_502,vM100)),"starts inside the tube and intersects axis");
        //TC22: starts on axis (1 point)
        assertEquals(List.of(p202),tube.findIntersections(new Ray(p102,v100)),"starts on axis and orthogonal to tube");
        //**** Group: Ray's line crosses axis's head and orthogonal to tube
        //TC21: starts outside and intersects twice(2 points)
        assertEquals(List.of(p200,p000),tube.findIntersections(new Ray(p400,vM100)),"starts outside and crosses axis's head and orthogonal to tube");
        //TC22: starts outside and doesn't intersect (0 points)
        assertNull(tube.findIntersections(new Ray(p400,v100)),"starts outside and ray's line crosses axis's head and orthogonal to tube");
        //TC23: starts on tube and intersects once (1 point)
        assertEquals(List.of(p000),tube.findIntersections(new Ray(p200,vM100)),"starts on tube and crosses axis's head and orthogonal to tube");
        //TC24: starts on tube and doesn't intersect (0 points)
        assertNull(tube.findIntersections(new Ray(p200,v100)),"starts on tube and ray's line crosses axis's head and orthogonal to tube");
        //TC25: starts inside and crosses axis's head (1 point)
        assertEquals(List.of(p200),tube.findIntersections(new Ray(p0_500,v100)),"starts inside and crosses axis's head and orthogonal to tube");
        //TC26: starts inside after axis's head (1 point)
        assertEquals(List.of(p000),tube.findIntersections(new Ray(p0_500,vM100)),"starts inside and ray's line crosses axis's head and orthogonal to tube");
        //TC27: starts at axis's head (1 point)
        assertEquals(List.of(p200),tube.findIntersections(new Ray(p100,v100)),"starts at axis's head and orthogonal to tube");
        //**** Group: Ray's line crosses axis's head and  not orthogonal to tube
        //TC28: starts outside and intersects twice (2 points)
        assertEquals(List.of(p201,p00M1),tube.findIntersections(new Ray(p302,vM20M2)),"starts outside and crosses axis's head");
        //TC29: starts outside and doesn't intersect (0 points)
        assertNull(tube.findIntersections(new Ray(p302,v202)),"starts outside and ray's line crosses axis's head");
        //TC30: starts on tube and intersects once (1 point)
        assertEquals(List.of(p00M1),tube.findIntersections(new Ray(p201,vM20M2)),"starts on tube and crosses axis's head");
        //TC31: starts on tube and doesn't intersect (0 points)
        assertNull(tube.findIntersections(new Ray(p201,v202)),"starts on tube and ray's line crosses axis's head");
        //TC32: starts inside and crosses axis's head (1 point)
        assertEquals(List.of(p00M1),tube.findIntersections(new Ray(p1_500_5,vM20M2)),"starts inside and crosses axis's head");
        //TC33: starts inside after axis's head (1 point)
        assertEquals(List.of(p201),tube.findIntersections(new Ray(p1_500_5,v202)),"starts inside and ray's line crosses axis's head");
        //TC34: starts at axis's head (1 point)
        assertEquals(List.of(p201),tube.findIntersections(new Ray(p100,v202)),"starts at axis's head");
        //**** Group: Ray's line tangents to tube (all tests 0 points)
        //TC35: tangents to tube
        assertNull(tube.findIntersections(new Ray(p2M10,v011)),"tangents to tube");
        //TC36: Ray's line tangents to tube
        assertNull(tube.findIntersections(new Ray(p2M10,v0M1M1)),"Ray's line tangents to tube");
        //TC37: tangents to tube and orthogonal to axis
        assertNull(tube.findIntersections(new Ray(p2M10,v010)),"tangents to tube and orthogonal to axis");
        //TC38: Ray's line tangents to tube and orthogonal to axis
        assertNull(tube.findIntersections(new Ray(p2M10,v0M10)),"Ray's line tangents to tube and orthogonal to axis");
    }
}