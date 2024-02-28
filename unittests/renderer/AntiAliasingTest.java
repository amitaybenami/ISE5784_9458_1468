package renderer;

import geometries.Geometry;
import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

public class AntiAliasingTest {
    private final Scene scene = new Scene("Anti Aliasing Test");
    private final Camera.Builder camera = Camera.getBuilder()
            .setDirection(Point.ZERO, new Vector(0, 1, 0))
            .setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
            .setVpSize(250, 250)
            .setRayTracer(new SimpleRayTracer(scene));

    @Test
    public void testAntiAliasing() {
        // Create multiple spheres
        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                Geometry sphere = new Sphere(30d, new Point(i * 100, j * 100, -200 - Math.abs(i) * 500))
                        .setEmission(new Color((i + 3) * 50, (j + 3) * 50, 100))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));
                scene.geometries.add(sphere);
            }
        }

        camera.setImageWriter(new ImageWriter("antiAliasingTest", 400, 400)
                        .setAmountOfSamples(9).antiAliasing())
                .build()
                .renderImage()
                .writeToImage();
    }
}
