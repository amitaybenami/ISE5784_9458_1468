package lighting;

import primitives.Color;
import primitives.Double3;
/**
 * represents the lightning at the scene
 * @author Elad and Amitay
 */
public class AmbientLight extends Light{
    /**
     * constructor that gets intensity and Double3 type attenuation coefficient
     * @param intensity
     * @param kA Double3 type attenuation coefficient
     */
    public AmbientLight(Color intensity, Double3 kA){
        super(intensity.scale(kA));
    }
    /**
     * constructor that gets intensity and Double type attenuation coefficient
     * @param intensity
     * @param kA Double type attenuation coefficient
     */
    public AmbientLight(Color intensity, Double kA){
        super(intensity.scale(kA));
    }


    //solid black lightning
    public final static AmbientLight NONE = new AmbientLight(Color.BLACK,0d);

}
