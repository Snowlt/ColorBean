package colorpad.core.model;

import colorpad.core.ArgumentOutOfRangeException;
import colorpad.core.Basic;

import java.text.MessageFormat;

/**
 * Represents CIE-Lab Color Model
 * 表示 CIE-Lab 颜色模型
 */
public final class Lab implements IColorModel {

    private final double l, a, b;

    /**
     * L (0 - 100)
     */
    public double l() {
        return l;
    }

    /**
     * a (-128 - 127)
     */
    public double a() {
        return a;
    }

    /**
     * b (-128 - 127)
     */
    public double b() {
        return b;
    }

    /**
     * Initialize the Lab object
     * 初始化 Lab 对象
     *
     * @param l L (0 - 100)
     * @param a a (-128 - 127)
     * @param b b (-128 - 127)
     */
    private Lab(double l, double a, double b) {
        this.l = l;
        this.a = a;
        this.b = b;
    }

    public boolean compareWith(double l, double a, double b) {
        return Basic.decimalEquals(l(), l) && Basic.decimalEquals(a(), a) && Basic.decimalEquals(b(), b);
    }

    @Override
    public String toString(String separator) {
        return Basic.decimalFormat(separator, l(), a(), b());
    }

    @Override
    public String toString() {
        return MessageFormat.format("CIE-Lab: ({0},{1},{2})", l(), a(), b());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lab)) return false;
        Lab lab = (Lab) o;
        return compareWith(lab.l(), lab.a(), lab.l());
    }

    @Override
    public int hashCode() {
        return (int) (Math.round(l) << 16 | Math.round(a + 128) << 8 | Math.round(b + 128));
    }


    /**
     * Create the CIE-Lab model
     * 创建 CIE-Lab 模型
     *
     * @param l l (0 - 100)
     * @param a a (-128 - 127)
     * @param b b (-128 - 127)
     * @return CIE-Lab
     * @throws ArgumentOutOfRangeException Value out of range / 数值超出范围
     */
    public static Lab from(double l, double a, double b) {
        checkRange(l, a, b);
        return new Lab(l, a, b);
    }

    /**
     * Parse out the CIE-Lab value from a string
     * 从一个字符串解析出 CIE-Lab 值
     *
     * @param color String of CIE-Lab / CIE-Lab 字符串
     * @return CIE-Lab
     * @throws IllegalArgumentException    Unable to parse / 无法解析
     * @throws ArgumentOutOfRangeException Value out of range / 数值超出范围
     */
    public static Lab fromString(String color) {
        double[] cm = Basic.extractFromString(color);
        // 检查格式
        if (cm.length != 3) throw new IllegalArgumentException();
        double l = cm[0];
        double a = cm[1];
        double b = cm[2];
        checkRange(l, a, b);
        return new Lab(l, a, b);
    }

    private static void checkRange(double l, double a, double b) {
        if (l < 0d || l > 100d || a < -128d || a > 127d || b < -128d || b > 127d)
            throw new ArgumentOutOfRangeException();
    }
}
