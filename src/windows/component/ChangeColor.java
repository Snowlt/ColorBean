package windows.component;

import colorpad.core.model.Rgb;
import colorpad.extend.ConvertBridge;
import colorpad.extend.NormalConvertBridge;
import toolkit.ColorTool;

import java.awt.*;

@FunctionalInterface
public interface ChangeColor {

    void change(ConvertBridge bridge);

    default void change(Color color) {
        change(ColorTool.toBridge(color));
    }

    default void change(Rgb rgb) {
        change(new NormalConvertBridge(rgb));
    }

}
