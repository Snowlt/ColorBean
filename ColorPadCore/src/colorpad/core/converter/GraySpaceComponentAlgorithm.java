package colorpad.core.converter;

import colorpad.core.model.Grayscale;
import colorpad.core.model.Rgb;

/**
 * Calculate Grayscale of RGB by sRGB space component algorithm
 * 使用 sRGB 分量方式计算 RGB 的灰度值
 */
public class GraySpaceComponentAlgorithm implements IConvertFromTo<Rgb, Grayscale> {
    private int calc(int r, int g, int b) {
        return (int) Math.round((r * 299 + g * 587 + b * 114) / 1000d);
    }

    @Override
    public Grayscale convert(Rgb rgb) {
        return Grayscale.from(calc(rgb.r(), rgb.g(), rgb.b()));
    }
}
