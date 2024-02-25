package renderer;

import geometries.Sphere;
import lighting.AmbientLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

import static java.awt.Color.*;

class SuperSamplingTest {

     @Test
     void testFocus(){
         Scene scene = new Scene("Test scene");
         Camera.Builder camera     = Camera.getBuilder()
                 .setDirection(new Point(15,0,0),Vector.Z)
                 .setLocation(new Point(-500, 0, 500)).setVpDistance(650)
                 .setVpSize(200, 200)
                 .setRayTracer(new SimpleRayTracer(scene));
         scene.geometries.add(
                 new Sphere(20,new Point(0,0,0)).setEmission(new Color(BLUE)),
                 new Sphere(20,new Point(30,0,0)).setEmission(new Color(RED))
         );
         scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.75));
         camera.setImageWriter(new ImageWriter("focus", 600, 600)
                         .setFocus(true).setAmountOfSamples(9).setFocalDistance(10))
                 .build()
                 .renderImage()
                 .writeToImage();
     }
}
