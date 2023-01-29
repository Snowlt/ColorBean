package colorpad.core.model;

import colorpad.core.ArgumentOutOfRangeException;
import colorpad.core.Basic;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * Represents HSL Color Model
 * 表示 HSL 颜色模型
 */
public final class Hsl implements IColorModel {

    private final double h, s, l;

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
     * Lightness (0 - 100)
     */
    public double l() {
        return l;
    }

    /**
     * Initialize the HSL object
     * 初始化 HSL 对象
     *
     * @param h Hue (0 - 360)
     * @param s Saturation (0 - 100)
     * @param l Lightness (0 - 100)
     */
    private Hsl(double h, double s, double l) {
        this.h = h;
        this.s = s;
        this.l = l;
    }

    public boolean compareWith(double h, double s, double l) {
        return Basic.decimalEquals(h(), h) && Basic.decimalEquals(s(), s) && Basic.decimalEquals(l(), l);
    }

    @Override
    public String toString(String separator) {
        return Basic.decimalFormat(separator, h(), s(), l());
    }

    @Override
    public String toString() {
        return MessageFormat.format("HSL: ({0})", toString(","));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hsl)) return false;
        Hsl hsl = (Hsl) o;
        return compareWith(hsl.h(), hsl.s(), hsl.l());
    }

    @Override
    public int hashCode() {
        return Objects.hash(h(), s(), l());
    }


    /**
     * Create the HSL model
     * 创建 HSL 模型
     *
     * @param h Hue (0 - 360)
     * @param s Saturation (0 - 100)
     * @param l Lightness (0 - 100)
     * @return HSL
     * @throws ArgumentOutOfRangeException Value out of range / 数值超出范围
     */
    public static Hsl from(double h, double s, double l) {
        checkRange(h, s, l);
        return new Hsl(h, s, l);
    }

    /**
     * Parse out the HSL value from a string
     * 从一个字符串解析出 HSL 值
     *
     * @param color String of HSL / HSL 字符串
     * @return HSL
     * @throws IllegalArgumentException    Unable to parse / 无法解析
     * @throws ArgumentOutOfRangeException Value out of range / 数值超出范围
     */
    public static Hsl fromString(String color) {
        double[] cm = Basic.extractFromString(color);
        // 检查格式
        if (cm.length != 3) throw new IllegalArgumentException();
        // 检查范围
        double h = cm[0];
        double s = cm[1];
        double l = cm[2];
        checkRange(h, s, l);
        // 返回对象
        return new Hsl(h, s, l);
    }

    private static void checkRange(double h, double s, double l) {
        if (h < 0d || h > 360d || s < 0d || s > 100d || l < 0d || l > 100d)
            throw new ArgumentOutOfRangeException();
    }
}
