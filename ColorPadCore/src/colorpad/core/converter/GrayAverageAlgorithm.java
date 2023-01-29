package colorpad.core.converter;

import colorpad.core.model.Grayscale;
import colorpad.core.model.Rgb;

/**
 * Calculate Grayscale of RGB by means of average value
 * 使用平均值方式计算 RGB 的灰度值
 */
public class GrayAverageAlgorithm implements IConvertFromTo<Rgb, Grayscale> {
    private int calc(int r, int g, int b) {
        return (int) Math.round((r + g + b) / 3d);
    }

    @Override
    public Grayscale convert(Rgb rgb) {
        return Grayscale.from(calc(rgb.r(), rgb.g(), rgb.b()));
    }
}
