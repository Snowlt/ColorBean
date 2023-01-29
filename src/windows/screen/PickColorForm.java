package windows.screen;

import colorpad.core.model.Hsb;
import colorpad.extend.IConvertBridge;
import toolkit.ColorTool;
import toolkit.Common;
import windows.component.ChangeColor;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import static java.awt.MouseInfo.getPointerInfo;


public class PickColorForm extends JFrame implements MouseInputListener {

    // 标签
    private JLabel labRgb, labHsb, labColor;
    // 面板
    private JPanel panRoot, panInfo;
    // 颜色对象 用于返回给调用的类
    private Color color;
    // 图片对象  用于画背景
    private final Image backgroundImage;
    private ChangeColor onChangeColor;
    private Runnable onDispose;

    /**
     * static方法
     * 获取颜色值 返回一个颜色对象
     *
     * @return Color
     */
    public static Color GetPixel() {
        Point mousePoint;
        Color pixel;
        Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            return Color.BLACK;
        }
        mousePoint = getPointerInfo().getLocation();
        pixel = robot.getPixelColor(mousePoint.x, mousePoint.y);
        return pixel;
    }

    /**
     * 返回Color对象给调用者
     *
     * @return 颜色
     */
    public Color getColor() {
        return color;
    }

    /**
     * 设置改变颜色时的回调
     *
     * @param onChangeColor 函数
     */
    public void setOnChangeColor(ChangeColor onChangeColor) {
        this.onChangeColor = onChangeColor;
    }

    /**
     * 设置窗口关闭时的回调
     *
     * @param onDispose 函数
     */
    public void setOnDispose(Runnable onDispose) {
        this.onDispose = onDispose;
    }

    /**
     * 通过传入的图片对象来创建窗体
     *
     * @param backgroundImage 要显示的图像
     */
    PickColorForm(Image backgroundImage) {
        setTitle("从屏幕取色");
        this.backgroundImage = backgroundImage;
        initFrame();
    }

    /**
     * 初始化窗口布局内容
     */
    private void initFrame() {
        panRoot = new PicturePanel();
        panRoot.setOpaque(true);
        this.getContentPane().add(panRoot);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        panRoot.setLayout(null);
        panInfo = new JPanel();
        // 设置显示颜色信息的浮动标签
        labRgb = new JLabel();
        labColor = new JLabel();
        labHsb = new JLabel();
        // 提示鼠标操作
        JLabel labNotice = new JLabel("左键取色 右键退出");
        panInfo.setLayout(null);
        panInfo.add(labNotice);
        panInfo.add(labRgb);
        panInfo.add(labHsb);
        panInfo.add(labColor);
        labColor.setOpaque(true);

        // 字体设置
        Font font = labRgb.getFont();
        String fontName = Common.getFontName();
        labRgb.setFont(new Font(fontName, font.getStyle(), font.getSize() + 4));
        labHsb.setFont(labRgb.getFont());
        labNotice.setFont(new Font(fontName, font.getStyle(), font.getSize() - 2));
        // 大小设置
        labNotice.setSize(160, 10);
        labRgb.setSize(160, 20);
        labHsb.setSize(labRgb.getSize());
        labColor.setSize(labRgb.getWidth(), 30);
        // 设置样式
        labRgb.setHorizontalAlignment(JLabel.CENTER);
        labHsb.setHorizontalAlignment(JLabel.CENTER);
        labNotice.setHorizontalAlignment(JLabel.CENTER);
        labColor.setBorder(BorderFactory.createLineBorder(new Color(0x535353), 2));
        int infoW = 0, infoH = 0;
        int gap = 4, margin = 2;
        // 计算宽高
        infoH += margin;
        for (Component component : panInfo.getComponents()) {
            component.setLocation(margin, infoH);
            infoW = Math.max(infoW, component.getWidth());
            infoH += component.getHeight() + gap;
        }
        infoH -= panInfo.getComponentCount() > 0 ? gap : 0;
        infoH += margin;
        infoW += 2 * margin;
        // 设置位置和宽高
        panInfo.setBounds(-infoW - 1, 0, infoW, infoH);
        // 添加到窗体
        panRoot.add(panInfo);
        panRoot.addMouseListener(this);
        panRoot.addMouseMotionListener(this);
        /*  鼠标拖动和移动的监听器*/
        // 按键监听
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                }
            }
        });
        // 全屏后需要以下方式进行响应按键
        // https://www.coder.work/article/5158801
        String keyPress = "keyPress";
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), keyPress);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Color color = GetPixel();
        labColor.setBackground(color);
        IConvertBridge bridge = ColorTool.toBridge(color);
        final String separator = ",";
        labRgb.setText(MessageFormat.format("RGB({0})", bridge.getRgb().toString(separator)));
        Hsb hsb = bridge.getHsb();
        labHsb.setText(String.format("HSB(%d,%d,%d)", Math.round(hsb.h()), Math.round(hsb.s()), Math.round(hsb.b())));
        this.color = color;
        final int offset = 16;
        // 当鼠标在右边 并且label无法正常显示
        int mouseX = e.getX();
        int mouseY = e.getY();
        int panX = mouseX + offset, panY = mouseY + offset;
        if (mouseX + panInfo.getWidth() + 2 * offset >= panRoot.getWidth()) {
            panX -= panInfo.getWidth() + 2 * offset;
        }
        if (mouseY + panInfo.getHeight() + 2 * offset >= panRoot.getHeight()) {
            panY -= panInfo.getHeight() + 2 * offset;
        }
        panInfo.setLocation(panX, panY);
    }

    @Override
    public void dispose() {
        try {
            super.dispose();
        } finally {
            if (onDispose != null) {
                onDispose.run();
            }
        }
    }

    /**
     * 继承JPanel类 来实现绘制背景图片上去
     */

    class PicturePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (onChangeColor != null) {
                onChangeColor.change(color);
            }
            dispose();
        } else
            dispose();
    }

    @Override
    public void mousePressed(MouseEvent e) {

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

    /**
     * 屏幕截图
     */
    public static PickColorForm createScreenCapture() {
        ImageIcon image = getScreenImage();
        if (image == null) {
            throw new UnsupportedOperationException("无法获取屏幕图像");
        }
        // 将图像实例赋给backgroundImage
        PickColorForm form = new PickColorForm(image.getImage());
        Common.OsType os = Common.getOs();
        if (os == Common.OsType.WINDOWS) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            form.setSize(screenSize);
            form.setUndecorated(true);
            form.setVisible(true);
            form.setAlwaysOnTop(true);
        } else if (os == Common.OsType.MAC) {
            form.setUndecorated(true);
            form.setVisible(true);
            form.repaint();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            gd.setFullScreenWindow(form);
        } else {
            throw new UnsupportedOperationException("Not support this OS");
        }
        return form;
    }

    private static ImageIcon getScreenImage() {
        // 得到屏幕尺寸信息
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // 创建一个Rectangle(区域)
        Rectangle screenRectangle = new Rectangle(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
        Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            return null;
        }
        // 截图 并缓存在程序的内存中
        BufferedImage screen = robot.createScreenCapture(screenRectangle);
        // 获得背景图像
        return new ImageIcon(screen);
    }

    @Deprecated
    private static ImageIcon loadLinuxScreen() {
        String command = "import -window root -quality 90 ";
        String path = "/tmp/cpc_screen.jpg";
        boolean sus = false;
        try {
            sus = Runtime.getRuntime().exec(command + path).waitFor(1, TimeUnit.SECONDS);
        } catch (InterruptedException | IOException e) {
//            e.printStackTrace();
        }
        if (!sus) {
            return null;
        }
        File file = new File(path);
        if (file.exists()) {
            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            file.delete();
            return icon;
        } else {
            return null;
        }
    }

}

