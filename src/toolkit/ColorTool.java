package toolkit;

import colorpad.core.ConversionManager;
import colorpad.core.ConvertBridge;
import colorpad.core.NormalConvertBridge;
import colorpad.core.model.Hsb;
import colorpad.core.model.Rgb;

import java.awt.*;

/**
 * 颜色模型拓展工具
 *
 * @author Snow
 */
public class ColorTool {

    public static Color toColor(Rgb rgb) {
        return new Color(rgb.r(), rgb.g(), rgb.b());
    }

    public static Color toColor(Hsb hsb) {
        return toColor(ConversionManager.convert(hsb, Rgb.class));
    }

    public static Color hsbToColor(double h, double s, double b) {
        return toColor(ConversionManager.convert(new Hsb(h, s, b), Rgb.class));
    }

    public static ConvertBridge toBridge(Color color) {
        return new NormalConvertBridge(new Rgb(color.getRed(), color.getGreen(), color.getBlue()));
    }
}
