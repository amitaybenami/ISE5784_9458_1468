package lighting;

import primitives.Color;

/**
 * represents a light
 * @author Elad and Amitay
 */
abstract class Light {
    /**
     * the intensity of the light
     */
    protected Color intensity;

    public Color getIntensity() {
        return intensity;
    }

    /**
     * simple constructor
     * @param intensity
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }
}
