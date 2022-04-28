package colorpad.core.model;


import colorpad.core.Basic;

import java.text.MessageFormat;

/**
 * Class of CMYK Color Model
 * 表示 CMYK 颜色模型的类
 */
public class Cmyk implements IColorModel {

    private int c, m, y, k;

    /**
     * Cyan (0 - 100)
     */
    public int c() {
        return c;
    }

    /**
     * Cyan (0 - 100)
     */
    public void setC(int c) {
        this.c = Basic.getFixRange(c, 0, 100);
    }

    /**
     * Magenta (0 - 100)
     */
    public int m() {
        return m;
    }

    /**
     * Magenta (0 - 100)
     */
    public void setM(int m) {
        this.m = Basic.getFixRange(m, 0, 100);
    }

    /**
     * Yellow (0 - 100)
     */
    public int y() {
        return y;
    }

    /**
     * Yellow (0 - 100)
     */
    public void setY(int y) {
        this.y = Basic.getFixRange(y, 0, 100);
    }

    /**
     * Key / Black (0 - 100)
     */
    public int k() {
        return k;
    }

    /**
     * Key / Black (0 - 100)
     */
    public void setK(int k) {
        this.k = Basic.getFixRange(k, 0, 100);
    }

    /**
     * Initialize the CMYK object
     * 初始化 CMYK 对象
     *
     * @param c Cyan (0 - 100)
     * @param m Magenta (0 - 100)
     * @param y Yellow (0 - 100)
     * @param k Key / Black (0 - 100)
     */
    public Cmyk(int c, int m, int y, int k) {
        this.setC(c);
        this.setM(m);
        this.setY(y);
        this.setK(k);
    }

    public boolean compareWith(int c, int m, int y, int k) {
        return c() == c && m() == m && y() == y && k() == k;
    }

    @Override
    public String toString(String separator) {
        return MessageFormat.format("{1}{0}{2}{0}{3}{0}{4}", separator, c(), m(), y(), k());
    }

    @Override
    public String toString() {
        return MessageFormat.format("CMYK: ({0},{1},{2},{3})", c(), m(), y(), k());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cmyk)) return false;
        Cmyk cmyk = (Cmyk) o;
        return compareWith(cmyk.c(), cmyk.m(), cmyk.y(), cmyk.k());
    }

    @Override
    public int hashCode() {
        return c() << 24 | m() << 16 | y() << 8 | k();
    }

    /**
     * Parse out the CMYK value from a string
     * 从一个字符串解析出 CMYK 值
     *
     * @param color String of CMYK / CMYK 字符串
     * @return CMYK Object / CMYK 对象
     */
    public static Cmyk fromString(String color) {
        int[] values = Basic.extractFromStringAsInt(color);
        // 检查格式
        if (values.length != 4)
            return null;
        // 检查范围
        int c = values[0];
        int m = values[1];
        int y = values[2];
        int k = values[3];
        if (c < 0 || c > 100 || m < 0 || m > 100 || y < 0 || y > 100 || k < 0 || k > 100) {
            return null;
        }
        // 返回对象
        return new Cmyk(c, m, y, k);
    }

}
