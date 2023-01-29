package windows.component;

import colorpad.core.Basic;
import fit.simplification.Check;
import toolkit.Common;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;

/**
 * 色轮
 *
 * @author Snow
 */
public class ColorWheel extends JPanel implements MouseInputListener {

    private double[] hues;
    private double saturation;

    private double centerX, centerY;
    private int diameter;
    private int dotDiameter;
    private BiConsumer<Double, Double> onSpin;

    private static final Image imgWheelSource = new ImageIcon(Common.getURL("res/ColorWheel.png")).getImage();
    // 256 * 256 下缩放 0.875 倍比较合适
    private static final double wheelScaledFactor = 0.875;
    private Image imgWheel;

    public ColorWheel() {
        this(0);
    }

    public ColorWheel(double saturation) {
        this.saturation = saturation;
        dotDiameter = 10;
        this.setOpaque(true);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    /**
     * 更新回调
     *
     * @param onSpin 分别传入 色相、饱和度
     */
    public void setOnSpin(BiConsumer<Double, Double> onSpin) {
        this.onSpin = onSpin;
    }

    public void setSaturationAndHues(double saturation, double... hues) {
        if (this.hues != null && this.hues.length == hues.length) {
            int same = 0;
            for (int i = 0; i < hues.length; i++) {
                same += Basic.decimalEquals(this.hues[i], hues[i]) ? 1 : 0;
            }
            if (same == hues.length && Basic.decimalEquals(this.saturation, saturation)) {
                return;
            }
        }
        this.saturation = saturation;
        setHues(hues);
    }

    public void setHues(double... hues) {
        if (!Check.isTrue(hues)) {
            hues = new double[]{0};
        }
        this.hues = hues;
        updateDisplay();
    }

    public void setSaturation(double saturation) {
        if (Basic.decimalEquals(this.saturation, saturation)) return;
        this.saturation = saturation;
        updateDisplay();
    }

    public void setDotDiameter(int dotDiameter) {
        if (dotDiameter < 1) {
            throw new IllegalArgumentException("dotDiameter must > 1");
        }
        this.dotDiameter = dotDiameter;
        updateDisplay();
    }

    public void updateDisplay() {
        repaint();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        int nd = Math.min(width, height);
        if (diameter != nd) {
            diameter = nd;
            imgWheel = imgWheelSource.getScaledInstance(diameter, diameter, Image.SCALE_SMOOTH);
        }
        centerX = width / 2.0;
        centerY = height / 2.0;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawWheel(g);
        if (diameter >= dotDiameter) {
            drawDot(g);
        }
    }

    private void drawDot(Graphics g) {
        for (int i = 0; i < hues.length; i++) {
            double hue = hues[i];
            // 色轮中心到点的圆心距离
            double dotRadius = dotDiameter / 2.0;
            double d = diameter * wheelScaledFactor * saturation / 2 / 100 - dotRadius;
            d = d < 0 ? 0 : d;
            // 极坐标转为直角坐标
            double x, y, a;
            // 普通夹角(+X 轴逆时针)为 alpha，色轮夹角(+Y 轴顺时针)为 beta，alpha = 90° - beta
            a = ((double) 90 - (hue % 360)) / 180 * Math.PI;
            x = Math.cos(a) * d;
            y = Math.sin(a) * d;
            // 将标准直角坐标系变为屏幕坐标系
            x += centerX;
            y = -y + centerY;
            // 绘制形状
            g.setColor(i == 0 ? Color.WHITE : Color.DARK_GRAY);
            g.fillOval((int) (x - dotRadius), (int) (y - dotRadius), dotDiameter, dotDiameter);
        }
    }

    private void drawWheel(Graphics g) {
        int x = (getWidth() - diameter) / 2, y = (getHeight() - diameter) / 2;
        boolean loaded = g.drawImage(imgWheel, x, y, null);
        if (!loaded) {
            // 强制等待一次图像加载
            imgWheel = new ImageIcon(imgWheel).getImage();
            g.drawImage(imgWheel, x, y, null);
        }
    }

    private void setDotLocation(Point point) {
        if (onSpin == null) {
            return;
        }
        double x = point.x - centerX;
        double y = -point.y + centerY;
        double angle = Math.atan2(y, x) * 180 / Math.PI;
        // angle 的范围是 [-180, 180) ，要修正为 [0, 360) 应加上 angle = (angle + 360) % 360;
        double hue = (360 + 90 - angle) % 360;
        double s = Math.sqrt(x * x + y * y);
        int wheelDiameter = diameter - dotDiameter;
        if (wheelDiameter <= 0) {
            s = 100;
        } else {
            s = Math.min(s * 2 * 100 / wheelDiameter / wheelScaledFactor, 100);
        }
        onSpin.accept(hue, s);
    }

    // 鼠标响应

    @Override
    public void mousePressed(MouseEvent e) {
        setDotLocation(e.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        setDotLocation(e.getPoint());
    }

    // 用不到的事件

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
