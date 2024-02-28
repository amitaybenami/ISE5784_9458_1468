package scene;

import geometries.*;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.*;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.io.*;

/**
 * this class represents a scene
 * @author Elad and Amitay
 */
public class Scene {
    public String name;

    public Color background = Color.BLACK;
    public AmbientLight ambientLight = AmbientLight.NONE;
    public Geometries geometries = new Geometries();
    public List<LightSource> lights = new LinkedList<>();

    public Scene(String name) {
        this.name = name;
    }

    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }

    public Scene setByXML(String filepath) {
        new SetByXML(filepath, this);
        return this;
    }


    /**
     * this class is used to set the scene using an XML file
     */
    class SetByXML {

        /**
         * sets the scene using an XML file
         *
         * @param filePath the file path
         * @param scene    the scene
         */
        public SetByXML(String filePath, Scene scene) {
            try {
                File xmlFile = new File(filePath);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(xmlFile);
                doc.getDocumentElement().normalize();

                // Parse background color
                String backgroundColor = doc.getDocumentElement().getAttribute("background-color");
                scene.background = strToColor(backgroundColor);

                // Parse ambient light
                Node ambientLightNode = doc.getElementsByTagName("ambient-light").item(0);
                if (ambientLightNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element ambientLightElement = (Element) ambientLightNode;
                    String ambientLightColor = ambientLightElement.getAttribute("color");
                    scene.ambientLight = new AmbientLight(strToColor(ambientLightColor), Double3.ONE);
                }

                // Parse geometries
                Node geometriesNode = doc.getElementsByTagName("geometries").item(0);
                if (geometriesNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element geometriesElement = (Element) geometriesNode;
                    NodeList geometryNodes = geometriesElement.getChildNodes();

                    for (int i = 0; i < geometryNodes.getLength(); i++) {
                        Node geometryNode = geometryNodes.item(i);

                        if (geometryNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element geometryElement = (Element) geometryNode;

                            switch (geometryElement.getTagName()) {
                                case "sphere":
                                    parseSphere(geometryElement, scene);
                                    break;
                                case "triangle":
                                    parseTriangle(geometryElement, scene);
                                    break;
                                case "plane":
                                    parsePlane(geometryElement, scene);
                                    break;
                                case "tube":
                                    parseTube(geometryElement, scene);
                                    break;
                                case "cylinder":
                                    parseCylinder(geometryElement, scene);
                                    break;
                                case "polygon":
                                    parsePolygon(geometryElement, scene);
                                    break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void parseSphere(Element sphereElement, Scene scene) {
            double radius = Double.parseDouble(sphereElement.getAttribute("radius"));
            String centerString = sphereElement.getAttribute("center");
            Point center = strToPoint(centerString);
            scene.geometries.add(new Sphere(radius, center));
        }

        private void parseTriangle(Element triangleElement, Scene scene) {
            Point p0 = strToPoint(triangleElement.getAttribute("p0"));
            Point p1 = strToPoint(triangleElement.getAttribute("p1"));
            Point p2 = strToPoint(triangleElement.getAttribute("p2"));
            scene.geometries.add(new Triangle(p0, p1, p2));
        }

        private void parsePlane(Element planeElement, Scene scene) {
            Point p0 = strToPoint(planeElement.getAttribute("p0"));
            Point p1 = strToPoint(planeElement.getAttribute("p1"));
            Point p2 = strToPoint(planeElement.getAttribute("p2"));
            scene.geometries.add(new Plane(p0, p1, p2));
        }

        private void parseTube(Element tubeElement, Scene scene) {
            double radius = Double.parseDouble(tubeElement.getAttribute("radius"));
            Point axisHead = strToPoint(tubeElement.getAttribute("axis-head"));
            Vector direction = strToVector(tubeElement.getAttribute("direction"));
            scene.geometries.add(new Tube(radius, new Ray(axisHead, direction)));
        }

        private void parseCylinder(Element cylinderElement, Scene scene) {
            double radius = Double.parseDouble(cylinderElement.getAttribute("radius"));
            Point axisHead = strToPoint(cylinderElement.getAttribute("axis-head"));
            Vector direction = strToVector(cylinderElement.getAttribute("direction"));
            double height = Double.parseDouble(cylinderElement.getAttribute("height"));
            scene.geometries.add(new Cylinder(radius, new Ray(axisHead, direction), height));
        }

        private void parsePolygon(Element polygonElement, Scene scene) {
            NodeList vertexNodes = polygonElement.getElementsByTagName("vertex");
            Point[] vertices = new Point[vertexNodes.getLength()];

            for (int i = 0; i < vertexNodes.getLength(); i++) {
                Element vertexElement = (Element) vertexNodes.item(i);
                Point vertex = strToPoint(vertexElement.getAttribute("position"));
                vertices[i] = vertex;
            }

            scene.geometries.add(new Polygon(vertices));
        }

        private Point strToPoint(String str) {
            String[] arr = str.split(" ");
            return new Point(Double.parseDouble(arr[0]), Double.parseDouble(arr[1]), Double.parseDouble(arr[2]));
        }

        private Color strToColor(String str) {
            String[] arr = str.split(" ");
            return new Color(Double.parseDouble(arr[0]), Double.parseDouble(arr[1]), Double.parseDouble(arr[2]));
        }

        private Vector strToVector(String str) {
            String[] arr = str.split(" ");
            return new Vector(Double.parseDouble(arr[0]), Double.parseDouble(arr[1]), Double.parseDouble(arr[2]));
        }

    }
}