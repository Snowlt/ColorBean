package colorpad.extend;

import colorpad.core.model.*;

/**
 * 颜色模型转换桥接工具
 *
 * @author Snow
 */
public interface IConvertBridge {

    // Getter

    public Rgb getRgb();

    public Hsb getHsb();

    public Hsl getHsl();

    public Cmyk getCmyk();

    public Xyz getXyz();

    public Lab getLab();

    public YCrCb getYCrCb();

    Grayscale getGrayscale();


    /**
     * Create upper case Hex / HTML string of RGB
     * 生成大写 RGB 的 16进制 / HTML 格式字符串
     *
     * @return Hex of RGB
     */
    default String toHex() {
        return getRgb().toHex(true);
    }

    /**
     * Create Hex / HTML string of RGB
     * 生成 RGB 的 16进制 / HTML 格式字符串
     *
     * @param upper Upper case / 大写
     * @return Hex of RGB
     */
    default String toHex(boolean upper) {
        return getRgb().toHex(upper);
    }
}
