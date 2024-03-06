package special;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;

public class ImpressiveForReal {
    private final Scene scene = new Scene("Complex Scene");
    private final Camera.Builder camera = Camera.getBuilder()
            .setDirection(Point.ZERO, new Vector(0, 1, 0))
            .setLocation(new Point(0, 0, 2000))
            .setVpDistance(300)
            .setVpSize(200, 200)
            .setRayTracer(new SimpleRayTracer(scene))
            .setAmountOfSamples(9)
            .setThreadsCount(3)
       //     .setFocalDistance(2000) lama hashura hazot mashmida?
            ;

    @Test
    public void testComplexScene() {
        // Add 15 spheres
        for (int i = 0; i < 15; i++) {
            Geometry sphere = new Sphere(30, new Point(i * 100 - 700, 0, -500))
                    .setEmission(new Color(i * 20, 255 - i * 10, 255))
                    .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));
            scene.geometries.add(sphere);

            // Add colorful small spheres around each large sphere
            for (int j = 0; j < 5; j++) {
                Geometry smallSphere = new Sphere(10, new Point(i * 100 - 700 + (j * 40) - 80, 0, -500 + (j * 20) - 40))
                        .setEmission(new Color(i * 20, 255 - i * 10, 255))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));
                scene.geometries.add(smallSphere);
            }
        }

        // Add 15 triangles
        for (int i = 0; i < 15; i++) {
            Point[] vertices = {
                    new Point(-150, i * 100 - 700, -300),
                    new Point(150, i * 100 - 700, -300),
                    new Point(0, 150 + i * 100 - 700, -300)
            };
            Geometry triangle = new Triangle(vertices[0], vertices[1], vertices[2])
                    .setEmission(new Color(i * 20, 255 - i * 10, 255))
                    .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));
            scene.geometries.add(triangle);

            // Add colorful small triangles around each large triangle
            for (int j = 0; j < 5; j++) {
                Point[] smallVertices = {
                        new Point(-150 + (j * 40) - 80, i * 100 - 700, -300 + (j * 20) - 40),
                        new Point(150 + (j * 40) - 80, i * 100 - 700, -300 + (j * 20) - 40),
                        new Point(0 + (j * 40) - 80, 150 + i * 100 - 700, -300 + (j * 20) - 40)
                };
                Geometry smallTriangle = new Triangle(smallVertices[0], smallVertices[1], smallVertices[2])
                        .setEmission(new Color(i * 20, 255 - i * 10, 255))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));
                scene.geometries.add(smallTriangle);
            }
        }

        // Add a large triangle beneath everything to cast shadows
        Geometry shadowTriangle = new Triangle(new Point(-1500, -1500, -1000),
                new Point(1500, -1500, -1000),
                new Point(0, 1500, -1000))
                .setEmission(new Color(50, 50, 70))
                .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30));
        scene.geometries.add(shadowTriangle);

        // Add 10 point lights
        for (int i = 0; i < 10; i++) {
            PointLight pointLight = new PointLight(new Color(255 - i * 25, 255 - i * 25, 255 - i * 25),
                    new Point(i * 150 - 300, 0, 300))
                    .setKl(0.0005).setKq(0.0005);
            scene.lights.add(pointLight);
        }

        DirectionalLight directionalLight = new DirectionalLight(new Color(100,100,100),new Vector(0.3,0,-1));
        scene.lights.add(directionalLight);

    //      PointLight pointLight1 = new PointLight(new Color(100,100,100),new Point(0,0,1000)).setKq(0.5).setKl(0.5);
    //    scene.lights.add(pointLight1); // lama halight haze lo ose shadow? tsarih lehazek oto
        camera.setImageWriter(new ImageWriter("complexSceneTest", 600, 600))
                .build()
                .renderImage()
                .writeToImage();
    }
}
