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
    public Scene(String name){
        this.name = name;
    }
    public Scene setBackground(Color background){
        this.background = background;
        return this;
    }
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    public Scene setGeometries(Geometries geometries){
        this.geometries = geometries;
        return this;
    }

    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }

    /**
     * sets the scene using an XML file
     * @param filePath the file path
     * @return the updated scene
     */
    public Scene setByXML(String filePath) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Parse background color
            String backgroundColor = doc.getDocumentElement().getAttribute("background-color");
            background = strToColor(backgroundColor);

            // Parse ambient light
            Node ambientLightNode = doc.getElementsByTagName("ambient-light").item(0);
            if (ambientLightNode.getNodeType() == Node.ELEMENT_NODE) {
                Element ambientLightElement = (Element) ambientLightNode;
                String ambientLightColor = ambientLightElement.getAttribute("color");
                ambientLight = new AmbientLight(strToColor(ambientLightColor),Double3.ONE);
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
                                parseSphere(geometryElement);
                                break;
                            case "triangle":
                                parseTriangle(geometryElement);
                                break;
                            case "plane":
                                parsePlane(geometryElement);
                                break;
                            case "tube":
                                parseTube(geometryElement);
                                break;
                            case "cylinder":
                                parseCylinder(geometryElement);
                                break;
                            case "polygon":
                                parsePolygon(geometryElement);
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    return this;
    }
    private void parseSphere(Element sphereElement) {
        double radius = Double.parseDouble(sphereElement.getAttribute("radius"));
        String centerString = sphereElement.getAttribute("center");
        Point center = strToPoint(centerString);
        geometries.add(new Sphere(radius, center));
    }

    private void parseTriangle(Element triangleElement) {
        Point p0 = strToPoint(triangleElement.getAttribute("p0"));
        Point p1 = strToPoint(triangleElement.getAttribute("p1"));
        Point p2 = strToPoint(triangleElement.getAttribute("p2"));
        geometries.add(new Triangle(p0, p1, p2));
    }

    private void parsePlane(Element planeElement) {
        Point p0 = strToPoint(planeElement.getAttribute("p0"));
        Point p1 = strToPoint(planeElement.getAttribute("p1"));
        Point p2 = strToPoint(planeElement.getAttribute("p2"));
        geometries.add(new Plane(p0, p1, p2));
    }

    private void parseTube(Element tubeElement) {
        double radius = Double.parseDouble(tubeElement.getAttribute("radius"));
        Point axisHead = strToPoint(tubeElement.getAttribute("axis-head"));
        Vector direction =strToVector(tubeElement.getAttribute("direction"));
        geometries.add(new Tube(radius, new Ray(axisHead, direction)));
    }

    private void parseCylinder(Element cylinderElement) {
        double radius = Double.parseDouble(cylinderElement.getAttribute("radius"));
        Point axisHead = strToPoint(cylinderElement.getAttribute("axis-head"));
        Vector direction = strToVector(cylinderElement.getAttribute("direction"));
        double height = Double.parseDouble(cylinderElement.getAttribute("height"));
        geometries.add(new Cylinder(radius, new Ray(axisHead, direction), height));
    }

    private void parsePolygon(Element polygonElement) {
        NodeList vertexNodes = polygonElement.getElementsByTagName("vertex");
        Point[] vertices = new Point[vertexNodes.getLength()];

        for (int i = 0; i < vertexNodes.getLength(); i++) {
            Element vertexElement = (Element) vertexNodes.item(i);
            Point vertex = strToPoint(vertexElement.getAttribute("position"));
            vertices[i] = vertex;
        }

        geometries.add(new Polygon(vertices));
    }

    private Point strToPoint(String str){
        String[] arr = str.split(" ");
        return new Point(Double.parseDouble(arr[0]),Double.parseDouble(arr[1]),Double.parseDouble(arr[2]));
    }

    private Color strToColor(String str){
        String[] arr = str.split(" ");
        return new Color(Double.parseDouble(arr[0]),Double.parseDouble(arr[1]),Double.parseDouble(arr[2]));
    }

    private Vector strToVector(String str){
        String[] arr = str.split(" ");
        return new Vector (Double.parseDouble(arr[0]),Double.parseDouble(arr[1]),Double.parseDouble(arr[2]));
    }

}
