package colorpad.core.model;


import colorpad.core.ArgumentOutOfRangeException;
import colorpad.core.Basic;

import java.text.MessageFormat;

/**
 * Represents CMYK Color Model
 * 表示 CMYK 颜色模型
 */
public final class Cmyk implements IColorModel {

    private final int c;
    private final int m;
    private final int y;
    private final int k;

    /**
     * Cyan (0 - 100)
     */
    public int c() {
        return c;
    }

    /**
     * Magenta (0 - 100)
     */
    public int m() {
        return m;
    }

    /**
     * Yellow (0 - 100)
     */
    public int y() {
        return y;
    }

    /**
     * Key / Black (0 - 100)
     */
    public int k() {
        return k;
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
        this.c = c;
        this.m = m;
        this.y = y;
        this.k = k;
    }

    public boolean compareWith(int c, int m, int y, int k) {
        return c() == c && m() == m && y() == y && k() == k;
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

    @Override
    public String toString() {
        return MessageFormat.format("CMYK: ({0},{1},{2},{3})", c(), m(), y(), k());
    }

    @Override
    public String toString(String separator) {
        return MessageFormat.format("{1}{0}{2}{0}{3}{0}{4}", separator, c(), m(), y(), k());
    }


    /**
     * Create the CMYK model
     * 创建 CMYK 模型
     *
     * @param c Cyan (0 - 100)
     * @param m Magenta (0 - 100)
     * @param y Yellow (0 - 100)
     * @param k Key / Black (0 - 100)
     * @return CMYK
     * @throws ArgumentOutOfRangeException Value out of range / 数值超出范围
     */
    public static Cmyk from(int c, int m, int y, int k) {
        checkRange(c, m, y, k);
        return new Cmyk(c, m, y, k);
    }

    /**
     * Parse out the CMYK value from a string
     * 从一个字符串解析出 CMYK 值
     *
     * @param color String of CMYK / CMYK 字符串
     * @return CMYK
     * @throws IllegalArgumentException    Unable to parse / 无法解析
     * @throws ArgumentOutOfRangeException Value out of range / 数值超出范围
     */
    public static Cmyk fromString(String color) {
        int[] cm = Basic.extractFromStringAsInt(color);
        if (cm.length != 4) throw new IllegalArgumentException();
        checkRange(cm[0], cm[1], cm[2], cm[3]);
        return new Cmyk(cm[0], cm[1], cm[2], cm[3]);
    }

    private static void checkRange(int c, int m, int y, int k) {
        if (c < 0 || c > 100 || m < 0 || m > 100 || y < 0 || y > 100 || k < 0 || k > 100)
            throw new ArgumentOutOfRangeException();
    }

}
