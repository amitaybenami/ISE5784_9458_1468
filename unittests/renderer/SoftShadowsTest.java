package renderer;

import static java.awt.Color.*;
import org.junit.jupiter.api.Test;
import geometries.*;
import primitives.*;
import scene.Scene;
import lighting.*;

        public class SoftShadowsTest {
            private final Scene scene = new Scene("Soft Shadows Test");
            private final Camera.Builder camera = Camera.getBuilder()
                    .setDirection(Point.ZERO, new Vector(0,1,0))
                    .setLocation(new Point(0, 0, 1000)).setVpDistance(500)
                    .setVpSize(200, 200)
                    .setRayTracer(new SimpleRayTracer(scene));

            @Test
            public void testSoftShadows() {
                // Create 10 spheres placed in a line along the x-axis
                for (int i = 0; i < 10; i++) {
                    Geometry sphere = new Sphere(60d, new Point(i * 130 - 450, 0, -150))
                            .setEmission(new Color(255, 0, 0))
                            .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));
                    scene.geometries.add(sphere);
                }
                for (int i = 0; i < 10; i++) {
                    Geometry sphere = new Sphere(60d, new Point(i * 130 - 450, 150, -150))
                            .setEmission(new Color(i*20, i*10, i*30))
                            .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));
                    scene.geometries.add(sphere);
                }
                for (int i = 0; i < 10; i++) {
                    Geometry sphere = new Sphere(60d, new Point(i * 130 - 450, -150, -150))
                            .setEmission(new Color((100-i*5)*20, i*9, 30))
                            .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));
                    scene.geometries.add(sphere);
                }
                // Add a triangle as a surface to cast shadows on
                Geometry triangle = new Triangle(new Point(-600, -200, -200), new Point(600, -200, -200), new Point(0, 600, -200))
                        .setEmission(new Color(30, 30, 30))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));
                scene.geometries.add(triangle);

                // Add a light source that casts soft shadows
                scene.lights.add(new PointLight(new Color(255, 255, 255), new Point(-500, 0, 500))
                        .setRadius(100)); // Set the radius of the light source to create soft shadows

                camera.setImageWriter(new ImageWriter("softShadowsTest", 400, 400).setAmountOfSamples(9))
                        .build()
                        .renderImage()
                        .writeToImage();
            }
        }