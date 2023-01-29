package windows.formula;

import colorpad.core.Formula;
import colorpad.core.FormulaType;
import colorpad.core.ModelsManager;
import colorpad.core.model.Hsb;
import colorpad.core.model.Rgb;
import fit.simplification.Check;
import option.Option;
import toolkit.ColorTool;
import toolkit.Common;
import windows.component.ColorWheel;
import windows.component.GradientColorSlider;
import windows.component.Palette;
import windows.listener.MouseClickListener;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * 配色窗口
 *
 * @author Snow
 */
public class FormulaForm extends JFrame {

    private FormulaType currentType;
    private Hsb currentColor;
    private final FormulaResultBinding[] resultBindings;
    private static FormulaType lastUsedType = FormulaType.Monochromatic;

    private JPanel panRoot;
    private JRadioButton radComplementary;
    private JRadioButton radMonochromatic;
    private JRadioButton radAnalogous;
    private JRadioButton radTetradic;
    private JRadioButton radSplitComplementary;
    private JRadioButton radTriadic;
    private JPanel panWheel;
    private JPanel panArea1;
    private JPanel panArea2;
    private JPanel panArea3;
    private JPanel panArea4;
    private JLabel labArea1Color1;
    private JLabel labArea1Hex1;
    private JLabel labArea1Hsb1;
    private JPanel panBrightness;

    private GradientColorSlider sldBrightness;
    private ColorWheel colorWheel;

    public FormulaForm(Hsb hsb) {
        setContentPane(panRoot);
        setMinimumSize(new Dimension(598, 500));
        setTitle("配色方案");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        currentColor = Objects.requireNonNull(hsb);
        sldBrightness = new GradientColorSlider(0, 100, 75);
        sldBrightness.setPaintTicks(true);
        panBrightness.add(sldBrightness);
        colorWheel = new ColorWheel(hsb.s());
        colorWheel.setDotDiameter(13);
        panWheel.add(colorWheel);
        // 绑定配色方案和 ViewModel
        resultBindings = new FormulaResultBinding[]{
                FormulaResultBinding.createColorBinding(panArea1),
                FormulaResultBinding.createColorBinding(panArea2),
                FormulaResultBinding.createColorBinding(panArea3),
                FormulaResultBinding.createColorBinding(panArea4),
        };
        colorWheel.setOnSpin((h, s) -> {
            currentColor = Hsb.from(h, s, currentColor.b());
            updateDisplay();
        });
        changeFormula(lastUsedType);
        radMonochromatic.addActionListener(e -> changeFormula(FormulaType.Monochromatic));
        radComplementary.addActionListener(e -> changeFormula(FormulaType.Complementary));
        radSplitComplementary.addActionListener(e -> changeFormula(FormulaType.SplitComplementary));
        radAnalogous.addActionListener(e -> changeFormula(FormulaType.Analogous));
        radTriadic.addActionListener(e -> changeFormula(FormulaType.Triadic));
        radTetradic.addActionListener(e -> changeFormula(FormulaType.Tetradic));
        sldBrightness.addChangeListener(e -> {
            JSlider sld = (JSlider) e.getSource();
            currentColor = Hsb.from(currentColor.h(), currentColor.s(), sld.getValue());
            updateDisplay();
        });
    }

    public void changeFormula(FormulaType type) {
        currentType = type;
        lastUsedType = currentType;
        updateDisplay();
    }

    public void setHsb(Hsb hsb) {
        this.currentColor = hsb;
        updateDisplay();
    }

    private void updateDisplay() {
        sldBrightness.setColors(ColorTool.hsbToColor(currentColor.h(), currentColor.s(), 0),
                ColorTool.hsbToColor(currentColor.h(), currentColor.s(), 100));
        double[] hues = Formula.GetFormula(currentColor.h(), currentType, null);
        colorWheel.setSaturationAndHues(currentColor.s(), hues);
        for (int i = 0; i < resultBindings.length; i++) {
            FormulaResultBinding rb = resultBindings[i];
            if (i >= hues.length) {
                rb.setVisible(false);
                continue;
            }
            rb.setVisible(true);
            rb.setColor(Hsb.from(hues[i], currentColor.s(), currentColor.b()));
        }
    }

