package windows.component;


import fit.simplification.Check;
import fit.simplification.Convert;
import main.BootError;

import javax.swing.*;
import javax.swing.plaf.SliderUI;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;

/**
 * 支持显示渐变的颜色条
 * P.S. 内部使用反射实现了一些 patch，程序启动时用 {@link #testPermission()} 做一次检查
 *
 * @author Snow
 */
public class GradientColorSlider extends JSlider implements ComponentListener {

    private Color[] bar;
    private Rectangle rect, thisRect;

    private static volatile Boolean useReplacedUI;

    public GradientColorSlider(int min, int max, int value) {
        super(min, max, value);
        this.setPaintTicks(true);
        majorTickSpacing = 0;
        minorTickSpacing = 0;
        addComponentListener(this);
    }

    /**
     * 设置渐变条中的颜色
     *
     * @param colors 颜色
     */
    public void setColors(Color... colors) {
        if (colors == null || colors.length == 0) {
            bar = null;
        } else if (colors.length == 1) {
            bar = new Color[]{colors[0], colors[0]};
        } else {
            bar = colors;
        }
        if (this.isVisible()) {
            this.repaint();
        }
    }

    @Override
    public void setUI(SliderUI ui) {
        if (useReplacedUI == null) {
            synchronized (GradientColorSlider.class) {
                if (useReplacedUI == null) {
                    try {
                        testPermission();
                        useReplacedUI = false;
                    } catch (BootError e) {
                        useReplacedUI = true;
                    }
                }
            }
        }
        if (!(ui instanceof GradientSliderUI) && useReplacedUI) {
            if (getUI() instanceof GradientSliderUI) {
                return;
            }
            super.setUI(new GradientSliderUI(this));
        } else {
            super.setUI(ui);
        }
    }

    @Override
    public void setMajorTickSpacing(int n) {
    }

    @Override
    public void setMinorTickSpacing(int n) {
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (ui == null || g == null) {
            return;
        }
        Graphics scratchGraphics = g.create();
        try {
            drawGradientBar((BasicSliderUI) ui, scratchGraphics, this);
            ui.update(scratchGraphics, this);
        } finally {
            scratchGraphics.dispose();
        }
    }

    private void drawGradientBar(BasicSliderUI ui, Graphics g, JSlider slider) {
        if (Check.isEmpty(bar)) {
            return;
        }
        if (rect == null || rect.width == 0 || rect.height == 0) {
            rect = new Rectangle();
            Rectangle trackRect;
            Rectangle tickRect;
            if (ui instanceof GradientSliderUI) {
                GradientSliderUI gradientSliderUi = (GradientSliderUI) ui;
                tickRect = gradientSliderUi.getTickRect();
                trackRect = gradientSliderUi.getTrackRect();
            } else {
                try {
                    // 参考 BasicSliderUI.paintTicks
                    Field fTrackRect = BasicSliderUI.class.getDeclaredField("trackRect");
                    Field fTickRect = BasicSliderUI.class.getDeclaredField("tickRect");
                    fTrackRect.setAccessible(true);
                    fTickRect.setAccessible(true);
                    trackRect = (Rectangle) fTrackRect.get(ui);
                    tickRect = (Rectangle) fTickRect.get(ui);
                } catch (Exception e) {
                    return;
                }
            }
            // 横向的尺寸，纵向不确定需不需要做矫正
            rect.setBounds(trackRect.x, tickRect.y + 2, trackRect.width, Math.max(tickRect.height, 12));
        }
        // 计算平分的颜色块
        Graphics2D gra = (Graphics2D) g;
        int blocks = bar.length - 1;
        Color cLeft, cRight;
        GradientPaint paint;
        double x = 0, y = 0, width = rect.getWidth(), height = rect.getHeight();
        gra.translate(rect.x, rect.y);
        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            width /= blocks;
        } else {
            height /= blocks;
        }
        for (int i = 0; i < blocks; i++) {
            cLeft = bar[i];
            cRight = bar[i + 1];
            if (slider.getOrientation() == JSlider.HORIZONTAL) {
                x = i * width;
                if (i == blocks - 1) {
                    width = rect.getWidth() - x;
                }
                paint = new GradientPaint(new Point2D.Double(x, y), cLeft,
                        new Point2D.Double((i + 1) * width, y), cRight);
            } else {
                y = i * height;
                if (i == blocks - 1) {
                    height = rect.getHeight() - y;
                }
                paint = new GradientPaint(new Point2D.Double(x, y), cLeft,
                        new Point2D.Double(x, (i + 1) * height), cRight);
            }
            gra.setPaint(paint);
            gra.fill(new Rectangle2D.Double(x, y, width, height));
        }
        gra.translate(-rect.x, -rect.y);
    }

    public static void testPermission() throws BootError {
        try {
            String version = System.getProperty("java.specification.version");
            if (Convert.toDouble(version, 1.9) > 1.8) {
                // Java 9 以上使用 setAccessible 可能没有权限
                Field field = BasicSliderUI.class.getDeclaredField("trackRect");
                field.setAccessible(true);
            }
        } catch (Exception e) {
            throw new BootError("Please add flowing param to Java: " +
                    "\"--add-opens java.desktop/javax.swing.plaf.basic=ALL-UNNAMED\". ", true, e);
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        rect = null;
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
