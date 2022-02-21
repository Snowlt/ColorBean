package colorpad.core.algorithm.interfaces;

import colorpad.core.model.Rgb;

/**
 * Calculate grayscale value
 */
public interface IGetGray {
    /**
     * Calculate grayscale value of specified RGB
     *
     * @param r Red (0 - 255)
     * @param g Green (0 - 255)
     * @param b Blue (0 - 255)
     * @return Grayscale
     */
    int calc(int r, int g, int b);

    /**
     * Calculate grayscale value of specified RGB
     *
     * @param rgb RGB color model
     * @return Grayscale
     */
    default int calc(Rgb rgb) {
        return calc(rgb.r(), rgb.g(), rgb.b());
    }
}
