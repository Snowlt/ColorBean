package windows.component;

import colorpad.core.model.Rgb;
import toolkit.ColorTool;

import javax.swing.*;
import java.awt.*;

public class Palette implements Icon {
    private Color color;
    private JComponent container;

    public Palette() {
        color = Color.WHITE;
    }

    public Palette(Rgb rgb) {
        this();
        color = ColorTool.toColor(rgb);
    }

    public void setColor(Rgb rgb) {
        if (rgb.compareWith(color.getRed(), color.getGreen(), color.getBlue())) {
            return;
        }
        this.color = ColorTool.toColor(rgb);
        if (container != null) {
            container.repaint();
        }
    }

    public void setUpdateContainer(JComponent container) {
        this.container = container;
    }

    public void autoRelateLabel(JLabel label) {
        label.setIcon(this);
        setUpdateContainer(label);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        g.fillRect(0, 0, c.getWidth(), c.getHeight());
    }

    @Override
    public int getIconWidth() {
        return 100;
    }

    @Override
    public int getIconHeight() {
        return 100;
    }
}
