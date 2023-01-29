package windows;

import colorpad.core.ModelsManager;
import colorpad.core.converter.GrayAverageAlgorithm;
import colorpad.core.converter.GraySpaceComponentAlgorithm;
import colorpad.core.model.*;
import colorpad.extend.IConvertBridge;
import colorpad.extend.NormalConvertBridge;
import fit.simplification.Check;
import main.ColorBean;
import option.Option;
import toolkit.Common;
import windows.component.HsbPanel;
import windows.component.Palette;
import windows.component.RgbPanel;
import windows.formula.FormulaForm;
import windows.listener.MouseClickListener;
import windows.screen.PickColorForm;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Snow
 */
public class MainForm extends JFrame {
    private JPanel panelRoot;
    private JTabbedPane tabPalette;
    private JButton btnFormula;
    private JTextField txtRgb;
    private JTextField txtHsb;
    private JTextField txtHex;
    private JTextField txtCmyk;
    private JTextField txtGray;
    private JTextField txtLab;
    private JButton btnRand;
    private JButton btnMenu;
    private JButton btnAbout;
    private JLabel boxColor;
    private JButton btnPick;
    private final Palette palette = new Palette();

    // 自定义面板
    private HsbPanel panelHsb;
    private RgbPanel panelRgb;

    private IConvertBridge bridge = new NormalConvertBridge(Rgb.WHITE);

