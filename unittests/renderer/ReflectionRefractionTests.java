/**
 * 
 */
package renderer;

import static java.awt.Color.*;

import geometries.Plane;
import geometries.Polygon;
import lighting.LightSource;
import lighting.PointLight;
import org.junit.jupiter.api.Test;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.SpotLight;
import primitives.*;
import renderer.*;
import scene.Scene;

/** Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 * @author dzilb */
public class ReflectionRefractionTests {
   /** Scene for the tests */
   private final Scene          scene         = new Scene("Test scene");
   /** Camera builder for the tests with triangles */
   private final Camera.Builder cameraBuilder = Camera.getBuilder()
      .setDirection(Point.ZERO, new Vector(0,1,0))
      .setRayTracer(new SimpleRayTracer(scene));

   /** Produce a picture of a sphere lighted by a spot light */
   @Test
   public void twoSpheres() {
      scene.geometries.add(
                           new Sphere(50d, new Point(0, 0, -50)).setEmission(new Color(BLUE))
                              .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setKt(0.3)),
                           new Sphere(25d, new Point(0, 0, -50)).setEmission(new Color(RED))
                              .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)));
      scene.lights.add(
                       new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2))
                          .setKl(0.0004).setKq(0.0000006));

      cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
         .setVpSize(150, 150)
         .setImageWriter(new ImageWriter("refractionTwoSpheres", 500, 500))
         .build()
         .renderImage()
         .writeToImage();
   }

   /** Produce a picture of a sphere lighted by a spot light */
   @Test
   public void twoSpheresOnMirrors() {
      scene.geometries.add(
                           new Sphere(400d, new Point(-950, -900, -1000)).setEmission(new Color(0, 50, 100))
                              .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)
                                 .setKt(new Double3(0.5, 0, 0))),
                           new Sphere(200d, new Point(-950, -900, -1000)).setEmission(new Color(100, 50, 20))
                              .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),
                           new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                                        new Point(670, 670, 3000))
                              .setEmission(new Color(20, 20, 20))
                              .setMaterial(new Material().setKr(1)),
                           new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                                        new Point(-1500, -1500, -2000))
                              .setEmission(new Color(20, 20, 20))
                              .setMaterial(new Material().setKr(new Double3(0.5, 0, 0.4))));
      scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));
      scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4))
         .setKl(0.00001).setKq(0.000005));

      cameraBuilder.setLocation(new Point(0, 0, 10000)).setVpDistance(10000)
         .setVpSize(2500, 2500)
         .setImageWriter(new ImageWriter("reflectionTwoSpheresMirrored", 500, 500))
         .build()
         .renderImage()
         .writeToImage();
   }

   /** Produce a picture of a two triangles lighted by a spot light with a
    * partially
    * transparent Sphere producing partial shadow */
   @Test
   public void trianglesTransparentSphere() {
      scene.geometries.add(
                           new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                                        new Point(75, 75, -150))
                              .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)),
                           new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                              .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)),
                           new Sphere(30d, new Point(60, 50, -50)).setEmission(new Color(BLUE))
                              .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.6)));
      scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));
      scene.lights.add(
                       new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                          .setKl(4E-5).setKq(2E-7));

      cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
         .setVpSize(200, 200)
         .setImageWriter(new ImageWriter("refractionShadow", 600, 600))
         .build()
         .renderImage()
         .writeToImage();
   }

   @Test
   public void impressiveTest(){
      final Point p000 = new Point(0,0,0);
      scene.geometries.add(
//              new Plane(new Point(0,0,-7),new Vector(0,0,1))
//                      .setEmission(new Color(20,20,20)).setMaterial(new Material()
//                              .setKr(1)), // the mirror
              new Sphere(0.5,p000)
                      .setEmission(new Color(RED)).setMaterial(new Material()
                              .setKs(0.5).setKd(0.5).setShininess(60)), // the nose
              new Sphere(5,p000)
                      .setEmission(new Color(135,206,235)).setMaterial(new Material()
                              .setKs(0.2).setKd(0.2).setShininess(30).setKt(0.6)), // the head
              new Triangle(new Point(-2,2,0),new Point(2,2,0),new Point(0,3,0))
                      .setEmission(new Color(RED)).setMaterial(new Material()
                              .setKs(0.5).setKd(0.5).setShininess(60)), // the mouth
              new Polygon(new Point(2.5,-2.5,0),new Point(1.5,-2.5,0),new Point(1.5,-1.5,0),new Point(2.5,-1.5,0))
                      .setEmission(new Color(BLACK)).setMaterial(new Material()
                              .setKs(0.5).setKd(0.5).setShininess(60)), // the left eye
              new Polygon(new Point(-2.5,-2.5,0),new Point(-1.5,-2.5,0),new Point(-1.5,-1.5,0),new Point(-2.5,-1.5,0))
                      .setEmission(new Color(BLACK)).setMaterial(new Material()
                              .setKs(0.5).setKd(0.5).setShininess(60)), // the right eye
              new Sphere(2.1,new Point(5,-5,0))
                      .setEmission(new Color(BLUE)).setMaterial(new Material()
                              .setKs(0.5).setKd(0.5).setShininess(60)), // the left ear
              new Sphere(2.1,new Point(-5,-5,0))
                      .setEmission(new Color(BLUE)).setMaterial(new Material()
                              .setKs(0.5).setKd(0.5).setShininess(60)), // the right ear
              new Plane(new Point(-15,0,0),new Vector(-1,-1,0))
                      .setEmission(new Color(20,20,20)).setMaterial(new Material()
                              .setKr(1))// the right mirror
//              new Plane(new Point(15,0,0),new Vector(-1,1,0))
//                      .setEmission(new Color(20,20,20)).setMaterial(new Material()
//                              .setKr(1))// the left mirror
      );
      scene.lights.add(
              new PointLight(new Color(500,500,250),new Point(0,40,0)).setKl(0.001).setKq(0.0002));
      scene.lights.add(
              new SpotLight(new Color(500,500,250),new Point(2,-2,0.5),new Vector(0,0,1)).setKl(0.001).setKq(0.00004).setNarrowBeam(10));
      scene.lights.add(
              new SpotLight(new Color(500,500,250),new Point(-2,-2,0.5),new Vector(0,0,1)).setKl(0.001).setKq(0.00004).setNarrowBeam(10));
      cameraBuilder.setLocation(new Point(0, 0, 100)).setVpDistance(95)
              .setVpSize(60, 60)
              .setImageWriter(new ImageWriter("impressive", 1000, 1000))
              .build()
              .renderImage()
              .writeToImage();
      cameraBuilder.setImageWriter(new ImageWriter("impressiveOtherAngle",1000,1000))
              .build().resetDirection(new Point(60,60,60),new Point(0,0,0)).renderImage().writeToImage();
      cameraBuilder.setImageWriter(new ImageWriter("impressiveRotated",1000,1000))
              .build().rotate(30).renderImage().writeToImage();
   }
}
