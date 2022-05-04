package colorpad.extend;

import colorpad.core.ModelsManager;
import colorpad.core.model.*;

/**
 * 抽象的颜色模型转换桥接工具
 *
 * @author Snow
 */
public abstract class ConvertBridge {

    protected Rgb rgb;
    protected Hsb hsb;
    protected Hsl hsl;
    protected Cmyk cmyk;
    protected Xyz xyz;
    protected Lab lab;
    protected YCrCb yCrCb;

    protected ConvertBridge() {
    }

    public String toHex() {
        return getRgb().toHex();

    }

    public int getGray() {
        return ModelsManager.getGrayscaleAlgorithm().calc(getRgb());
    }

    protected void SetRgbFrom(Object source) {
        rgb = ModelsManager.convert(source, Rgb.class);
    }

    // Getter & Setter

    public Rgb getRgb() {
        return rgb;
    }

    public Hsb getHsb() {
        return hsb;
    }

    public Hsl getHsl() {
        return hsl;
    }

    public Cmyk getCmyk() {
        return cmyk;
    }

    public Xyz getXyz() {
        return xyz;
    }

    public Lab getLab() {
        return lab;
    }

    public YCrCb getYCrCb() {
        return yCrCb;
    }
}
