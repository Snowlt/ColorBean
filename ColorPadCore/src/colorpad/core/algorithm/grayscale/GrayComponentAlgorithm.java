package colorpad.core.algorithm.grayscale;

/**
 * Calculate Grayscale of RGB by sRGB space component algorithm
 */
public class GrayComponentAlgorithm implements IGrayscaleAlgorithm {
    @Override
    public int calc(int r, int g, int b) {
        return (int) Math.round((r * 299 + g * 587 + b * 114) / 1000d);
    }
}
