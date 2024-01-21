package primitives;

import static org.junit.jupiter.api.Assertions.*;

import geometries.Triangle;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    /**Test method for {@link Ray#findClosestPoint(List)}*/
    @Test
    void testFindClosestPoint() {
        Ray ray = new Ray(new Point(1,0,0),new Vector(0,0,1));
        final Point p101 = new Point (1,0,1);
        final Point p102 = new Point(1,0,2);
        final Point p103 = new Point(1,0,3);
        final Point p104 = new Point(1,0,4);
        // ============ Equivalence Partitions Tests ==============
        //TC01: the closest point is in the middle of the list
        assertEquals(p101,ray.findClosestPoint(List.of(p102,p103,p101,p104)),"closest point is in the middle of the list");
        // =============== Boundary Values Tests ==================
        //TC11: the list is empty
        assertNull(ray.findClosestPoint(List.of()),"the list is empty");
        //TC12: the first point is the closest
        assertEquals(p101,ray.findClosestPoint(List.of(p101,p104,p102,p103)),"the first point is the closest");
        //TC13: the last point is the closest
        assertEquals(p101,ray.findClosestPoint(List.of(p103,p104,p102,p101)),"the last point is the closest");
    }
}