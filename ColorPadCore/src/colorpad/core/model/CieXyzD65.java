package colorpad.core.model;

import colorpad.core.Basic;

/**
 * CIE-XYZ Color Space (Illuminant = D65)
 * CIE-XYZ 颜色空间(Illuminant = D65)
 */
public class CieXyzD65 extends Xyz {
    /**
     * Initialize the XYZ object (range limited)
     * 初始化 XYZ 对象(有范围限制)
     *
     * @param x X (0 - 0.95047)
     * @param y Y (0 - 1.000)
     * @param z Z (0 - 1.08883)
     */
    public CieXyzD65(double x, double y, double z) {
        super(x, y, z);
    }

    /**
     * Parse out the XYZ value from a string(Range limited)
     * 从一个字符串解析出 XYZ 值(有范围限制)
     * X (0 - 0.95047), Y (0 - 1.0), Z (0 - 1.08883)
     *
     * @param color String of XYZ / XYZ 字符串
     * @return CIE-XYZ D65 Object / CIE-XYZ D65 对象
     */
    public static Xyz fromString(String color) {
        double[] xyz = Basic.extractFromString(color);
        if (xyz.length != 3)
            return null;
        // 检查范围
        double x = xyz[0];
        double y = xyz[1];
        double z = xyz[2];
        // X: 0 to 0.95047
        // Y: 0 to 1.00000
        // Z: 0 to 1.08883
        if (x < 0d || x > 0.95047d || y < 0d || y > 1.0d || z < 0d || z > 1.08883d) {
            return null;
        }
        // 返回对象
        return new Xyz(x, y, z);
    }

    @Override
    protected double replaceXValue(double value) {
        return Basic.getFixRange(value, 0d, 0.95047d);
    }

    @Override
    protected double replaceYValue(double value) {
        return Basic.getFixRange(value, 0d, 1d);
    }

    @Override
    protected double replaceZValue(double value) {
        return Basic.getFixRange(value, 0d, 1.08883d);
    }
}
