package toolkit;

import fit.simplification.Check;
import fit.simplification.Convert;
import main.ColorBean;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.net.URL;

/**
 * 基础功能和共用函数类
 */
public class Common {

    /* 带异常处理的变量类型转换函数 */

    /**
     * 从字符串解析出 int
     *
     * @param s 要解析的字符串
     * @return 返回转换后的整形（如果出错会返回0）
     */
    public static int cInt(String s) {
        return Convert.toInt(s, 0);
    }

    public static String getValue(String raw) {
        StringBuilder fixed = new StringBuilder();
        for (int i = 0; i < raw.length(); i++) {
            if (Character.isDigit(raw.charAt(i))) {
                fixed.append(raw.charAt(i));
            }
        }
        return fixed.toString();
    }

    /* 消息和通知对话框 */

    /**
     * 显示一个消息框
     *
     * @param title 窗口标题
     * @param text  消息文本内容
     */
    public static void msgShow(String title, String text) {
        msgShow(title, text, null);
    }

    /**
     * 显示一个消息框
     *
     * @param title  窗口标题
     * @param text   消息文本内容
     * @param parent 上级窗口
     */
    public static void msgShow(String title, String text, Component parent) {
        JOptionPane.showMessageDialog(parent, text, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 显示一个确定取消的对话框
     *
     * @param title 窗口标题
     * @param text  消息文本内容
     * @return 返回是否点击了确定
     */
    public static boolean msgQuestion(String title, String text) {
        return msgQuestion(title, text, null);
    }

    /**
     * 显示一个确定取消的对话框
     *
     * @param title  窗口标题
     * @param text   消息文本内容
     * @param parent 上级窗口
     * @return 返回是否点击了确定
     */
    public static boolean msgQuestion(String title, String text, Component parent) {
        int choose = JOptionPane.showConfirmDialog(parent, text, title, JOptionPane.OK_CANCEL_OPTION);
        return choose == 0;
    }

    public static String msgInput(String title, String text) {
        return msgInput(title, text, null);
    }

    public static String msgInput(String title, String text, Component parent) {
        return (String) JOptionPane.showInputDialog(parent, text, title,
                JOptionPane.PLAIN_MESSAGE, null, null, null);
    }

    /* 系统剪切板 */

    /**
     * 获取系统剪切板的内容
     *
     * @return 返回字符串(不是文本会返回null)
     */
    public static String getSysClipboardText() {
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 获取剪切板的内容
        Transferable clipTf = sysClip.getContents(null);
        if (clipTf != null) {
            // 检查是否是文本类型
            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    return (String) clipTf.getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    return "";
                }
            }
        }
        return null;
    }

    /**
     * 把文本复制到系统剪切板
     *
     * @param text 要复制的文本
     */
    public static void setSysClipboardText(String text) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(text);
        clip.setContents(tText, null);
    }

    public static URL getURL(String res) {
        // 打成Jar包后，路径需要改变
        // System.out.println(ColorBean.class.getResource("../"+res));
        return ColorBean.class.getResource("/" + res);
    }

    /**
     * 获取操作系统类型（Windows/Linux/Mac/未知）
     *
     * @return 操作系统类型
     */
    public static OsType getOs() {
        String os = System.getProperty("os.name");
        if (Check.isEmpty(os)) return OsType.UNKNOWN;
        if (os.contains("Win")) {
            return OsType.WINDOWS;
        } else if (os.contains("Linux")) {
            return OsType.LINUX;
        } else if (os.contains("Mac")) {
            return OsType.MAC;
        }
        return OsType.UNKNOWN;
    }

    public static String getFontName() {
        switch (getOs()) {
            case WINDOWS:
                return "微软雅黑";
            case LINUX:
                return "Noto Sans CJK SC Regular";
            case MAC:
                return ".AppleSystemUIFont";
            default:
                return "Dialog";
        }
    }

    public enum OsType {
        WINDOWS, LINUX, MAC, UNKNOWN
    }
}
