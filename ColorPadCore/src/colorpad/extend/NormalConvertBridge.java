package colorpad.extend;

import colorpad.core.ModelsManager;
import colorpad.core.model.*;

import java.util.Objects;

import static colorpad.core.ModelsManager.convert;

/**
 * 颜色模型转换桥接工具
 *
 * @author Snow
 */
public class NormalConvertBridge implements IConvertBridge {
    protected Rgb rgb;
    protected Hsb hsb;
    protected Hsl hsl;
    protected Cmyk cmyk;
    protected Xyz xyz;
    protected Lab lab;
    protected YCrCb yCrCb;
    protected Grayscale grayscale;

    public NormalConvertBridge(Rgb model) {
        Objects.requireNonNull(model, "model cannot be null");
        rgb = model;
        grayscale = convert(model, Grayscale.class);
        InitConvertPossibleField();
    }

    public NormalConvertBridge(Hsb model) {
        Objects.requireNonNull(model, "model cannot be null");
        hsb = model;
        this.setRgbFrom(model);
        InitConvertPossibleField();
    }

    public NormalConvertBridge(Hsl model) {
        Objects.requireNonNull(model, "model cannot be null");
        hsl = model;
        this.setRgbFrom(model);
        InitConvertPossibleField();
    }

    public NormalConvertBridge(Cmyk model) {
        Objects.requireNonNull(model, "model cannot be null");
        cmyk = model;
        this.setRgbFrom(model);
        InitConvertPossibleField();
    }

    public NormalConvertBridge(YCrCb model) {
        Objects.requireNonNull(model, "model cannot be null");
        yCrCb = model;
        this.setRgbFrom(model);
        InitConvertPossibleField();
    }

    public NormalConvertBridge(Xyz model) {
        Objects.requireNonNull(model, "model cannot be null");
        xyz = model;
        this.setRgbFrom(model);
        InitConvertPossibleField();
    }

    public NormalConvertBridge(Lab model) {
        Objects.requireNonNull(model, "model cannot be null");
        lab = model;
        xyz = convert(lab, Xyz.class);
        this.setRgbFrom(xyz);
        InitConvertPossibleField();
    }

    protected void InitConvertPossibleField() {
        if (hsb == null) hsb = convert(rgb, Hsb.class);
        if (hsl == null) hsl = convert(rgb, Hsl.class);
        if (cmyk == null) cmyk = convert(rgb, Cmyk.class);
        if (yCrCb == null) yCrCb = convert(rgb, YCrCb.class);
        if (xyz == null) xyz = convert(rgb, Xyz.class);
        if (lab == null) lab = convert(xyz, Lab.class);
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

    @Override
    public Grayscale getGrayscale() {
        return grayscale;
    }

    public static NormalConvertBridge fromRgb(int r, int g, int b) {
        return new NormalConvertBridge(Rgb.from(r, g, b));
    }

    protected void setRgbFrom(Object source) {
        rgb = ModelsManager.convert(source, Rgb.class);
        grayscale = ModelsManager.convert(rgb, Grayscale.class);
    }
}
