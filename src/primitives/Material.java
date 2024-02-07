package primitives;

/**
 * PDS holds factors of materials
 * @author Elad and Amitay
 */
public class Material {
    /**
     * factor for diffusive element of light propagation
     */
    public Double3 kD = Double3.ZERO;
    /**
     * factor for specular element of light propagation
     */
    public Double3 kS = Double3.ZERO;
    /**
     * factor for shininess narrowness of light propagation
     */
    public int nShininess = 1;
    /**
     * factor for transparency
     */
    public Double3 kT = Double3.ZERO;
    /**
     * factor for reflection
     */
    public Double3 kR = Double3.ZERO;

    /**
     * builder template setter for kD
     * @param kD a Double3 value
     * @return the updated material
     */
    public Material setKd(Double3 kD) {
        this.kD = kD;
        return this;
    }
    /**
     * builder template setter for kS
     * @param kS a Double3 value
     * @return the updated material
     */
    public Material setKs(Double3 kS) {
        this.kS = kS;
        return this;
    }
    /**
     * builder template setter for kD
     * @param kD a double value
     * @return the updated material
     */
    public Material setKd(double kD) {
        this.kD = new Double3(kD);
        return this;
    }
    /**
     * builder template setter for kS
     * @param kS a double value
     * @return the updated material
     */
    public Material setKs(double kS) {
        this.kS = new Double3(kS);
        return this;
    }
    /**
     * builder template setter for nShininess
     * @param nShininess an integer value
     * @return the updated material
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
    /**
     * builder template setter for kT
     * @param kT a Double3 value
     * @return the updated material
     */
    public Material setKt(Double3 kT) {
        this.kT = kT;
        return this;
    }
    /**
     * builder template setter for kR
     * @param kR a Double3 value
     * @return the updated material
     */
    public Material setKr(Double3 kR) {
        this.kR = kR;
        return this;
    }
    /**
     * builder template setter for kT
     * @param kT a double value
     * @return the updated material
     */
    public Material setKt(double kT) {
        this.kT = new Double3(kT);
        return this;
    }
    /**
     * builder template setter for kR
     * @param kR a double value
     * @return the updated material
     */
    public Material setKr(double kR) {
        this.kR = new Double3(kR);
        return this;
    }
}
