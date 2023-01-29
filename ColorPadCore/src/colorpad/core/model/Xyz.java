package colorpad.core.model;

import colorpad.core.Basic;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * Represents the values of X, Y, Z in CIE-XYZ Color Space. Usually X, Y, Z ranges from about 0 to 1.0
 * <br>
 * 表示 CIE-XYZ 颜色空间 X, Y, Z 的值。通常 X, Y, Z 的范围大约是 0 到 1.0
 */
public final class Xyz implements IColorModel {

    private final double x, y, z;

    /**
     * Value of X
     */
    public double x() {
        return x;
    }

    /**
     * Value of Y
     */
    public double y() {
        return y;
    }

    /**
     * Value of Z
     */
    public double z() {
        return z;
    }

    /**
     * Initialize the XYZ object
     * 初始化 XYZ 对象
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    private Xyz(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean compareWith(double x, double y, double z) {
        return Basic.decimalEquals(x(), x) && Basic.decimalEquals(y(), y) && Basic.decimalEquals(z(), z);
    }

    @Override
    public String toString(String separator) {
        return Basic.decimalFormat(separator, x(), y(), z());
    }

    @Override
    public String toString() {
        return MessageFormat.format("CIE-XYZ: ({0})", toString(","));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Xyz)) return false;
        Xyz xyz = (Xyz) o;
        return compareWith(xyz.x(), xyz.y(), xyz.z());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x(), y(), z());
    }

    /**
     * Create the XYZ struct(No range limited)
     * 创建 XYZ 结构(无范围限制)
     *
     * @param x X
     * @param y Y
     * @param z Z
     * @return CIE-XYZ
     */
    public static Xyz from(double x, double y, double z) {
        return new Xyz(x, y, z);
    }

    /**
     * Parse out the XYZ value from a string(No range limited)
     * 从一个字符串解析出 XYZ 值(无范围限制)
     *
     * @param color String of XYZ / XYZ 字符串
     * @return CIE-XYZ
     * @throws IllegalArgumentException Unable to parse / 无法解析
     */
    public static Xyz fromString(String color) {
        double[] xyz = Basic.extractFromString(color);
        if (xyz.length != 3) throw new IllegalArgumentException();
        return new Xyz(xyz[0], xyz[1], xyz[2]);
    }
}
