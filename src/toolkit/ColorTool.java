package toolkit;

import colorpad.core.ModelsManager;
import colorpad.core.model.Hsb;
import colorpad.core.model.Rgb;
import colorpad.extend.IConvertBridge;
import colorpad.extend.NormalConvertBridge;

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
        return toColor(ModelsManager.convert(hsb, Rgb.class));
    }

    public static Color hsbToColor(double h, double s, double b) {
        return toColor(ModelsManager.convert(Hsb.from(h, s, b), Rgb.class));
    }

    public static IConvertBridge toBridge(Color color) {
        return new NormalConvertBridge(Rgb.from(color.getRed(), color.getGreen(), color.getBlue()));
    }
}
