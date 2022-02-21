package colorpad.core.model;

import colorpad.core.Basic;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * Class of HSB / HSV Color Model
 * 表示 HSB / HSV 颜色模型的类
 */
public class Hsb implements IToString {

    private double h, s, b;

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
     * Brightness / Value (0 - 100)
     */
    public double b() {
        return b;
    }

    /**
     * Brightness / Value (0 - 100)
     */
    public void setB(double b) {
        this.b = Basic.getFixRange(b, 0, 100);
    }

    /**
     * Initialize the HSB / HSV object
     * 初始化 HSB / HSV 对象
     *
     * @param h Hue (0 - 360)
     * @param s Saturation (0 - 100)
     * @param b Brightness / Value (0 - 100)
     */
    public Hsb(double h, double s, double b) {
        this.setH(h);
        this.setS(s);
        this.setB(b);
    }

    public boolean compareWith(double h, double s, double b) {
        return Basic.decimalEquals(h(), h) && Basic.decimalEquals(s(), s) && Basic.decimalEquals(b(), b);
    }

    @Override
    public String toString(String separator) {
        return Basic.decimalFormat(separator, h(), s(), b());
    }

    @Override
    public String toString() {
        return MessageFormat.format("HSB: ({0},{1},{2})", h(), s(), b());
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

    /**
     * Parse out the HSB value from a string
     * 从一个字符串解析出 HSB 值
     *
     * @param color String of HSB / HSB 字符串
     * @return HSB Object / HSB 对象
     */
    public static Hsb fromString(String color) {
        double[] values = Basic.extractFromString(color);
        // 检查格式
        if (values.length != 3)
            return null;
        // 检查范围
        double h = values[0];
        double s = values[1];
        double b = values[2];
        if (h < 0 || h > 360 || s < 0 || s > 100 || b < 0 || b > 100) {
            return null;
        }
        // 返回对象
        return new Hsb(h, s, b);
    }
}
