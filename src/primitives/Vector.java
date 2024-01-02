package primitives;

/**
 * this class will represent vectors
 * @author Elad and Amitay
 */
public class Vector extends Point {
    /**
     * constructor that gets 3 doubles and make the matching vector
     *
     * @param d1 first number value
     * @param d2 second number value
     * @param d3 third number value
     * @throws IllegalArgumentException if the given numbers are all zeros
     */
    public Vector(double d1, double d2, double d3) {
        super(d1, d2, d3);
        if (Util.isZero(d1) && Util.isZero(d2) && Util.isZero(d3))
            throw new IllegalArgumentException("the zero vector is illegal to use");
    }


    /**
     * this constructor gets Double3 and assign it as the vector
     *
     * @param xyz the value of the vector
     * @throws IllegalArgumentException if the given Double3 is the Double3.ZERO
     */
     Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("the zero vector is illegal to use");
    }

    /**
     * Add two vectors into a new point by adding their Double3 fields
     *
     * @param vector right hand side operand for addition
     * @return result of addition
     */

    @Override
    public Vector add(Vector vector) {
        return new Vector(xyz.add(vector.xyz));
    }


    /**
     * multiply the vector by a scalar
     *
     * @param scalar is the scalar
     * @return result of scaling
     */

    public Vector scale(double scalar) {
        return new Vector(xyz.scale((scalar)));
    }

    /**
     * multiply the vector with another vector
     *
     * @param vector is the other vector
     * @return result of multiplying
     */
    public double dotProduct(Vector vector) {
        return vector.xyz.d1 * xyz.d1 + vector.xyz.d2 * xyz.d2 + vector.xyz.d3 * xyz.d3;
    }

    /**
     * multiply the vector with another vector
     *
     * @param vector is the other vector
     * @return result of multiplying
     */
    public Vector crossProduct(Vector vector) {
        return new Vector(xyz.d2 * vector.xyz.d3 - xyz.d3 * vector.xyz.d2, xyz.d3 * vector.xyz.d1 - xyz.d1 * vector.xyz.d3, xyz.d1 * vector.xyz.d2 - xyz.d2 * vector.xyz.d1);
    }

    /**
     * returns the dot product of the vector with itself which is the length of the vector squared
     *
     * @return length of the vector squared
     */
    public double lengthSquared() {
        return dotProduct(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof Vector other && super.equals(other);
    }

    /**
     * returns the square root of the lengthSquared of the vector which is the length of the vector
     *
     * @return length of the vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * returns the vector normalized by scaling it with its length reversed
     *
     * @return the vector normalized
     */
    public Vector normalize() {
        return scale(1 / length());
    }

    @Override
    public String toString() {
        return "->" + super.toString();
    }

}