    static class FormulaResultBinding {
        private JPanel area;
        private SingleColorBinding[] colorBindings;

        protected FormulaResultBinding() {
        }

        /**
         * 创建外层方案 {@link JPanel} 的绑定（每一个方案最外层的Panel当作一个{@link FormulaResultBinding}）
         *
         * @param panel Panel
         * @return 承载绑定关系的对象
         */
        public static FormulaResultBinding createColorBinding(JPanel panel) {
            Component[] components = panel.getComponents();
            if (components.length != 3)
                throw new IllegalArgumentException("Component amount wrong, " +
                        "Don't know how to bind inner panel to outer");
            FormulaResultBinding bind = new FormulaResultBinding();
            bind.colorBindings = new SingleColorBinding[components.length];
            for (int i = 0; i < components.length; i++) {
                Component component = components[i];
                if (!(component instanceof JComponent))
                    throw new IllegalArgumentException("Binding object is " + component.getClass().getName());
                bind.colorBindings[i] = SingleColorBinding.createColorBinding((JComponent) component);
            }
            bind.area = panel;
            return bind;
        }

        public void setVisible(boolean visible) {
            area.setVisible(visible);
        }

        public void setColor(Hsb hsb) {
            colorBindings[0].setColor(hsb);
            colorBindings[1].setColor(Hsb.from(hsb.h(), hsb.s(), 100));
            colorBindings[2].setColor(Hsb.from(hsb.h(), 100, 100));
        }

    }

    static class SingleColorBinding {
        private JLabel labColor, labHex, labHsb;
        private Hsb hsb;
        private String hex;
        private final Palette palette = new Palette();

        protected SingleColorBinding() {
        }

        /**
         * 对一个配色方案中内层的三种颜色的创建绑定
         * 每个颜色是1个{@link JPanel}，内部包含3个{@link JLabel}
         *
         * @param panel Panel容器
         * @return 承载绑定关系的对象
         */
        public static SingleColorBinding createColorBinding(JComponent panel) {
            Component[] labels = panel.getComponents();
            if (labels == null || labels.length != 3)
                throw new IllegalArgumentException("Binding target amount not match");
            for (Component label : labels) {
                if (!(label instanceof JLabel)) {
                    String className = label != null ? label.getClass().getName() : null;
                    throw new IllegalArgumentException("Binding object is " + className +
                            " instead of " + JLabel.class.getName());
                }
            }
            SingleColorBinding binding = new SingleColorBinding();
            binding.labColor = (JLabel) labels[0];
            binding.labHex = (JLabel) labels[1];
            binding.labHsb = (JLabel) labels[2];
            binding.initialize();
            return binding;
        }

        private void initialize() {
            palette.autoRelateLabel(labColor);
            labHex.setToolTipText("点击复制 Hex");
            labHex.addMouseListener(MouseClickListener.buildClicked(e -> {
                if (Check.notEmpty(hex)) Common.setSysClipboardText(Option.HexShowSym() ? "#" + hex : hex);
            }));
            labHsb.setToolTipText("点击复制 HSB");
            labHsb.addMouseListener(MouseClickListener.buildClicked(e -> {
                if (hsb != null) {
                    String text = MessageFormat.format("{1}{0}{2}{0}{3}", ",",
                            Math.round(hsb.h()), Math.round(hsb.s()), Math.round(hsb.b()));
                    Common.setSysClipboardText(text);
                }
            }));
        }

        public void setColor(Hsb hsb) {
            if (hsb.equals(this.hsb)) {
                return;
            }
            this.hsb = hsb;
            updateLabel();
        }

        private void updateLabel() {
            Rgb rgb = ModelsManager.convert(hsb, Rgb.class);
            hex = rgb.toHex();
            labHex.setText(Option.HexShowSym() ? "#" + hex : hex);
            labHsb.setText(MessageFormat.format("HSB({1}{0}{2}{0}{3})", ",",
                    Math.round(hsb.h()), Math.round(hsb.s()), Math.round(hsb.b())));
            palette.setColor(rgb);
        }
    }
}
