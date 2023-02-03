package colorpad.core.model;

import colorpad.core.ArgumentOutOfRangeException;
import colorpad.core.Basic;

import java.text.MessageFormat;

/**
 * Represents RGB Color Model
 * 表示 RGB 颜色模型
 */
public final class Rgb implements IColorModel {

    private final int r;
    private final int g;
    private final int b;

    /**
     * Pure white 纯白色
     * <p>RGB(255, 255, 255)</p>
     */
    public static final Rgb WHITE = new Rgb(255, 255, 255);

    /**
     * Pure black 纯黑色
     * <p>RGB(0, 0, 0)</p>
     */
    public static final Rgb BLACK = new Rgb(0);

    /**
     * Red (0 - 255)
     */
    public int r() {
        return r;
    }

    /**
     * Green (0 - 255)
     */
    public int g() {
        return g;
    }

    /**
     * Blue (0 - 255)
     */
    public int b() {
        return b;
    }

    /**
     * Initialize the RGB object
     * 初始化 RGB 对象
     *
     * @param r Red (0 - 255)
     * @param g Green (0 - 255)
     * @param b Blue (0 - 255)
     */
    private Rgb(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    Rgb(int value) {
        this.r = (value >>> 16) & 255;
        this.g = (value >>> 8) & 255;
        this.b = value & 255;
    }

    public boolean compareWith(int r, int g, int b) {
        return r() == r && g() == g && b() == b;
    }

    /**
     * Create upper case Hex / HTML string of RGB
     * 生成大写 RGB 的 16进制 / HTML 格式字符串
     *
     * @return Hex of RGB
     */
    public String toHex() {
        return toHex(true);
    }

    /**
     * Create Hex / HTML string of RGB
     * 生成 RGB 的 16进制 / HTML 格式字符串
     *
     * @param upper Upper case / 大写
     * @return RGB Hex
     */
    public String toHex(boolean upper) {
        return Basic.toLengthSixHex(toInteger(), upper);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rgb)) return false;
        Rgb rgb = (Rgb) o;
        return compareWith(rgb.r(), rgb.g(), rgb.b());
    }

    @Override
    public int hashCode() {
        return toInteger();
    }

    @Override
    public String toString() {
        return MessageFormat.format("RGB: ({0},{1},{2})", r(), g(), b());
    }

    @Override
    public String toString(String separator) {
        return MessageFormat.format("{1}{0}{2}{0}{3}", separator, r(), g(), b());
    }

    /**
     * Get the integer value of RGB, equals to decimal value of hex()
     *
     * @return Integer value
     */
    int toInteger() {
        return r() << 16 | g() << 8 | b();
    }

    /**
     * Create the RGB model
     * 创建 RGB 模型
     *
     * @param r Red (0 - 255)
     * @param g Green (0 - 255)
     * @param b Blue (0 - 255)
     * @return RGB
     * @throws ArgumentOutOfRangeException Value out of range / 数值超出范围
     */
    public static Rgb from(int r, int g, int b) {
        checkRange(r, g, b);
        return new Rgb(r, g, b);
    }

    /**
     * Parse out the RGB value from a string
     * 从一个字符串解析出 RGB 值
     *
     * @param color String of RGB / RGB 字符串
     * @return RGB
     * @throws IllegalArgumentException    Unable to parse / 无法解析
     * @throws ArgumentOutOfRangeException Value out of range / 数值超出范围
     */
    public static Rgb fromString(String color) {
        int[] values = Basic.extractFromStringAsInt(color);
        // 检查格式
        if (values.length != 3)
            throw new IllegalArgumentException();
        // 检查范围
        int r = values[0];
        int g = values[1];
        int b = values[2];
        checkRange(r, g, b);
        // 返回对象
        return new Rgb(r, g, b);
    }

    /**
     * Parse out the RGB value from a Hex string
     * 从一个 Hex 字符串中解析出 RGB 值
     *
     * @param color String of Hex / Hex 字符串
     * @return RGB
     * @throws IllegalArgumentException    Unable to parse / 无法解析
     * @throws ArgumentOutOfRangeException Value out of range / 数值超出范围
     */
    public static Rgb fromHex(String color) {
        if (Basic.isEmpty(color))
            throw new IllegalArgumentException();
        // 处理并分割字符串
        if (color.charAt(0) == '#')
            color = color.substring(1);
        // 检查格式
        if (color.length() != 6)
            throw new IllegalArgumentException();
        return new Rgb(Basic.parseHex(color));
    }

    /**
     * Parse out the RGB value from a Hex string(Support incomplete Hex and CSS style)
     * 从一个 Hex 字符串中解析出 RGB 值(支持不完整的 Hex 和 CSS 样式)
     *
     * @param color String of Hex / Hex 字符串
     * @return RGB
     * @throws IllegalArgumentException    Unable to parse / 无法解析
     * @throws ArgumentOutOfRangeException Value out of range / 数值超出范围
     */
    public static Rgb fromHexEnhanced(String color) {
        if (Basic.isEmpty(color))
            throw new IllegalArgumentException();
        // 处理并分割字符串
        if (color.charAt(0) == '#')
            color = color.substring(1);
        // 检查格式
        if (color.length() > 6) {
            // 无效的Hex
            throw new IllegalArgumentException();
        } else if (color.length() == 3) {
            // CSS样式
            StringBuilder builder = new StringBuilder(6);
            for (int i = 0; i < 3; i++) {
                char c = color.charAt(i);
                builder.append(c).append(c);
            }
            color = builder.toString();
        }
        return new Rgb(Basic.parseHex(color));
    }

    private static void checkRange(int r, int g, int b) {
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255)
            throw new ArgumentOutOfRangeException();
    }
}
