package main;

import colorpad.core.Formula;
import colorpad.core.FormulaType;
import colorpad.core.ModelsManager;
import colorpad.core.model.*;
import colorpad.extend.ColorType;
import colorpad.extend.IConvertBridge;
import colorpad.extend.NormalConvertBridge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CommandLine {

    private static final String[] options = {"-h", "-c", "-s"}, modes = {"rgb", "hsb", "hex", "cmyk"};

    private static final String COLOR_STRING_SEPARATOR = ",";

    public static void run(String[] args) {
        System.out.println("ColorBean - Command Line Mode\n");
        ArrayList<String> option = new ArrayList<>();
        String cMode = null, cValue = null;
        for (int i = 0; i < args.length; i++) {
            switch (argumentType(args[i])) {
                case 0:
                    System.out.printf("Unknown argument: \"%s\"\n", args[i]);
                    System.out.println("Use \"-h\" to get help\n");
                    System.exit(0);
                    break;
                case 1:
                    option.add(args[i]);
                    break;
                case 2:
                    if (i + 1 >= args.length) {
                        System.out.printf("Wrong argument: Missing color value after: \"%s\"\n", args[i]);
                        System.exit(0);
                    }
                    cMode = args[i];
                    cValue = args[i + 1];
                    i++;
                    break;
            }
        }
        // 帮助
        if (option.contains(options[0])) {
            help();
        }
        // 提取出颜色
        IConvertBridge bridge = null;
        try {
            if (ColorType.RGB.displayName().equalsIgnoreCase(cMode)) {
                bridge = new NormalConvertBridge(Rgb.fromString(cValue));
            } else if (ColorType.HEX.displayName().equalsIgnoreCase(cMode)) {
                bridge = new NormalConvertBridge(Rgb.fromHexEnhanced(cValue));
            } else if (ColorType.HSB.displayName().equalsIgnoreCase(cMode)) {
                bridge = new NormalConvertBridge(Hsb.fromString(cValue));
            } else if (ColorType.HSL.displayName().equalsIgnoreCase(cMode)) {
                bridge = new NormalConvertBridge(Hsl.fromString(cValue));
            } else if (ColorType.CMYK.displayName().equalsIgnoreCase(cMode)) {
                bridge = new NormalConvertBridge(Cmyk.fromString(cValue));
            } else if (ColorType.Y_CR_CB.displayName().equalsIgnoreCase(cMode)) {
                bridge = new NormalConvertBridge(YCrCb.fromString(cValue));
            } else if (ColorType.CIE_LAB.displayName().equalsIgnoreCase(cMode)) {
                bridge = new NormalConvertBridge(Lab.fromString(cValue));
            } else if (ColorType.XYZ.displayName().equalsIgnoreCase(cMode)) {
                bridge = new NormalConvertBridge(Xyz.fromString(cValue));
            }
        } catch (IllegalArgumentException ignored) {
        }

        // 转换颜色
        boolean needConvert = option.contains(options[1]);
        boolean needScheme = option.contains(options[2]);
        if (bridge == null && (needConvert || needScheme)) {
            if (cMode == null) {
                System.out.println("Convert failed: Missing color type");
            } else {
                System.out.println("Convert from \"%s\" failed: Color value might be wrong");
            }
            System.exit(0);
        }
        if (needConvert) {
            convert(bridge);
        }
        // 配色方案
        if (needScheme) {
            scheme(bridge);
        }
    }

    /**
     * 获取参数的类型
     *
     * @param arg 参数（字符串）
     * @return 0 - 未知参数 / 1 - 选项 / 2 - 颜色模式
     */
    public static int argumentType(String arg) {
        for (String command : options) {
            if (command.equals(arg)) {
                return 1;
            }
        }
        for (String command : modes) {
            if (command.equals(arg)) {
                return 2;
            }
        }
        return 0;
    }

    /**
     * 显示程序的帮助信息
     */
    public static void help() {
        System.out.println("Usage: cpc [Option] Type Value");
        System.out.println("Options:");
        System.out.println("          -h, Show help");
        System.out.println("          -c, Convert the given color type to others");
        System.out.println("          -s, Show color scheme of the given color");
        String supportTypes = Arrays.stream(ColorType.values()).map(ColorType::displayName)
                .collect(Collectors.joining("/"));
        System.out.println("Color type(Ignore case): Choose one in " + supportTypes);
        System.out.println("Color value: The value of color, use \",\" to separate");
        System.out.println("\n    For example: cpc -c -s RGB 128,128,128");
        System.out.println();
    }

    /**
     * 将颜色转换到其他颜色模型
     *
     * @param bridge 转换桥
     */
    public static void convert(IConvertBridge bridge) {
        System.out.println("=== Color Conversion ===");
        System.out.println("RGB:      " + bridge.getRgb().toString(COLOR_STRING_SEPARATOR));
        System.out.println("Hex:      #" + bridge.toHex());
        System.out.println("HSB(HSV): " + bridge.getHsb().toString(COLOR_STRING_SEPARATOR));
        System.out.println("HSL:      " + bridge.getHsl().toString(COLOR_STRING_SEPARATOR));
        System.out.println("CMYK:     " + bridge.getCmyk().toString(COLOR_STRING_SEPARATOR));
        System.out.println("YCrCb:    " + bridge.getYCrCb().toString(COLOR_STRING_SEPARATOR));
        System.out.println("CIE-Lab:  " + bridge.getLab().toString(COLOR_STRING_SEPARATOR));
        System.out.println("CIE-XYZ:  " + bridge.getXyz().toString(COLOR_STRING_SEPARATOR));
        System.out.println();
    }

    /**
     * 获取该颜色的配色方案
     *
     * @param bridge 转换桥
     */
    public static void scheme(IConvertBridge bridge) {
        Hsb hsb = bridge.getHsb();
        System.out.println("=== Color Scheme ===");
        System.out.printf("Original color: RGB - %s\n", bridge.getRgb().toString(COLOR_STRING_SEPARATOR));
        System.out.printf("                HSB - %s\n", hsb.toString(COLOR_STRING_SEPARATOR));
        for (FormulaType type : FormulaType.values()) {
            Hsb[] schemes = Formula.GetFormula(hsb, type, null);
            System.out.printf("Scheme %s (%d point): \n", type.name(), schemes.length);
            for (int i = 0; i < schemes.length; i++) {
                System.out.printf("    Color %d: RGB - %s, HSB - %s\n", i + 1,
                        ModelsManager.convert(schemes[i], Rgb.class).toString(COLOR_STRING_SEPARATOR),
                        schemes[i].toString(COLOR_STRING_SEPARATOR));
            }
        }
        System.out.println();
    }
}
