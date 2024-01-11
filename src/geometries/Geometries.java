package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

/**
 * this class is used to create a list of geometries
 * @author Elad and Amitay
 */
public class Geometries implements Intersectable{
    //the list itself
    private final List<Intersectable> geometries = new LinkedList<Intersectable>();

    /**
     * default constructor does nothing
     */
    public Geometries() {
    }

    /**
     * constructor that gets array of geometries and assign it as the list
     * @param geometries an array of geometries
     */
    public Geometries(Intersectable... geometries){
        add(geometries);
    }

    /**
     * gets an array of geometries and ass it to the list
     * @param geometries an array of geometries
     */
    public void add(Intersectable... geometries){
        this.geometries.addAll(List.of(geometries));
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> list = null;
        for (Intersectable intersectable : geometries){
            if (intersectable.findIntersections(ray) != null)
                if (list == null)
                    list = new LinkedList<Point>(intersectable.findIntersections(ray));
                else
                    list.addAll(intersectable.findIntersections(ray));
        }
        return list;
    }

}
