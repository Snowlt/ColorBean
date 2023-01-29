package colorpad.core.model;

import colorpad.core.ArgumentOutOfRangeException;

/**
 * CIE-XYZ Color Space Helper
 * CIE-XYZ 颜色空间辅助工具
 *
 * @see Xyz
 */
public class CieXyzHelper {
    private CieXyzHelper() {
    }

    /**
     * Check if X, Y, Z values are within the valid range of D65 illuminant
     * 检查 X, Y, Z 的值是否在 D65 光源的有效范围内
     * <p>X: 0 - 0.95047, Y: 0 - 1.0, Z: 0 - 1.08883</p>
     *
     * @param value CIE-XYZ
     * @return true if in valid range / 在有效范围内则返回 true
     */
    public static boolean checkRangeOfD65(Xyz value) {
        return !(value.x() < 0d || value.x() > 0.95047d || value.y() < 0d || value.y() > 1.0d ||
                value.z() < 0d || value.z() > 1.08883d);
    }

    /**
     * Parse string of d 65 xyz.
     * Parse out the XYZ value from a string, which within the valid range of D65 illuminant
     * 从一个字符串解析出在 D65 光源有效范围内的 XYZ 值
     * <p>X (0 - 0.95047), Y (0 - 1.0), Z (0 - 1.08883)</p>
     *
     * @param color String of XYZ / XYZ 字符串
     * @return CIE-XYZ(D65)
     * @throws IllegalArgumentException    Unable to parse / 无法解析
     * @throws ArgumentOutOfRangeException Value out of range / 数值超出范围
     */
    public static Xyz parseStringOfD65(String color) {
        Xyz value = Xyz.fromString(color);
        if (!checkRangeOfD65(value)) throw new ArgumentOutOfRangeException();
        return value;
    }
}
