package colorpad.core.algorithm;

import colorpad.core.algorithm.interfaces.IGetGray;

/**
 * Calculate Grayscale of RGB by means of average value
 */
public class GrayAverageAlgorithm implements IGetGray {
    @Override
    public int calc(int r, int g, int b) {
        return (int) Math.round((r + g + b) / 3d);
    }
}
