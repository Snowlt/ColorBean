package colorpad.extend;

import colorpad.core.model.*;

import java.util.Objects;

import static colorpad.core.ModelsManager.convert;

/**
 * 颜色模型转换桥接工具
 *
 * @author Snow
 */
public class NormalConvertBridge extends ConvertBridge {

    public NormalConvertBridge(Rgb model) {
        Objects.requireNonNull(model, "model cannot be null");
        rgb = model;
        InitConvertPossibleField();
    }

    public NormalConvertBridge(Hsb model) {
        Objects.requireNonNull(model, "model cannot be null");
        hsb = model;
        this.SetRgbFrom(model);
        InitConvertPossibleField();
    }

    public NormalConvertBridge(Hsl model) {
        Objects.requireNonNull(model, "model cannot be null");
        hsl = model;
        this.SetRgbFrom(model);
        InitConvertPossibleField();
    }

    public NormalConvertBridge(Cmyk model) {
        Objects.requireNonNull(model, "model cannot be null");
        cmyk = model;
        this.SetRgbFrom(model);
        InitConvertPossibleField();
    }

    public NormalConvertBridge(YCrCb model) {
        Objects.requireNonNull(model, "model cannot be null");
        yCrCb = model;
        this.SetRgbFrom(model);
        InitConvertPossibleField();
    }

    public NormalConvertBridge(Xyz model) {
        Objects.requireNonNull(model, "model cannot be null");
        xyz = model;
        this.SetRgbFrom(model);
        InitConvertPossibleField();
    }

    public NormalConvertBridge(Lab model) {
        Objects.requireNonNull(model, "model cannot be null");
        lab = model;
        xyz = convert(lab, Xyz.class);
        this.SetRgbFrom(xyz);
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

    public static NormalConvertBridge FromRgb(int r, int g, int b) {
        return new NormalConvertBridge(new Rgb(r, g, b));
    }
}