    public MainForm() {
        // 程序退出时的回调用
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (Option.Restore()) {
                Option.ColorBackup(bridge.toHex());
            }
            // 写入文件
            try {
                Option.saveOption();
            } catch (Exception e) {
                System.err.println("保存设置失败");
            }
        }));
        // 按钮事件
        btnAbout.addActionListener(e -> Common.msgShow("关于 " + ColorBean.APPLICATION_NAME,
                ColorBean.APPLICATION_NAME + " " + ColorBean.APPLICATION_VERSION +
                        "\n一个颜色处理小工具\n基于 ColorPadConsole 分支制作\n\nPowered By Xyuxf.com\n",
                this));
        btnRand.addActionListener(e -> {
            int r, g, b;
            r = (int) (Math.random() * 256);
            g = (int) (Math.random() * 256);
            b = (int) (Math.random() * 256);
            updateAll(NormalConvertBridge.fromRgb(r, g, b));
        });
        btnFormula.addActionListener(e -> {
            FormulaForm form = new FormulaForm(bridge.getHsb());
            form.setLocationRelativeTo(this);
            form.setVisible(true);
        });
        JPopupMenu menu = getMenu();
        btnMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // 获取鼠标点击的位置
                menu.show(btnMenu, e.getX(), e.getY());
            }
        });
        btnMenu.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                // 空格键 32，回车键 10
                if (e.getKeyCode() == 32 || e.getKeyCode() == 10) {
                    menu.show(btnMenu, btnMenu.getWidth(), btnMenu.getHeight());
                }
            }
        });
        btnPick.setEnabled(Common.getOs() == Common.OsType.WINDOWS || Common.getOs() == Common.OsType.MAC);
        btnPick.addActionListener(e -> {
            String message = null;
            try {
                this.setVisible(false);
                PickColorForm pickColorForm = PickColorForm.createScreenCapture();
                pickColorForm.setOnChangeColor(this::updateAll);
                pickColorForm.setOnDispose(() -> this.setVisible(true));
            } catch (UnsupportedOperationException ex) {
                message = ex.getMessage();
            } catch (Exception ex) {
                ex.printStackTrace();
                message = "未知错误，无法启动屏幕取色";
            }
            if (message != null) {
                Common.msgShow("错误", message);
                this.setVisible(true);
            }
        });
        txtRgb.addMouseListener(buildCtbListener("RGB", Rgb::fromString, NormalConvertBridge::new));
        txtHsb.addMouseListener(buildCtbListener("HSB", Hsb::fromString, NormalConvertBridge::new));
        txtCmyk.addMouseListener(buildCtbListener("CMYK", Cmyk::fromString, NormalConvertBridge::new));
        txtLab.addMouseListener(buildCtbListener("CIE-Lab", Lab::fromString, NormalConvertBridge::new));
        txtHex.addMouseListener(buildCtbListener("Hex", Rgb::fromHexEnhanced, NormalConvertBridge::new));
        txtGray.addMouseListener(buildCtbListener("灰度值", Grayscale::fromString,
                grayscale -> new NormalConvertBridge(grayscale.toRgb())));
    }

    /**
     * 生成一个给 颜色值 文本框用的鼠标点击的监听器，用于处理复制和输入操作
     *
     * @param name          显示的颜色模型名字
     * @param strToModel    将字符转为颜色模型(lambada)
     * @param modelToBridge 将颜色模型转为ConvertBridge(lambada)
     * @param <M>           颜色模型的类型(泛型)
     * @return 点击事件监听器
     */
    private <M> MouseListener buildCtbListener(String name, Function<String, M> strToModel,
                                               Function<M, IConvertBridge> modelToBridge) {
        Objects.requireNonNull(strToModel);
        Objects.requireNonNull(modelToBridge);
        return MouseClickListener.buildClicked(e -> {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (!(e.getSource() instanceof JTextComponent)) return;
                JTextComponent text = (JTextComponent) e.getSource();
                Common.setSysClipboardText(text.getText());
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                String input = Common.msgInput("输入颜色值", "请输入 " + name + ":", this);
                if (Check.isEmpty(input)) return;
                Optional<IConvertBridge> bridge = Optional.of(input).map(strToModel).map(modelToBridge);
                if (!bridge.isPresent()) {
                    Common.msgShow("提示", "无法将输入的内容转为 " + name, this);
                    return;
                }
                updateAll(bridge.get());
            }
        });
    }

    /**
     * 修改界面的颜色文本信息和颜色显示框
     *
     * @param bridge 转换桥
     */
    private void updateInfo(IConvertBridge bridge) {
        this.bridge = bridge;
        // 信息面板
        final String separator = ",";
        Rgb rgb = bridge.getRgb();
        // RGB
        if (Option.RGBFloat()) {
            txtRgb.setText(String.format("%.2f,%.2f,%.2f", rgb.r() / 255.0, rgb.g() / 255.0, rgb.b() / 255.0));
        } else {
            txtRgb.setText(rgb.toString(separator));
        }
        // HSB
        Hsb hsb = bridge.getHsb();
        if (Option.HSBFloat()) {
            txtHsb.setText(String.format("%.2f,%.2f,%.2f", hsb.h() / 360.0, hsb.s() / 100.0, hsb.b() / 100.0));
        } else {
            txtHsb.setText(hsb.toString(separator));
        }
        String hex = bridge.toHex();
        txtHex.setText(Option.HexShowSym() ? "#" + hex : hex);
        txtCmyk.setText(bridge.getCmyk().toString(separator));
        txtGray.setText(String.valueOf(bridge.getGrayscale().value()));
        txtLab.setText(bridge.getLab().toString(separator));
        // 颜色框
        palette.setColor(bridge.getRgb());
        boxColor.repaint();
    }


    /**
     * 修改界面的颜色文本信息、颜色框、面板
     *
     * @param bridge 转换桥
     */
    public void updateAll(IConvertBridge bridge) {
        updateInfo(bridge);
        panelHsb.updatePanel(bridge);
        panelRgb.updatePanel(bridge);
    }

    /**
     * 窗口启动的初始化
     */
    public void formLoad() {
        this.setLocationRelativeTo(null);
        this.setTitle(ColorBean.APPLICATION_NAME);
        this.setSize(380, 520);
        this.setResizable(false);
        this.setContentPane(panelRoot);
        palette.autoRelateLabel(boxColor);
        panelHsb = new HsbPanel();
        panelRgb = new RgbPanel();
        panelRgb.setUpdateCallback(bridge -> {
            // 更新主界面面板信息
            updateInfo(bridge);
            // 单独更新 HSB 面板
            panelHsb.updatePanel(bridge);
        });
        panelHsb.setUpdateCallback(bridge -> {
            // 更新主界面面板信息
            updateInfo(bridge);
            // 单独更新 RGB 面板
            panelRgb.updatePanel(bridge);
        });
        tabPalette.setOpaque(false);
        tabPalette.setBackground(new Color(0x363636));
        tabPalette.addTab("HSB", panelHsb);
        tabPalette.addTab("RGB", panelRgb);
        if (Common.getOs() != Common.OsType.MAC) {
            this.setIconImage(new ImageIcon(Common.getURL("res/icon.png")).getImage());
        }
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        /*
            设置初始化和刷新面板信息
         */
        // 从设置中读取颜色并刷新整个主窗体的内容
        Rgb rgb = Rgb.fromHex(Option.ColorBackup());
        if (rgb == null) {
            updateAll(NormalConvertBridge.fromRgb(255, 255, 255));
        } else {
            updateAll(new NormalConvertBridge(rgb));
        }
        this.setVisible(true);
    }

    /**
     * 创建弹出式菜单
     *
     * @return 菜单项
     */
    private JPopupMenu getMenu() {
        JPopupMenu topMenu = new JPopupMenu();
        // 创建子菜单选项
        JCheckBoxMenuItem itemKeepTop = new JCheckBoxMenuItem("置顶", Option.AlwaysOnTop());
        JCheckBoxMenuItem itemHexPrefix = new JCheckBoxMenuItem("Hex值前显示\"#\"", Option.HexShowSym());
        JCheckBoxMenuItem itemDecimalRgb = new JCheckBoxMenuItem("RGB值以小数形式显示", Option.RGBFloat());
        JCheckBoxMenuItem itemDecimalHsb = new JCheckBoxMenuItem("HSB值以小数形式显示", Option.HSBFloat());
        JCheckBoxMenuItem itemRestoreColor = new JCheckBoxMenuItem("启动时恢复上次颜色", Option.Restore());
        JMenuItem itemExit = new JMenuItem("退出");
        // 二级菜单
        JRadioButtonMenuItem grayRgb = new JRadioButtonMenuItem("RGB空间分量算法");
        JRadioButtonMenuItem grayAverage = new JRadioButtonMenuItem("平均值算法");
        ButtonGroup bg = new ButtonGroup();
        bg.add(grayRgb);
        bg.add(grayAverage);
        JMenu subMenuGrayMethod = new JMenu("灰度值计算方式");
        subMenuGrayMethod.add(grayRgb);
        subMenuGrayMethod.add(grayAverage);
        if (Option.GrayCal() == Option.GrayscaleMethod.SPACE_COMPONENT) {
            grayRgb.setSelected(true);
        } else {
            grayAverage.setSelected(true);
        }
        // 添加菜单项
        topMenu.add(itemKeepTop);
        topMenu.addSeparator();
        topMenu.add(itemHexPrefix);
        topMenu.add(itemDecimalRgb);
        topMenu.add(itemDecimalHsb);
        topMenu.add(subMenuGrayMethod);
        topMenu.add(itemRestoreColor);
        topMenu.addSeparator();
        topMenu.add(itemExit);
        // 点击事件
        itemKeepTop.addActionListener(e -> {
            boolean v = !Option.AlwaysOnTop();
            Option.AlwaysOnTop(v);
            setAlwaysOnTop(v);
        });
        itemHexPrefix.addActionListener(e -> {
            // Hex前显示'#'
            Option.HexShowSym(!Option.HexShowSym());
            updateInfo(bridge);
        });
        itemDecimalRgb.addActionListener(e -> {
            // RGB值以小数形式显示
            Option.RGBFloat(!Option.RGBFloat());
            updateInfo(bridge);
        });
        itemDecimalHsb.addActionListener(e -> {
            // HSB值以小数形式显示
            Option.HSBFloat(!Option.HSBFloat());
            updateInfo(bridge);
        });
        grayRgb.addActionListener(e -> {
            // 灰度值算法 分量
            Option.GrayCal(Option.GrayscaleMethod.SPACE_COMPONENT);
            ModelsManager.register(Rgb.class, Grayscale.class, new GraySpaceComponentAlgorithm());
            Grayscale g = ModelsManager.convert(bridge.getRgb(), Grayscale.class);
            txtGray.setText(String.valueOf(g.value()));
        });
        grayAverage.addActionListener(e -> {
            // 灰度值算法 平均值
            Option.GrayCal(Option.GrayscaleMethod.AVERAGE);
            ModelsManager.register(Rgb.class, Grayscale.class, new GrayAverageAlgorithm());
            Grayscale g = ModelsManager.convert(bridge.getRgb(), Grayscale.class);
            txtGray.setText(String.valueOf(g.value()));
        });
        itemRestoreColor.addActionListener(e -> Option.Restore(!Option.Restore()));
        itemExit.addActionListener(e -> System.exit(0));
        return topMenu;
    }

}
