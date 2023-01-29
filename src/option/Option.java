package option;

import fit.ini.FitIni;
import fit.ini.Ini;
import fit.ini.Section;
import fit.simplification.Check;
import fit.simplification.Convert;
import toolkit.ReflectionUtil;

import java.io.File;
import java.lang.reflect.Field;

public class Option {

    private final static String FILENAME = "config.ini";
    private static final String CONFIG_PATH = baseDirPath() + "/" + FILENAME;
    private static final String DEFAULT_COLOR = "FFFFFF";

    public enum GrayscaleMethod {
        SPACE_COMPONENT,
        AVERAGE
    }

    static class SectionOption {
        @IniKey("ShowPrefix")
        boolean hex = true;
        @IniKey("ShowRGBFloat")
        boolean rgb;
        @IniKey("ShowHSBFloat")
        boolean hsb;
        @IniKey("Restore")
        boolean restore = true;
        @IniKey("CalGray")
        GrayscaleMethod grayMethod = GrayscaleMethod.SPACE_COMPONENT;
    }

    static class SectionBackup {
        /**
         * 保存的颜色
         */
        @IniKey("Color")
        String color = DEFAULT_COLOR;
    }

    private static final SectionOption sectionOption = new SectionOption();
    private static final SectionBackup sectionBackup = new SectionBackup();
    private static boolean top = false;


    /**
     * 从文件加载设置（启动时调用一次）
     */
    public static void loadOption() {
        File file = new File(CONFIG_PATH);
        if (file.exists()) {
            try {
                loadFromFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 获取当前程序的运行路径
    private static String baseDirPath() {
        return new File("").getAbsolutePath();
    }

    // 从文件载入
    private static void loadFromFile() {
        Ini ini = FitIni.loadFromFileSimply(FILENAME);
        // 获得ini文件中第一个section
        Section iniSection = ini.getSection("Option");
        readFromSection(iniSection, sectionOption);
        if (sectionOption.restore) {
            Section backup = ini.getSection("Backup");
            readFromSection(backup, sectionBackup);
            if (Check.isEmpty(sectionBackup.color)) {
                sectionBackup.color = DEFAULT_COLOR;
            }
        }
    }

    /**
     * 保存设置到文件（退出时调用一次）
     */
    public static void saveOption() {
        Ini ini = FitIni.loadFromFile(FILENAME);
        // 添加ini文件中 Option section
        Section iniSection = ini.getOrAdd("Option");
        saveToSection(iniSection, sectionOption);
        // 添加ini文件中 Backup section
        if (sectionOption.restore) {
            iniSection = ini.getOrAdd("Backup");
            saveToSection(iniSection, sectionBackup);
        }
        ini.saveToFile(FILENAME);
    }

    @SuppressWarnings("unchecked")
    private static void readFromSection(Section iniSection, Object sectionBean) {
        // 得到这个section中所有的key值
        for (Field field : sectionBean.getClass().getDeclaredFields()) {
            IniKey key = field.getAnnotation(IniKey.class);
            if (key == null) {
                continue;
            }
            String itemValue = iniSection.getItem(key.value());
            if (itemValue == null || itemValue.isEmpty()) {
                continue;
            }
            try {
                field.setAccessible(true);
                if (field.getType().isEnum()) {
                    int ord = Integer.parseInt(itemValue);
                    Enum<?> value = Convert.toEnumByImplicit((Class<Enum<?>>) field.getType(), ord, null);
                    field.set(null, value);
                } else {
                    ReflectionUtil.setFieldValueFromString(field, sectionBean, itemValue);
                }
            } catch (IllegalAccessException | RuntimeException ignored) {
            }
        }
    }

    private static void saveToSection(Section iniSection, Object sectionBean) {
        for (Field field : sectionBean.getClass().getDeclaredFields()) {
            IniKey key = field.getAnnotation(IniKey.class);
            if (key == null) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object value = field.get(sectionBean);
                if (value instanceof Enum) {
                    value = ((Enum<?>) value).ordinal();
                }
                iniSection.setItem(key.value(), value);
            } catch (IllegalAccessException ignored) {
            }
        }
    }


    /**
     * 获取 置顶（不写入文件）
     *
     * @return 布尔值
     */
    public static boolean AlwaysOnTop() {
        return top;
    }

    /**
     * 设置 置顶（不写入文件）
     */
    public static void AlwaysOnTop(boolean value) {
        top = value;
    }

    /**
     * 获取 是否在 Hex 前显示 # 号
     *
     * @return 布尔值
     */
    public static boolean HexShowSym() {
        return sectionOption.hex;
    }

    /**
     * 设置 是否在 Hex 前显示 # 号
     */
    public static void HexShowSym(boolean value) {
        sectionOption.hex = value;
    }

    /**
     * 获取 RGB 是否以小数显示
     *
     * @return 布尔值
     */
    public static boolean RGBFloat() {
        return sectionOption.rgb;
    }

    /**
     * 设置 RGB 是否以小鼠显示
     */
    public static void RGBFloat(boolean value) {
        sectionOption.rgb = value;
    }

    /**
     * 获取 HSB 是否以小数显示
     *
     * @return 布尔值
     */
    public static boolean HSBFloat() {
        return sectionOption.hsb;
    }

    /**
     * 设置 HSB 是否以小数显示
     */
    public static void HSBFloat(boolean value) {
        sectionOption.hsb = value;
    }

    /**
     * 获取 灰度值计算方式
     *
     * @return 0 或 1
     */
    public static GrayscaleMethod GrayCal() {
        return sectionOption.grayMethod;
    }

    /**
     * 设置 灰度值计算方式
     */
    public static void GrayCal(GrayscaleMethod value) {
        sectionOption.grayMethod = value;
    }

    /**
     * 获取 退出时是否保存颜色
     *
     * @return 布尔值
     */
    public static boolean Restore() {
        return sectionOption.restore;
    }

    /**
     * 设置 下次打开程序时是否自动恢复颜色
     */
    public static void Restore(boolean value) {
        sectionOption.restore = value;
    }

    /**
     * 获取 上次关闭程序时的颜色
     *
     * @return RGB字符串
     */
    public static String ColorBackup() {
        return sectionBackup.color;
    }

    /**
     * 设置 下次打开程序时自动恢复的颜色
     */
    public static void ColorBackup(String value) {
        sectionBackup.color = value;
    }

}