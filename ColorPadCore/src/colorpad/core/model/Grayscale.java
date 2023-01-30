package colorpad.core.model;

import colorpad.core.ArgumentOutOfRangeException;
import colorpad.core.Basic;

/**
 * Represents Grayscale of RGB
 * 表示 RGB 的灰度值
 *
 * @see Rgb
 */
public final class Grayscale implements IColorModel {
    private final int value;

    /**
     * Value (0 - 255)
     */
    public int value() {
        return value;
    }

    private Grayscale(int value) {
        this.value = value;
    }

    public Rgb toRgb() {
        return Rgb.from(value(), value(), value());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grayscale grayscale = (Grayscale) o;
        return value == grayscale.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("Grayscale: %s", value());
    }

    @Override
    public String toString(String separator) {
        return String.valueOf(value);
    }

    /**
     * Create the Grayscale model
     * 创建 Grayscale 模型
     *
     * @param g Grayscale value (0 - 255)
     * @return Grayscale
     * @throws ArgumentOutOfRangeException Value out of range / 数值超出范围
     */
    public static Grayscale from(int g) {
        checkRange(g);
        return new Grayscale(g);
    }

    /**
     * Parse the Grayscale value from string
     * 从字符串中解析出灰度值
     *
     * @param color String containing Grayscale / 包含灰度值的字符串
     * @return Grayscale
     * @throws IllegalArgumentException    Unable to parse / 无法解析
     * @throws ArgumentOutOfRangeException Value out of range / 数值超出范围
     */
    public static Grayscale fromString(String color) {
        int[] cm = Basic.extractFromStringAsInt(color);
        if (cm.length != 1) throw new IllegalArgumentException();
        return from(cm[0]);
    }

    private static void checkRange(int g) {
        if (g < 0 || g > 255) throw new ArgumentOutOfRangeException();
    }
}
