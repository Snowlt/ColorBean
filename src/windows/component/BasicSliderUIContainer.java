package windows.component;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.lang.reflect.Field;

/**
 *
 */
public class BasicSliderUIContainer extends BasicSliderUI {

    private final JSlider slider;
    private final BasicSliderUI ui;

    public BasicSliderUIContainer(JSlider b, BasicSliderUI ui) {
        super(b);
        this.slider = b;
        this.ui = ui;
    }

    public JSlider getSlider() {
        return slider;
    }

    public BasicSliderUI getUi() {
        return ui;
    }

    public static JSlider getInnerSlider(BasicSliderUI ui) {
        try {
            Field field = BasicSliderUI.class.getDeclaredField("slider");
            field.setAccessible(true);
            return (JSlider) field.get(ui);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return null;
        }
    }

    public Rectangle getTickRect() {
        try {
            Field field = BasicSliderUI.class.getDeclaredField("tickRect");
            field.setAccessible(true);
            return (Rectangle) field.get(ui);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Rectangle getTrackRect() {
        try {
            Field field = BasicSliderUI.class.getDeclaredField("trackRect");
            field.setAccessible(true);
            return (Rectangle) field.get(ui);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Rectangle getTickRect(BasicSliderUI ui) {
        try {
            Field field = BasicSliderUI.class.getDeclaredField("tickRect");
            field.setAccessible(true);
            return (Rectangle) field.get(ui);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Rectangle getTrackRect(BasicSliderUI ui) {
        try {
            Field field = BasicSliderUI.class.getDeclaredField("trackRect");
            field.setAccessible(true);
            return (Rectangle) field.get(ui);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

}
