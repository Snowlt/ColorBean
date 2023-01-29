package windows.component;

import colorpad.core.model.Hsb;
import colorpad.extend.IConvertBridge;
import colorpad.extend.NormalConvertBridge;
import toolkit.ColorTool;
import toolkit.Common;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 主界面的Panel类 HSB 面板
 *
 * @author Snow
 */
public class HsbPanel extends AbstractColorPanel {
    private final GradientColorSlider sldHue, sldSaturation, sldBrightness;
    private final JTextField txtH, txtS, txtB;

    public HsbPanel() {
        // Label
        JLabel labTH = new JLabel("色相");
        JLabel labTS = new JLabel("饱和度");
        JLabel labTB = new JLabel("亮度");
        int x = 4, y = 6, interval = 24;
        int w = 42, h = 24;
        labTH.setBounds(x, y, w, h);
        y += interval + labTH.getHeight();
        labTS.setBounds(x, y, w, h);
        y += interval + labTS.getHeight();
        labTB.setBounds(x, y, w, h);
        labTH.setHorizontalAlignment(JLabel.CENTER);
        labTS.setHorizontalAlignment(JLabel.CENTER);
        labTB.setHorizontalAlignment(JLabel.CENTER);
        this.add(labTH);
        this.add(labTS);
        this.add(labTB);
        // Hue滑块
        sldHue = new GradientColorSlider(0, 360, 0);
        sldSaturation = new GradientColorSlider(0, 100, 0);
        sldBrightness = new GradientColorSlider(0, 100, 0);
        x = labTH.getX() + labTH.getWidth();
        y = labTH.getY();
        interval = labTS.getY() - labTH.getY();
        sldHue.setBounds(x, y, sliderWidth, sliderHeight);
        y += interval;
        sldSaturation.setBounds(x, y, sliderWidth, sliderHeight);
        y += interval;
        sldBrightness.setBounds(x, y, sliderWidth, sliderHeight);
        // 刻度线
        sldHue.setMajorTickSpacing(30);
        sldHue.setMinorTickSpacing(15);
        sldSaturation.setMajorTickSpacing(10);
        sldBrightness.setMajorTickSpacing(10);
        sldHue.setOpaque(false);
        sldSaturation.setOpaque(false);
        sldBrightness.setOpaque(false);
        // 增加到Panel中
        this.add(sldHue);
        this.add(sldSaturation);
        this.add(sldBrightness);
        // 绘制颜色
        sldHue.setColors(new Color(0xFF0000), new Color(0xFFFF00),
                new Color(0x00FF00), new Color(0x00FFFF),
                new Color(0x0000FF), new Color(0xFF00FF),
                new Color(0xFF0000));
        // TextField
        txtH = new JTextField(String.valueOf(sldHue.getValue()));
        txtS = new JTextField(String.valueOf(sldSaturation.getValue()));
        txtB = new JTextField(String.valueOf(sldBrightness.getValue()));
        x = labTH.getX() + (labTH.getWidth() - txtWidth) / 2;
        y = labTH.getY() + labTH.getHeight();
        txtH.setBounds(x, y, txtWidth, txtHeight);
        y = labTS.getY() + labTS.getHeight();
        txtS.setBounds(x, y, txtWidth, txtHeight);
        y = labTB.getY() + labTB.getHeight();
        txtB.setBounds(x, y, txtWidth, txtHeight);
        this.add(txtH);
        this.add(txtS);
        this.add(txtB);
        // 设置文本框的文字对齐
        txtH.setHorizontalAlignment(JTextField.CENTER);
        txtS.setHorizontalAlignment(JTextField.CENTER);
        txtB.setHorizontalAlignment(JTextField.CENTER);
        // 监听器
        // 文本框
        txtH.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                String fixed = Common.getValue(txtH.getText());
                int num = Common.cInt(fixed);
                if (num < 0)
                    num = 0;
                if (num > 360)
                    num = 360;
                if (num != sldHue.getValue())
                    updateHSBValue(num, -1, -1);
                else
                    txtH.setText(fixed);
            }
        });
        txtS.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                String fixed = Common.getValue(txtS.getText());
                int num = Common.cInt(fixed);
                if (num < 0)
                    num = 0;
                if (num > 100)
                    num = 100;
                if (num != sldSaturation.getValue())
                    updateHSBValue(-1, num, -1);
                else
                    txtS.setText(fixed);
            }
        });
        txtB.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                String fixed = Common.getValue(txtB.getText());
                int num = Common.cInt(fixed);
                if (num < 0)
                    num = 0;
                if (num > 100)
                    num = 100;
                if (num != sldBrightness.getValue())
                    updateHSBValue(-1, -1, num);
                else
                    txtB.setText(fixed);
            }
        });
        // 滑块
        sldHue.addChangeListener((ChangeEvent e) -> {
            updateHSBValue(sldHue.getValue(), -1, -1);
            autoUpdateOutside();
        });
        sldSaturation.addChangeListener((ChangeEvent e) -> {
            updateHSBValue(-1, sldSaturation.getValue(), -1);
            autoUpdateOutside();
        });
        sldBrightness.addChangeListener((ChangeEvent e) -> {
            updateHSBValue(-1, -1, sldBrightness.getValue());
            autoUpdateOutside();
        });
    }


    /**
     * 内部调用 单独刷新 HSB 面板的内容（如果要单独设置给其他项传入-1）
     *
     * @param h 更新 色相（忽略此项传入-1）
     * @param s 更新 饱和度（忽略此项传入-1）
     * @param b 更新 亮度（忽略此项传入-1）
     */
    private void updateHSBValue(int h, int s, int b) {
        // 判断文本框和滑块的改变
        if (h >= 0) {
            sldHue.setValue(h);
            txtH.setText(String.valueOf(h));
        }
        h = sldHue.getValue();
        if (s >= 0) {
            sldSaturation.setValue(s);
            txtS.setText(String.valueOf(s));
        }
        if (b >= 0) {
            sldBrightness.setValue(b);
            txtB.setText(String.valueOf(b));
        }
        // 重新绘制图像
        int ch = sldHue.getValue();
        int cs = sldSaturation.getValue();
        int cb = sldBrightness.getValue();
        if (h >= 0 || s >= 0) {
            // 色相饱和度改变 重绘亮度
            Color cLeft = ColorTool.hsbToColor(ch, cs, 0);
            Color cRight = ColorTool.hsbToColor(ch, cs, 100);
            sldBrightness.setColors(cLeft, cRight);
        }
        if (h >= 0 || b >= 0) {
            // 色相亮度改变 重绘饱和度
            Color cLeft = ColorTool.hsbToColor(ch, 0, cb);
            Color cRight = ColorTool.hsbToColor(ch, 100, cb);
            sldSaturation.setColors(cLeft, cRight);
        }
    }

    @Override
    protected IConvertBridge getCurrentColor() {
        int h, s, b;
        h = sldHue.getValue();
        s = sldSaturation.getValue();
        b = sldBrightness.getValue();
        return new NormalConvertBridge(Hsb.from(h, s, b));
    }

    @Override
    protected void updateCurrentColor(IConvertBridge bridge) {
        Hsb hsb = bridge.getHsb();
        updateHSBValue((int) hsb.h(), (int) hsb.s(), (int) hsb.b());
    }
}

