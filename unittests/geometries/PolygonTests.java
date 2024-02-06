package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Testing Polygons
 * @author Dan
 */
public class PolygonTests {
   /**
    * Delta value for accuracy when comparing the numbers of type 'double' in
    * assertEquals
    */
   private final double DELTA = 0.000001;

   /** Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}. */
   @Test
   public void testConstructor() {
      // ============ Equivalence Partitions Tests ==============

      // TC01: Correct concave quadrangular with vertices in correct order
      assertDoesNotThrow(() -> new Polygon(new Point(0, 0, 1),
                                           new Point(1, 0, 0),
                                           new Point(0, 1, 0),
                                           new Point(-1, 1, 1)),
                         "Failed constructing a correct polygon");

      // TC02: Wrong vertices order
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
                   "Constructed a polygon with wrong order of vertices");

      // TC03: Not in the same plane
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
                   "Constructed a polygon with vertices that are not in the same plane");

      // TC04: Concave quadrangular
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                                     new Point(0.5, 0.25, 0.5)), //
                   "Constructed a concave polygon");

      // =============== Boundary Values Tests ==================

      // TC10: Vertex on a side of a quadrangular
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                                     new Point(0, 0.5, 0.5)),
                   "Constructed a polygon with vertix on a side");

      // TC11: Last point = first point
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
                   "Constructed a polygon with vertice on a side");

      // TC12: Co-located points
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                   "Constructed a polygon with vertice on a side");

   }

   /** Test method for {@link geometries.Polygon#getNormal(primitives.Point)}. */
   @Test
   public void testGetNormal() {
      // ============ Equivalence Partitions Tests ==============
      // TC01: There is a simple single test here - using a quad
      Point[] pts =
         { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1) };
      Polygon pol = new Polygon(pts);
      // ensure there are no exceptions
      assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
      // generate the test result
      Vector result = pol.getNormal(new Point(0, 0, 1));
      // ensure |result| = 1
      assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
      // ensure the result is orthogonal to all the edges
      for (int i = 0; i < 3; ++i)
         assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                      "Polygon's normal is not orthogonal to one of the edges");
   }

   final Point p100 = new Point(1,0,0);
   final Point p010 = new Point(0,1,0);
   final Point p110 = new Point(1,1,0);
   final Point p111 = new Point(1,1,1);
   final Point pM1M10 = new Point(-1,-1,0);
   final Point p0_70_70 = new Point(0.7,0.7,0);
   final Vector vM0_3M0_3M1 = new Vector(-0.3,-0.3,-1);
   Polygon polygon = new Polygon(p010, p110,p100,pM1M10);

   /**Test method for {@link Intersectable#findIntersections(Ray)}*/
   @Test
   void testFindIntersections() {
      final Vector v0_20_2M1 = new Vector(0.2,0.2,-1);
      final Vector vM0_50M1 = new Vector(-0.5,0,-1);
      final Vector v0_50M1 = new Vector(0.5,0,-1);
      final  Vector v00M1 = new Vector(0,0,-1);
      final Vector v0_5M0_5M1 = new Vector(0.5,-0.5,-1);
      // ============ Equivalence Partitions Tests ==============
      // TC01: Point is inside the polygon (1 point)
      List<Point> result1 = polygon.findIntersections(new Ray(p111,vM0_3M0_3M1));
      assertEquals(1,result1.size(),"Wrong number of points");
      assertEquals(List.of(p0_70_70),result1,"Point is inside the polygon");
      // **** Group: Point is outside the polygon (all tests 0 points)
      //TC02: Point is against edge
      assertNull(polygon.findIntersections(new Ray(p111,v0_5M0_5M1)),"Point is outside against edge");
      //TC03: Point is against vertex
      assertNull(polygon.findIntersections(new Ray(p111,v0_20_2M1)),"Point is outside against vertex");
      // =============== Boundary Values Tests ==================
      //TC11: Point is on edge (0 points)
      assertNull(polygon.findIntersections(new Ray(p111,vM0_50M1)),"Point is on edge");
      //TC11: Point is in vertex (0 points)
      assertNull(polygon.findIntersections(new Ray(p111,v00M1)),"Point is in vertex");
      //TC11: Point is on edge's continuation (0 points)
      assertNull(polygon.findIntersections(new Ray(p111,v0_50M1)),"Point is on edge's continuation");
   }

   /**
    * Test method for {@link Polygon#findGeoIntersectionsHelper(Ray, double)}
    */
   @Test
   void testFindGeoIntersectionsHelper() {
      // ============ Equivalence Partitions Tests ==============
      // TC01: Point is inside the distance (1 point)
      List<Point> result1 = polygon.findIntersections(new Ray(p111,vM0_3M0_3M1),2);
      assertEquals(1,result1.size(),"Wrong number of points");
      assertEquals(List.of(p0_70_70),result1,"Point is inside the distance");
      // TC02: Point is outside the distance (0 points)
      assertNull(polygon.findIntersections(new Ray(p111,vM0_3M0_3M1),1),"Point is outside the distance");
      // =============== Boundary Values Tests ==================
      // TC11: Point is on distance (1 point)
      List<Point> result2 = polygon.findIntersections(new Ray(p111,vM0_3M0_3M1),1.0862780491200215);
      assertEquals(1, result2.size(),"Wrong number of points");
      assertEquals(List.of(p0_70_70), result2,"Point is on distance");
   }
}
