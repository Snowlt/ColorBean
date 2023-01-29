package windows.component;

import colorpad.core.model.Rgb;
import colorpad.extend.IConvertBridge;
import colorpad.extend.NormalConvertBridge;
import toolkit.Common;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 主界面的Panel类 RGB 面板
 *
 * @author Snow
 */
public class RgbPanel extends AbstractColorPanel {
    private final GradientColorSlider sldR, sldB, sldG;
    private final JTextField txtR, txtG, txtB;

    public RgbPanel() {
        // Label
        JLabel labTR = new JLabel("红");
        JLabel labTG = new JLabel("绿");
        JLabel labTB = new JLabel("蓝");
        int x = 4, y = 6, interval = 24;
        int w = 42, h = 24;
        labTR.setBounds(x, y, w, h);
        y += interval + labTR.getHeight();
        labTG.setBounds(x, y, w, h);
        y += interval + labTG.getHeight();
        labTB.setBounds(x, y, w, h);
        labTR.setHorizontalAlignment(JLabel.CENTER);
        labTG.setHorizontalAlignment(JLabel.CENTER);
        labTB.setHorizontalAlignment(JLabel.CENTER);
        this.add(labTR);
        this.add(labTG);
        this.add(labTB);
        // 滑块
        sldR = new GradientColorSlider(0, 255, 0);
        sldG = new GradientColorSlider(0, 255, 0);
        sldB = new GradientColorSlider(0, 255, 0);
        x = labTR.getX() + labTR.getWidth();
        y = labTR.getY();
        interval = labTG.getY() - labTR.getY();
        sldR.setBounds(x, y, sliderWidth, sliderHeight);
        y += interval;
        sldG.setBounds(x, y, sliderWidth, sliderHeight);
        y += interval;
        sldB.setBounds(x, y, sliderWidth, sliderHeight);
        // 刻度线
        sldR.setMajorTickSpacing(10);
        sldG.setMajorTickSpacing(10);
        sldB.setMajorTickSpacing(10);
        sldR.setOpaque(false);
        sldG.setOpaque(false);
        sldB.setOpaque(false);
        // 增加到Panel中
        this.add(sldR);
        this.add(sldB);
        this.add(sldG);
        // TextField
        txtR = new JTextField(String.valueOf(sldR.getValue()));
        txtG = new JTextField(String.valueOf(sldB.getValue()));
        txtB = new JTextField(String.valueOf(sldG.getValue()));
        x = labTR.getX() + (labTR.getWidth() - txtWidth) / 2;
        y = labTR.getY() + labTR.getHeight();
        txtR.setBounds(x, y, txtWidth, txtHeight);
        y = labTG.getY() + labTG.getHeight();
        txtG.setBounds(x, y, txtWidth, txtHeight);
        y = labTB.getY() + labTB.getHeight();
        txtB.setBounds(x, y, txtWidth, txtHeight);
        this.add(txtR);
        this.add(txtG);
        this.add(txtB);
        // 设置文本框的文字对齐
        txtR.setHorizontalAlignment(JTextField.CENTER);
        txtG.setHorizontalAlignment(JTextField.CENTER);
        txtB.setHorizontalAlignment(JTextField.CENTER);
        // 监听器
        // 文本框
        txtR.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                String fixed = Common.getValue(txtR.getText());
                int num = Common.cInt(fixed);
                if (num < 0)
                    num = 0;
                if (num > 255)
                    num = 255;
                if (num != sldR.getValue())
                    updateRGBValue(num, -1, -1);
                else
                    txtR.setText(fixed);
            }
        });
        txtG.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                String fixed = Common.getValue(txtG.getText());
                int num = Common.cInt(fixed);
                if (num < 0)
                    num = 0;
                if (num > 255)
                    num = 255;
                if (num != sldG.getValue())
                    updateRGBValue(-1, num, -1);
                else
                    txtG.setText(fixed);
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
                if (num > 255)
                    num = 255;
                if (num != sldB.getValue())
                    updateRGBValue(-1, -1, num);
                else
                    txtB.setText(fixed);
            }
        });
        // 滑块
        sldR.addChangeListener((ChangeEvent e) -> {
            updateRGBValue(sldR.getValue(), -1, -1);
            autoUpdateOutside();
        });
        sldG.addChangeListener((ChangeEvent e) -> {
            updateRGBValue(-1, sldG.getValue(), -1);
            autoUpdateOutside();
        });
        sldB.addChangeListener((ChangeEvent e) -> {
            updateRGBValue(-1, -1, sldB.getValue());
            autoUpdateOutside();
        });
    }

    /**
     * 内部调用 单独刷新 RGB 面板的内容（如果要单独设置给其他项传入-1）
     *
     * @param r 更新 色相（忽略此项传入-1）
     * @param g 更新 饱和度（忽略此项传入-1）
     * @param b 更新 亮度（忽略此项传入-1）
     */
    private void updateRGBValue(int r, int g, int b) {
        // 判断文本框和滑块的改变
        if (r >= 0) {
            sldR.setValue(r);
            txtR.setText(String.valueOf(r));
        }
        if (g >= 0) {
            sldG.setValue(g);
            txtG.setText(String.valueOf(g));
        }
        if (b >= 0) {
            sldB.setValue(b);
            txtB.setText(String.valueOf(b));
        }
        // 重新绘制图像
        int cr = sldR.getValue();
        int cg = sldG.getValue();
        int cb = sldB.getValue();
        if (r >= 0 || g >= 0) {
            // RG改变 重绘B
            Color cLeft = new Color(cr, cg, 0);
            Color cRight = new Color(cr, cg, 255);
            sldB.setColors(cLeft, cRight);
        }
        if (r >= 0 || b >= 0) {
            // RB改变 重绘G
            Color cLeft = new Color(cr, 0, cb);
            Color cRight = new Color(cr, 255, cb);
            sldG.setColors(cLeft, cRight);
        }
        if (g >= 0 || b >= 0) {
            // GB改变 重绘R
            Color cLeft = new Color(0, cg, cb);
            Color cRight = new Color(255, cg, cb);
            sldR.setColors(cLeft, cRight);
        }
    }

    @Override
    protected IConvertBridge getCurrentColor() {
        return NormalConvertBridge.fromRgb(sldR.getValue(), sldG.getValue(), sldB.getValue());
    }

    @Override
    protected void updateCurrentColor(IConvertBridge bridge) {
        Rgb rgb = bridge.getRgb();
        updateRGBValue(rgb.r(), rgb.g(), rgb.b());
    }
}