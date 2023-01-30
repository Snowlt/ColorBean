package colorpad.core.model;

import colorpad.core.ArgumentOutOfRangeException;
import colorpad.core.Basic;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * Represents HSB / HSV Color Model
 * 表示 HSB / HSV 颜色模型
 */
public final class Hsb implements IColorModel {

    private final double h;
    private final double s;
    private final double b;

    /**
     * Hue (0 - 360)
     */
    public double h() {
        return h;
    }

    /**
     * Saturation (0 - 100)
     */
    public double s() {
        return s;
    }

    /**
     * Brightness / Value (0 - 100)
     */
    public double b() {
        return b;
    }

    /**
     * Initialize the HSB / HSV object
     * 初始化 HSB / HSV 对象
     *
     * @param h Hue (0 - 360)
     * @param s Saturation (0 - 100)
     * @param b Brightness / Value (0 - 100)
     */
    private Hsb(double h, double s, double b) {
        this.h = h;
        this.s = s;
        this.b = b;
    }

    public boolean compareWith(double h, double s, double b) {
        return Basic.decimalEquals(h(), h) && Basic.decimalEquals(s(), s) && Basic.decimalEquals(b(), b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hsb)) return false;
        Hsb hsb = (Hsb) o;
        return compareWith(hsb.h(), hsb.s(), hsb.b());
    }

    @Override
    public int hashCode() {
        return Objects.hash(h(), s(), b());
    }

    @Override
    public String toString() {
        return MessageFormat.format("HSB: ({0})", toString(","));
    }

    @Override
    public String toString(String separator) {
        return Basic.decimalFormat(separator, h(), s(), b());
    }

    /**
     * Create the HSB / HSV model
     * 创建 HSB / HSV 模型
     *
     * @param h Hue (0 - 360)
     * @param s Saturation (0 - 100)
     * @param b Brightness / Value (0 - 100)
     * @return HSB
     * @throws ArgumentOutOfRangeException Value out of range / 数值超出范围
     */
    public static Hsb from(double h, double s, double b) {
        checkRange(h, s, b);
        return new Hsb(h, s, b);
    }

    /**
     * Parse out the HSB value from a string
     * 从一个字符串解析出 HSB 值
     *
     * @param color String of HSB / HSB 字符串
     * @return HSB
     * @throws IllegalArgumentException    Unable to parse / 无法解析
     * @throws ArgumentOutOfRangeException Value out of range / 数值超出范围
     */
    public static Hsb fromString(String color) {
        double[] cm = Basic.extractFromString(color);
        // 检查格式
        if (cm.length != 3) throw new IllegalArgumentException();
        // 检查范围
        double h = cm[0];
        double s = cm[1];
        double b = cm[2];
        checkRange(h, s, b);
        // 返回对象
        return new Hsb(h, s, b);
    }

    private static void checkRange(double h, double s, double b) {
        if (h < 0d || h > 360d || s < 0d || s > 100d || b < 0d || b > 100d)
            throw new ArgumentOutOfRangeException();
    }
}
