package colorpad.core.model;

import colorpad.core.Basic;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * Class of HSL Color Model
 * 表示 HSL 颜色模型的类
 */
public class Hsl implements IColorModel {

    private double h, s, l;

    /**
     * Hue (0 - 360)
     */
    public double h() {
        return h;
    }

    /**
     * Hue (0 - 360)
     */
    public void setH(double h) {
        this.h = Basic.getFixRange(h, 0, 360);
    }

    /**
     * Saturation (0 - 100)
     */
    public double s() {
        return s;
    }

    /**
     * Saturation (0 - 100)
     */
    public void setS(double s) {
        this.s = Basic.getFixRange(s, 0, 100);
    }

    /**
     * Lightness (0 - 100)
     */
    public double l() {
        return l;
    }

    /**
     * Lightness (0 - 100)
     */
    public void setL(double l) {
        this.l = Basic.getFixRange(l, 0, 100);
    }

    /**
     * Initialize the HSL object
     * 初始化 HSL 对象
     *
     * @param h Hue (0 - 360)
     * @param s Saturation (0 - 100)
     * @param l Lightness (0 - 100)
     */
    public Hsl(double h, double s, double l) {
        this.setH(h);
        this.setS(s);
        this.setL(l);
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
        return MessageFormat.format("HSL: ({0},{1},{2})", h(), s(), l());
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
     * Parse out the HSL value from a string
     * 从一个字符串解析出 HSL 值
     *
     * @param color String of HSL / HSL 字符串
     * @return HSL Object / HSL 对象
     */
    public static Hsl fromString(String color) {
        double[] values = Basic.extractFromString(color);
        // 检查格式
        if (values.length != 3)
            return null;
        // 检查范围
        double h = values[0];
        double s = values[1];
        double l = values[2];
        if (h < 0 || h > 360 || s < 0 || s > 100 || l < 0 || l > 100) {
            return null;
        }
        // 返回对象
        return new Hsl(h, s, l);
    }
}
