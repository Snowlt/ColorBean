package colorpad.core.model;

import colorpad.core.Basic;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * Class to store the values of X, Y, Z in CIE-XYZ Color Space
 * 用来存储 CIE-XYZ 颜色空间 X, Y, Z 值的类
 */
public class Xyz implements IToString {

    private double x, y, z;

    /**
     * Value of X
     */
    public double x() {
        return x;
    }

    /**
     * Value of X
     */
    public void setX(double x) {
        this.x = replaceXValue(x);
    }

    /**
     * Value of Y
     */
    public double y() {
        return y;
    }

    /**
     * Value of Y
     */
    public void setY(double y) {
        this.y = replaceYValue(y);
    }

    /**
     * Value of Z
     */
    public double z() {
        return z;
    }

    /**
     * Value of Z
     */
    public void setZ(double z) {
        this.z = replaceZValue(z);
    }


    /**
     * Initialize the XYZ object
     * 初始化 XYZ 对象
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    public Xyz(double x, double y, double z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
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
        return MessageFormat.format("CIE-XYZ: ({0},{1},{2})", x(), y(), z());
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
     * Parse out the XYZ value from a string(No range limited)
     * 从一个字符串解析出 XYZ 值(无范围限制)
     *
     * @param color String of XYZ / XYZ 字符串
     * @return CIE-XYZ Object / CIE-XYZ 对象
     */
    public static Xyz fromString(String color) {
        double[] values = Basic.extractFromString(color);
        // 检查格式
        if (values.length != 3)
            return null;
        // 返回对象
        return new Xyz(values[0], values[1], values[2]);
    }

    /**
     * Invoked when setting X. Modify value here if necessary
     *
     * @param value inputted value
     * @return output value
     */
    protected double replaceXValue(double value) {
        return value;
    }

    /**
     * Invoked when setting Y. Modify value here if necessary
     *
     * @param value inputted value
     * @return output value
     */
    protected double replaceYValue(double value) {
        return value;
    }

    /**
     * Invoked when setting Z. Modify value here if necessary
     *
     * @param value inputted value
     * @return output value
     */
    protected double replaceZValue(double value) {
        return value;
    }
}
