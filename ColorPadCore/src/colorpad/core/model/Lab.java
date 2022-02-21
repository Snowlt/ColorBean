package colorpad.core.model;

import colorpad.core.Basic;

import java.text.MessageFormat;

/**
 * Class of CIE-Lab Color Model
 * 表示 CIE-Lab 颜色模型的类
 */
public class Lab implements IToString {

    private double l, a, b;

    /**
     * L (0 - 100)
     */
    public double l() {
        return l;
    }

    /**
     * L (0 - 100)
     */
    public void setL(double l) {
        this.l = Basic.getFixRange(l, 0, 100);
    }

    /**
     * a (-128 - 127)
     */
    public double a() {
        return a;
    }

    /**
     * a (-128 - 127)
     */
    public void setA(double a) {
        this.a = Basic.getFixRange(a, -128, 127);
    }

    /**
     * b (-128 - 127)
     */
    public double b() {
        return b;
    }

    /**
     * b (-128 - 127)
     */
    public void setB(double b) {
        this.b = Basic.getFixRange(b, -128, 127);
    }

    /**
     * Initialize the Lab object
     * 初始化 Lab 对象
     *
     * @param l L (0 - 100)
     * @param a a (-128 - 127)
     * @param b b (-128 - 127)
     */
    public Lab(double l, double a, double b) {
        this.setL(l);
        this.setA(a);
        this.setB(b);
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
     * Parse out the Lab value from a string
     * 从一个字符串解析出 Lab 值
     *
     * @param color String of Lab / Lab 字符串
     * @return Lab Object / Lab 对象
     */
    public static Lab fromString(String color) {
        double[] values = Basic.extractFromString(color);
        // 检查格式
        if (values.length != 3)
            return null;
        // 检查范围
        double l = values[0];
        double a = values[1];
        double b = values[2];
        if (l < 0 || l > 100 || a < -128 || a > 127 || b < -128 || b > 127) {
            return null;
        }
        // 返回对象
        return new Lab(l, a, b);
    }
}
