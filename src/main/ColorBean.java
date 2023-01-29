package main;

import option.Option;
import toolkit.Common;
import windows.MainForm;
import windows.component.GradientColorSlider;

import javax.swing.*;

public class ColorBean {

    public static void main(String[] args) {
        if (args.length != 0) CommandLine.run(args);
        else loadGui();
    }

    private static void loadGui() {
        try {
            GradientColorSlider.testPermission();
        } catch (BootError e) {
            if (e.canResume()) {
                JOptionPane.showMessageDialog(null,
                        "An error occurred: \n" + e.getNoticeMessage(),
                        "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Cannot start " + ColorBean.APPLICATION_NAME + ": \n" + e.getNoticeMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(1);
            }
        }

        if (Common.getOs() == Common.OsType.WINDOWS) {
            UIManager.LookAndFeelInfo[] laf = UIManager.getInstalledLookAndFeels();
            for (UIManager.LookAndFeelInfo info : laf) {
                String name = info.getName();
                if (name.contains("Windows") && !name.contains("Classic")) {
                    try {
                        UIManager.setLookAndFeel(info.getClassName());
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                             UnsupportedLookAndFeelException e) {
                        e.printStackTrace();
                        System.err.println("Failed to set Windows visual style.");
                    }
                    break;
                }
            }
        }

        Option.loadOption();
        MainForm window = new MainForm();
        window.formLoad();
    }

    public static final String APPLICATION_NAME = "ColorBean";
    public static final String APPLICATION_VERSION = "2.0";

}
