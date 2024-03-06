package renderer;

import primitives.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.lang.Math;

/**
 * represents the camera
 *
 * @author Elad and Amitay
 */
public class Camera implements Cloneable {
    private Point p0;
    private Vector Vto;
    private Vector Vup;
    private Vector Vright;
    private double width = 0.0;
    private double height = 0.0;
    private double distance = 0.0;

    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;

    public Point getP0() {
        return p0;
    }

    public Vector getVto() {
        return Vto;
    }

    public Vector getVup() {
        return Vup;
    }

    public Vector getVright() {
        return Vright;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDistance() {
        return distance;
    }

    /**
     * center of the view plane
     */
    private Point center;

    /**
     * amount of threads to use for the creation of the image
     */
    private int threadsCount = 1;

    /**
     * Progress percentage printing interval
     */
    private long printInterval = 1;

    private boolean adaptiveSuperSampling = true;

    /**
     * number of rays through each pixel
     */
    private int amountOfSamples = 1;
    /**
     * boolean variable determining whether using antialiasing or not
     */
    private boolean antiAliasing = false;
    /**
     * boolean variable determining whether using circle blackboard in antialiasing or not
     */
    private boolean antiAliasingCircle = false;

    /**
     * distance from view plane to focal plane (depth of field)
     */
    private double focalDistance = 0;

    private Camera() {
    }

    /**
     * Pixel manager for supporting:
     * <ul>
     * <li>multi-threading</li>
     * <li>debug print of progress percentage in Console window/tab</li>
     * <ul>
     */
    private PixelManager pixelManager;


    /**
     * This function renders image's pixel color map from the scene
     * included in the ray tracer object
     *
     * @return the camera object itself
     */
    public Camera renderImage() {
        int nY = imageWriter.getNy();
        int nX = imageWriter.getNx();
        pixelManager = new PixelManager(nY, nX, printInterval);
        if (threadsCount == 0) {
            if (amountOfSamples != 1 && antiAliasing)
                for (int i = 0; i < nY; i += 1)
                    for (int j = 0; j < nX; j += 1)
                        castBeam(nX, nY, j, i, amountOfSamples);
            else
                for (int i = 0; i < nY; i += 1)
                    for (int j = 0; j < nX; j += 1)
                        castRay(nX, nY, j, i);
        } else {//using multi-threading

            var threads = new LinkedList<Thread>(); // list of threads
            while (threadsCount-- > 0) // add appropriate number of threads
                threads.add(new Thread(() -> { // add a thread with its code
                    PixelManager.Pixel pixel; // current pixel(row,col)
                    // allocate pixel(row,col) in loop until there are no more pixels
                    if (amountOfSamples != 1 && antiAliasing)
                        while ((pixel = pixelManager.nextPixel()) != null)
                            // cast ray through pixel (and color it – inside castRay)
                            castBeam(nX, nY, pixel.col(), pixel.row(), amountOfSamples);
                    else
                        while ((pixel = pixelManager.nextPixel()) != null)
                            // cast ray through pixel (and color it – inside castRay)
                            castRay(nX, nY, pixel.col(), pixel.row());
                }));
            // start all the threads
            for (var thread : threads) thread.start();
            // wait until all the threads have finished
            try {
                for (var thread : threads) thread.join();
            } catch (InterruptedException ignore) {
            }
        }
        return this;
    }


    /**
     * returns the builder of the camera
     *
     * @return the builder
     */

    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * construct a ray that goes through a point
     *
     * @param point the point
     * @return the ray
     */
    public Ray constructRay(Point point) {

        return new Ray(p0, point.subtract(p0));
    }

    public Ray constructRay(int nX, int nY, int j, int i) {
        Point center = getCenter(nX, nY, j, i);

        return constructRay(center);
    }

    private Point getCenter(int nX, int nY, int j, int i) {
        double y = -(i - (nY - 1.0) / 2.0) * (height / nY);
        double x = (j - (nX - 1.0) / 2.0) * (width / nX);
        //Pij - center of pixel i,j
        Point Pij;

        if (!Util.isZero(x) && !Util.isZero(y))
            Pij = center.add(Vright.scale(x).add(Vup.scale(y)));
        else if (!Util.isZero(x))
            Pij = center.add(Vright.scale(x));
        else if (!Util.isZero(y))
            Pij = center.add(Vup.scale(y));
        else
            Pij = center;
        return Pij;
    }

    /**
     * rotates the camera on the Vto axis with angle
     *
     * @param angle the angle in degrees
     * @return updated camera
     * @throws IllegalStateException if the direction vectors weren't set yet
     */
    public Camera rotate(double angle) {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        if (Vto == null || Vup == null || Vright == null)
            throw new IllegalStateException("you can't rotate a not fully-built camera");
        Vector upToSin = Vto.crossProduct(Vup).scale(sin);
        Vector toCos = Vup.scale(cos);
        Vup = upToSin.add(toCos).normalize();
        Vright = Vto.crossProduct(Vup);
        return this;
    }

    /**
     * sets the vectors from the camera towards a given point
     *
     * @param Pto the direction point
     * @return updated camera
     * @throws IllegalStateException    if the direction vectors weren't set yet
     * @throws IllegalArgumentException if Pto is null
     */
    public Camera resetDirection(Point p0, Point Pto) {
        if (Vto == null || Vup == null || Vright == null)
            throw new IllegalStateException("you can't rotate a not fully-built camera");
        if (Pto == null)
            throw new IllegalArgumentException("Pto can't be null");
        if (p0 != null)
            this.p0 = p0;
        Vto = Pto.subtract(this.p0).normalize();
        if (Vto.equals(Vector.Y) || Vto.equals(Vector.Y.scale(-1)))
            Vright = Vector.Z;
        else {
            Vright = new Vector(-Vto.getZ() / Vto.getX(), 0, 1).normalize();

        }
        Vup = Vto.crossProduct(Vright);
        return this;
    }

    /**
     * resets the direction to a given pto from the current position of the camera
     *
     * @param Pto
     * @return updated camera
     */
    public Camera resetDirection(Point Pto) {
        return resetDirection(this.p0, Pto);
    }

    /**
     * construct a ray to a pixel and color the pixel in the image
     *
     * @param nX,nY      the resolution
     * @param column,row the pixel's indexes
     */
    private void castRay(int nX, int nY, int column, int row) {
        Color color;
        if (focalDistance > 0) {
            if (width / nX != height / nY)
                throw new IllegalStateException("the pixels must be squared for anti-aliasing");
            color = raysThroughFocus(getCenter(nX, nY, column, row));
            imageWriter.writePixel(column, row, color);
        } else {
            Ray ray = constructRay(nX, nY, column, row);
            color = rayTracer.traceRay(ray, amountOfSamples);
            imageWriter.writePixel(column, row, color);
        }
        pixelManager.pixelDone();
    }

    /**
     * initialize the blackboard on the camera location for depth of field
     */
    private void initializeBlackBoard() {
        camBlackBoard = new Blackboard(p0, Vup, Vright, width / imageWriter.getNx() * 8);
        if(adaptiveSuperSampling)
            camBlackBoard.grid();
        camBlackBoardList = camBlackBoard.getPoints(amountOfSamples);
    }

    private Blackboard camBlackBoard;

    private List<Point> camBlackBoardList;

    /**
     * construct a lot of rays though a pixel and color the pixel in the image (antialiasing)
     *
     * @param nX,nY           the resolution
     * @param column,row      the pixel's index
     * @param amountOfSamples amount of samples for multi-sampling
     * @throws IllegalStateException if the pixels aren't squared
     */
    private void castBeam(int nX, int nY, int column, int row, int amountOfSamples) {
        if (width / nX != height / nY)
            throw new IllegalStateException("the pixels must be squared for anti-aliasing");
        Blackboard blackboard = new Blackboard(getCenter(nX, nY, column, row), Vup, Vright, width / nX).setCircle(antiAliasingCircle);

        Color color = Color.BLACK;

        if (adaptiveSuperSampling) {
            blackboard.grid();
            List<Point> list = blackboard.getPoints(amountOfSamples);
            ArrayList<ColoredPoint> coloredPoints= new ArrayList<ColoredPoint>();
            for (int i =0 ; i< amountOfSamples*amountOfSamples;i++)
                coloredPoints.add(new ColoredPoint(list.get(i),null));
            color = adaptiveSuperSampling(coloredPoints, amountOfSamples, 0, 0, false);
        } else {
            List<Point> list = blackboard.getPoints(amountOfSamples);
            for (Point point : list) {
                color = color.add(color(point));
            }
            color = color.reduce(amountOfSamples * amountOfSamples);
        }


        imageWriter.writePixel(column, row, color);
        pixelManager.pixelDone();
    }

    /**
     * returns the corners of the sub pixel which its upper left corner is the pixel (indexOnRow,IndexOnColumn) and its length is lengthOfRow
     *
     * @param lengthOfRow   the length of the square's edge
     * @param indexOnRow    the index of the upper left corner on the row
     * @param indexOnColumn the index of the upper left corner on the column
     * @return the corners of the sub pixel which its upper left corner is the pixel (indexOnRow,IndexOnColumn) and its length is lengthOfRow
     */
    private List<Integer> corners(int lengthOfRow, int indexOnRow, int indexOnColumn) {
        // return List.of(0,lengthOfRow-1,lengthOfRow*(lengthOfRow-1),lengthOfRow*lengthOfRow-1);
        return List.of(indexOnRow + indexOnColumn * amountOfSamples,
                indexOnRow + lengthOfRow - 1 + indexOnColumn * amountOfSamples,
                indexOnRow + (indexOnColumn + lengthOfRow - 1) * amountOfSamples,
                indexOnRow + lengthOfRow - 1 + (indexOnColumn + lengthOfRow - 1) * amountOfSamples
        );
    }

    private Color color(Point point) {
        if (focalDistance > 0)
            return raysThroughFocus(point);
        return traceToPoint(point);
    }

    private Color traceToPoint(Point point){
        return rayTracer.traceRay(constructRay(point), amountOfSamples);
    }

    private Color adaptiveSuperSampling(ArrayList<ColoredPoint> list, int lengthOfRow, int indexOnRow, int indexOnColumn, boolean focusing) {
        List<Integer> corners = corners(lengthOfRow, indexOnRow, indexOnColumn);
        boolean sameColor = true;
        for (int i = 0; i < corners.size(); i++) {
            if (list.get(corners.get(i)).color == null)
                list.set(corners.get(i),new ColoredPoint(list.get(corners.get(i)).point, focusing? traceToPoint(list.get(corners.get(i)).point):color(list.get(corners.get(i)).point)));
            if (i != 0)
                if (!list.get(corners.get(i)).color.equals(list.get(corners.get(i-1)).color))
                    sameColor = false;
        }

        if (sameColor)
            return list.get(corners.get(0)).color;

        Color color = Color.BLACK;
        if (lengthOfRow > 2)
            color = color.add(adaptiveSuperSampling(list, lengthOfRow / 2 + 1, indexOnRow, indexOnColumn, focusing)
                    .add(adaptiveSuperSampling(list, lengthOfRow / 2 + 1, indexOnRow + lengthOfRow / 2, indexOnColumn, focusing))
                    .add(adaptiveSuperSampling(list, lengthOfRow / 2 + 1, indexOnRow, indexOnColumn + lengthOfRow / 2, focusing))
                    .add(adaptiveSuperSampling(list, lengthOfRow / 2 + 1, indexOnRow + lengthOfRow / 2, indexOnColumn + lengthOfRow / 2, focusing)));
        else
            for (int i = 0; i < 4; i++)
                color = color.add(list.get(corners.get(i)).color);


        return color.reduce(4);
    }

    public class ColoredPoint {
        public Point point;
        public Color color;

        public ColoredPoint(Point point, Color color) {
            this.point = point;
            this.color = color;
        }
    }

    /**
     * calculates the color of a pixel by multi-sampling, depth of field
     *
     * @param point
     * @return
     */
    private Color raysThroughFocus(Point point) {
        int nX = imageWriter.getNx();

        Point focalPoint = point.add(Vto.scale(focalDistance));
        Color color = Color.BLACK;

        if(adaptiveSuperSampling){
            ArrayList<ColoredPoint> coloredPoints= new ArrayList<ColoredPoint>();
            for (int i =0 ; i< amountOfSamples*amountOfSamples;i++)
                coloredPoints.add(new ColoredPoint(camBlackBoardList.get(i),null));
            color = adaptiveSuperSampling(coloredPoints, amountOfSamples, 0, 0, true );
            return color;
        }
        Ray ray;
        for (Point point1 : camBlackBoardList) {
            ray = new Ray(point1, focalPoint.subtract(point1));
            color = color.add(rayTracer.traceRay(ray, amountOfSamples));
        }
        return color.reduce(amountOfSamples * amountOfSamples);
    }

    /**
     * prints a grid in a given color and a given interval on the image
     *
     * @param interval the interval
     * @param color    the grid's color
     */
    public Camera printGrid(int interval, Color color) {
        int ny = imageWriter.getNy();
        int nx = imageWriter.getNx();

        for (int i = 0; i < ny; i += 1)
            for (int j = 0; j < nx; j += 50)
                imageWriter.writePixel(j, i, color);

        for (int i = 0; i < ny; i += 50)
            for (int j = 0; j < nx; j += 1)
                imageWriter.writePixel(j, i, color);

        return this;
    }


    /**
     * writes to image
     */
    public Camera writeToImage() {
        imageWriter.writeToImage();
        return this;
    }

    /**
     * the builder of the camera
     *
     * @author Elad and Amitay
     */
    public static class Builder {
        private final Camera camera = new Camera();
        private Point Pto = null;

        /**
         * sets the location of the camera
         *
         * @param p0 the location
         * @return the updated builder
         */
        public Builder setLocation(Point p0) {
            camera.p0 = p0;
            return this;
        }

        /**
         * sets the vectors from the camera (to, up)
         *
         * @param Vto the vector to the view
         * @param Vup the vector up
         * @return the updated builder
         * @throws IllegalArgumentException if Vto and Vup aren't orthogonal
         */
        public Builder setDirection(Vector Vto, Vector Vup) {
            if (!Util.isZero(Vto.dotProduct(Vup)))
                throw new IllegalArgumentException("Vto and Vup must be orthogonal");
            camera.Vto = Vto.normalize();
            camera.Vup = Vup.normalize();
            return this;
        }

        /**
         * sets the vectors from the camera towards a given point
         *
         * @param Pto the direction point
         * @param Vup the general up direction (of view plane)
         * @return the updated builder
         */
        public Builder setDirection(Point Pto, Vector Vup) {
            camera.Vup = Vup;
            this.Pto = Pto;
            return this;
        }

        /**
         * set the width and the height of the view plane
         *
         * @param width  the width
         * @param height the height
         * @return the updated builder
         */
        public Builder setVpSize(double width, double height) {
            camera.width = width;
            camera.height = height;
            return this;
        }


        /**
         * set the distance from the camera to the view plane
         *
         * @param distance the distance
         * @return the updated builder
         */
        public Builder setVpDistance(double distance) {
            camera.distance = distance;
            return this;
        }

        /**
         * sets the image writer of the camera
         *
         * @param imageWriter the image writer
         * @return the updated builder
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter = imageWriter;
            return this;
        }

        /**
         * sets the ray tracer of the camera
         *
         * @param rayTracer the ray tracer
         * @return the updated builder
         */
        public Builder setRayTracer(RayTracerBase rayTracer) {
            camera.rayTracer = rayTracer;
            return this;
        }

        /**
         * sets the amount of threads used to create the image
         *
         * @param threadsCount the amount of threads
         * @return the updated builder
         */
        public Builder setThreadsCount(int threadsCount) {
            camera.threadsCount = threadsCount;
            return this;
        }

        /**
         * sets the progress percentage printing interval
         *
         * @param printInterval the interval in seconds
         * @return the updated builder
         */
        public Builder setPrintInterval(int printInterval) {
            camera.printInterval = printInterval;
            return this;
        }

        /**
         * turns off adaptive super sampling
         *
         * @return the updated builder
         */
        public Builder turnAdaptiveSuperSamplingOff() {
            camera.adaptiveSuperSampling = false;
            return this;
        }

        public Builder setAmountOfSamples(int amountOfSamples) {
            camera.amountOfSamples = amountOfSamples;
            return this;
        }

        public Builder antiAliasing() {
            camera.antiAliasing = true;
            return this;
        }

        public Builder antiAliasingCircle() {
            camera.antiAliasingCircle = true;
            return this;
        }

        public Builder setFocalDistance(double focalDistance) {
            if (focalDistance < 0)
                throw new IllegalArgumentException("focal plane can't be behind the view plane");
            camera.focalDistance = focalDistance;
            return this;
        }

        /**
         * checks that all of camera's fields are well assigned
         *
         * @return a clone of the object with valid values
         * @throws MissingResourceException if a field is null or zero
         * @throws IllegalCallerException   if the vectors aren't orthogonal or aren't normalized
         */
        public Camera build() {
            //const strings for exceptions throwing
            final String MISSING_RENDERING_ARGUMENT = "Missing rendering argument";
            final String CAMERA = "Camera";
            final String MUST_BE_NORMALIZED = " must be normalized";
            //if a field is null
            if (camera.p0 == null)
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT, CAMERA, "p0");
            if (Pto == null && camera.Vto == null)
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT, CAMERA, "direction (Vto or Pto)");
            if (camera.Vup == null)
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT, CAMERA, "Vup");
            if (camera.imageWriter == null)
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT, CAMERA, "imageWriter");
            if (camera.rayTracer == null)
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT, CAMERA, "rayTracer");
            // check if Vto and Vup are orthogonal
            if (Pto == null && !Util.isZero(camera.Vto.dotProduct(camera.Vup)))
                throw new IllegalArgumentException("Vto and Vup are not orthogonal");
            // check if Vup and Vto are normalized
            if (Pto == null && !Util.isZero(camera.Vto.lengthSquared() - 1))
                throw new IllegalArgumentException("Vto" + MUST_BE_NORMALIZED);
            if (!Util.isZero(camera.Vup.lengthSquared() - 1))
                throw new IllegalArgumentException("Vup" + MUST_BE_NORMALIZED);
            //check if Pto equals P0
            if (camera.p0.equals(Pto))
                throw new IllegalArgumentException("Pto must be different from P0");
            // setting Vright and center (Vto optionally)
            if (Pto != null)
                camera.Vto = Pto.subtract(camera.p0).normalize();

            camera.Vright = camera.Vto.crossProduct(camera.Vup);

            camera.center = camera.p0.add(camera.Vto.scale(camera.distance));
            //if a field is zero
            if (Util.isZero(camera.width))
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT, CAMERA, "width");
            if (Util.isZero(camera.height))
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT, CAMERA, "height");
            if (Util.isZero(camera.distance))
                throw new MissingResourceException(MISSING_RENDERING_ARGUMENT, CAMERA, "distance");

            if(camera.focalDistance>0)
                camera.initializeBlackBoard();

            // return clone of the object
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
