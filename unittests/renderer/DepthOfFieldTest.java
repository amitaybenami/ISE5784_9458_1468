package renderer;

import geometries.Geometry;
import geometries.Sphere;
import lighting.AmbientLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

import static java.awt.Color.*;

//class SuperSamplingTest {
//
//     @Test
//     void testFocus(){
//         Scene scene = new Scene("Test scene");
//         Camera.Builder camera     = Camera.getBuilder()
//                 .setDirection(new Point(15,0,0),Vector.Z)
//                 .setLocation(new Point(-500, 0, 500)).setVpDistance(650)
//                 .setVpSize(200, 200)
//                 .setRayTracer(new SimpleRayTracer(scene));
//         scene.geometries.add(
//                 new Sphere(20,new Point(0,0,0)).setEmission(new Color(BLUE)),
//                 new Sphere(20,new Point(30,0,0)).setEmission(new Color(RED))
//         );
//         scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.75));
//         camera.setImageWriter(new ImageWriter("focus", 600, 600)
//                         .setFocus(false).setAmountOfSamples(9).setFocalDistance(10))
//                 .build()
//                 .renderImage()
//                 .writeToImage();
//     }
//}


public class DepthOfFieldTest {
    private final Scene scene = new Scene("Depth of Field Test");
    private final Camera.Builder camera = Camera.getBuilder()
            .setDirection(Point.ZERO, new Vector(0, 1, 0))
            .setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
            .setVpSize(250, 250)
            .setRayTracer(new SimpleRayTracer(scene));

    @Test
    public void testDepthOfField() {
        // Create multiple spheres
        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                Geometry sphere = new Sphere(30d, new Point(i * 100, j * 100, -200 - Math.abs(i) * 700))
                        .setEmission(new Color((i + 3) * 50, (j + 3) * 50, 100))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));
                scene.geometries.add(sphere);
            }
        }
        scene.geometries.add(new Sphere(60, new Point(100, 100, -300)).setEmission(new Color(BLUE))
                .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.6)));
        camera.setImageWriter(new ImageWriter("depthOfFieldTest", 400, 400)
                        .setFocus(true).setAmountOfSamples(9).setFocalDistance(200).setAntiAliasing(false))
                .build()
                .renderImage()
                .writeToImage();
    }
}