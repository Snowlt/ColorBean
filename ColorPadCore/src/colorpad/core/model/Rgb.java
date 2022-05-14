package colorpad.core.model;

import colorpad.core.Basic;

import java.text.MessageFormat;

/**
 * Class of RGB Color Model
 * 表示 RGB 颜色模型的类
 */
public class Rgb implements IColorModel {

    private int r, g, b;

    /**
     * Red (0 - 255)
     */
    public int r() {
        return r;
    }

    /**
     * Red (0 - 255)
     */
    public void setR(int r) {
        this.r = Basic.getFixRange(r, 0, 255);
    }

    /**
     * Green (0 - 255)
     */
    public int g() {
        return g;
    }

    /**
     * Green (0 - 255)
     */
    public void setG(int g) {
        this.g = Basic.getFixRange(g, 0, 255);
    }

    /**
     * Blue (0 - 255)
     */
    public int b() {
        return b;
    }

    /**
     * Blue (0 - 255)
     */
    public void setB(int b) {
        this.b = Basic.getFixRange(b, 0, 255);
    }

    /**
     * Initialize the RGB object
     * 初始化 RGB 对象
     *
     * @param r Red (0 - 255)
     * @param g Green (0 - 255)
     * @param b Blue (0 - 255)
     */
    public Rgb(int r, int g, int b) {
        this.setR(r);
        this.setG(g);
        this.setB(b);
    }

    Rgb(int value) {
        int r, g, b;
        r = (value >>> 16) & 255;
        g = (value >>> 8) & 255;
        b = value & 255;
        this.setR(r);
        this.setG(g);
        this.setB(b);
    }

    public boolean compareWith(int r, int g, int b) {
        return r() == r && g() == g && b() == b;
    }

    public String toHex() {
        return Integer.toHexString(toInteger()).toUpperCase();
    }

    @Override
    public String toString(String separator) {
        return MessageFormat.format("{1}{0}{2}{0}{3}", separator, r(), g(), b());
    }

    @Override
    public String toString() {
        return MessageFormat.format("RGB: ({0},{1},{2})", r(), g(), b());
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

    /**
     * Get the integer value of RGB, equals to decimal value of hex()
     *
     * @return Integer value
     */
    int toInteger() {
        return r() << 16 | g() << 8 | b();
    }

    /**
     * Parse out the RGB value from a string
     * 从一个字符串解析出 RGB 值
     *
     * @param color String of RGB / RGB 字符串
     * @return RGB Object / RGB 对象
     */
    public static Rgb fromString(String color) {
        int[] values = Basic.extractFromStringAsInt(color);
        // 检查格式
        if (values.length != 3)
            return null;
        // 检查范围
        int r = values[0];
        int g = values[1];
        int b = values[2];
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            return null;
        }
        // 返回对象
        return new Rgb(r, g, b);
    }

    /**
     * Parse out the RGB value from a Hex string
     * 从一个 Hex 字符串中解析出 RGB 值
     *
     * @param color String of Hex / Hex 字符串
     * @return RGB Object / RGB 对象
     */
    public static Rgb fromHex(String color) {
        if (Basic.isEmpty(color))
            return null;
        // 处理并分割字符串
        if (color.charAt(0) == '#')
            color = color.substring(1);
        // 检查格式
        if (color.length() != 6)
            return null;
        return new Rgb(Basic.parseHex(color));
    }

    /**
     * Parse out the RGB value from a Hex string(Support incomplete Hex and CSS style)
     * 从一个 Hex 字符串中解析出 RGB 值(支持不完整的 Hex 和 CSS 样式)
     *
     * @param color String of Hex / Hex 字符串
     * @return RGB Object / RGB 对象
     */
    public static Rgb fromHexEnhanced(String color) {
        if (Basic.isEmpty(color))
            return null;
        // 处理并分割字符串
        if (color.charAt(0) == '#')
            color = color.substring(1);
        // 检查格式
        if (color.length() > 6) {
            // 无效的Hex
            return null;
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

    /**
     * Generate the RGB value from a string containing Grayscale
     * 从一个包含灰度值的字符串中生成 RGB 值
     *
     * @param color String contains Grayscale / 包含灰度值的字符串
     * @return RGB Object / RGB 对象
     */
    public static Rgb fromGray(String color) {
        if (Basic.isEmpty(color))
            return null;
        int g = Basic.fIntValue(color);
        if (g < 0 || g > 255)
            return null;
        return new Rgb(g, g, g);
    }

}
