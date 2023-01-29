package windows.component;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

public class GradientSliderUI extends BasicSliderUI {

    public GradientSliderUI(JSlider b) {
        super(b);
    }

    public Rectangle getTickRect() {
        return super.tickRect;
    }

    public Rectangle getTrackRect() {
        return super.trackRect;
    }

    @Override
    public void paintTicks(Graphics g) {
        // 什么也不做 o_o
    }

    private static final Color trackGray = new Color(0xbababa);
    private static final Color lightBlue = new Color(0x8fbded);

    @Override
    protected void installDefaults(JSlider slider) {
        super.installDefaults(slider);
        Color highlightColor = getHighlightColor();
        dimGray = new Color((highlightColor.getRed() + trackGray.getRed()) / 2,
                (highlightColor.getGreen() + trackGray.getGreen()) / 2,
                (highlightColor.getBlue() + trackGray.getBlue()) / 2);
    }

    private Color dimGray;

    @Override
    protected Dimension getThumbSize() {
        if (slider.getOrientation() == JSlider.VERTICAL) {
            return new Dimension(20, 15);
        } else {
            return new Dimension(15, 20);
        }
    }

    @Override
    public void paintTrack(Graphics g) {
        Rectangle trackBounds = trackRect;
        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            int y = (trackBounds.height / 2) - 6;
            int w = trackBounds.width;
            g.translate(trackBounds.x, trackBounds.y + y);
            int yu = y - 1, yd = y + 1;
            // 上 中 下
            g.setColor(trackGray);
            g.drawLine(0, yu, w, yu);
            g.drawLine(0, y, w, y);
            g.drawLine(0, yd, w, yd);
            // 左上 左下 右上 右下
            g.setColor(dimGray);
            g.drawLine(0, yu, 0, yu);
            g.drawLine(0, yd, 0, yd);
            g.drawLine(w, yu, w, yu);
            g.drawLine(w, yd, w, yd);

            g.translate(-trackBounds.x, -(trackBounds.y + y));
        } else {
            int x = (trackBounds.width / 2) - 6;
            int h = trackBounds.height;

            g.translate(trackBounds.x + x, trackBounds.y);
            int xl = x - 1, xr = x + 1;
            // 同上，横向绘制
            g.setColor(trackGray);
            g.drawLine(xl, 0, xl, h);
            g.drawLine(x, 0, x, h);
            g.drawLine(xr, 0, xr, h);
            g.setColor(dimGray);
            g.drawLine(xl, 0, xl, 0);
            g.drawLine(xr, 0, xr, 0);
            g.drawLine(xl, h, xl, h);
            g.drawLine(xr, h, xr, h);
            g.translate(-(trackBounds.x + x), -trackBounds.y);
        }
    }

    @Override
    public void paintThumb(Graphics g) {
        Rectangle knobBounds = thumbRect;
        int w = knobBounds.width;
        int h = knobBounds.height;

        g.translate(knobBounds.x, knobBounds.y);
        Rectangle clip = g.getClipBounds();
        g.clipRect(0, 0, w, h);
        g.setColor(slider.isEnabled() ? Color.WHITE : Color.WHITE.darker());
        Color highlightColor = lightBlue;
        Color shadowColor = trackGray;

        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            int cw = w / 2;
            g.fillRect(1, 1, w - 3, h - 1 - cw);
            Polygon p = new Polygon();
            p.addPoint(1, h - cw);
            p.addPoint(cw - 1, h - 1);
            p.addPoint(w - 2, h - 1 - cw);
            g.fillPolygon(p);

            g.setColor(highlightColor);
            g.drawLine(0, 0, w - 2, 0);
            g.drawLine(0, 1, 0, h - 1 - cw);
            g.drawLine(0, h - cw, cw - 1, h - 1);

            g.setColor(lightBlue);
            g.drawLine(w - 1, 0, w - 1, h - 2 - cw);
            g.drawLine(w - 1, h - 1 - cw, w - 1 - cw, h - 1);

            g.setColor(shadowColor);
            g.drawLine(w - 2, 1, w - 2, h - 2 - cw);
            g.drawLine(w - 2, h - 1 - cw, w - 1 - cw, h - 2);
        } else {  // vertical
            int cw = h / 2;
            boolean leftToRight = slider.getComponentOrientation().isLeftToRight();
            if (leftToRight) {
                g.fillRect(1, 1, w - 1 - cw, h - 3);
                Polygon p = new Polygon();
                p.addPoint(w - cw - 1, 0);
                p.addPoint(w - 1, cw);
                p.addPoint(w - 1 - cw, h - 2);
                g.fillPolygon(p);

                g.setColor(highlightColor);
                g.drawLine(0, 0, 0, h - 2);                  // left
                g.drawLine(1, 0, w - 1 - cw, 0);                 // top
                g.drawLine(w - cw - 1, 0, w - 1, cw);              // top slant

                g.setColor(lightBlue);
                g.drawLine(0, h - 2, w - 2 - cw, h - 2);             // bottom
                g.drawLine(w - 1 - cw, h - 1, w - 1, h - 1 - cw);        // bottom slant

                g.setColor(shadowColor);
                g.drawLine(1, h - 2, w - 2 - cw, h - 2);         // bottom
                g.drawLine(w - 1 - cw, h - 2, w - 2, h - cw - 1);     // bottom slant
            } else {
                g.fillRect(5, 1, w - 1 - cw, h - 3);
                Polygon p = new Polygon();
                p.addPoint(cw, 0);
                p.addPoint(0, cw);
                p.addPoint(cw, h - 2);
                g.fillPolygon(p);

                g.setColor(highlightColor);
                g.drawLine(cw - 1, 0, w - 2, 0);             // top
                g.drawLine(0, cw, cw, 0);                // top slant

                g.setColor(Color.black);
                g.drawLine(0, h - 1 - cw, cw, h - 1);         // bottom slant
                g.drawLine(cw, h - 1, w - 1, h - 1);           // bottom

                g.setColor(shadowColor);
                g.drawLine(cw, h - 2, w - 2, h - 2);         // bottom
                g.drawLine(w - 1, 1, w - 1, h - 2);          // right
            }
        }
        g.setClip(clip);
        g.translate(-knobBounds.x, -knobBounds.y);
    }
}
