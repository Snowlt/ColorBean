package colorpad.core.algorithm.grayscale;

/**
 * Calculate Grayscale of RGB by means of average value
 */
public class GrayAverageAlgorithm implements IGrayscaleAlgorithm {
    @Override
    public int calc(int r, int g, int b) {
        return (int) Math.round((r + g + b) / 3d);
    }
}
