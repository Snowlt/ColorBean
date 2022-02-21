package colorpad.core.model;

import colorpad.core.Basic;

import java.text.MessageFormat;

/**
 * Class of YCrCb Color Model
 * 表示 YCrCb 颜色模型的类
 */
public class YCrCb implements IToString {

    private int y, cr, cb;

    /**
     * Y (0 - 255)
     */
    public int y() {
        return y;
    }

    /**
     * Y (0 - 255)
     */
    public void setY(int y) {
        this.y = Basic.getFixRange(y, 0, 255);
    }

    /**
     * Cr (0 - 255)
     */
    public int cr() {
        return cr;
    }

    /**
     * Cr (0 - 255)
     */
    public void setCr(int cr) {
        this.cr = Basic.getFixRange(cr, 0, 255);
    }

    /**
     * Cb (0 - 255)
     */
    public int cb() {
        return cb;
    }

    /**
     * Cb (0 - 255)
     */
    public void setCb(int cb) {
        this.cb = Basic.getFixRange(cb, 0, 255);
    }

    /**
     * Initialize the YCrCb object
     * 初始化 YCrCb 对象
     *
     * @param y  Y (0 - 255)
     * @param cr Cr (0 - 255)
     * @param cb Cb (0 - 255)
     */
    public YCrCb(int y, int cr, int cb) {
        this.setY(y);
        this.setCr(cr);
        this.setCb(cb);
    }

    public boolean compareWith(int y, int cr, int cb) {
        return y() == y && cr() == cr && cb() == cb;
    }

    @Override
    public String toString(String separator) {
        return MessageFormat.format("{1}{0}{2}{0}{3}", separator, y(), cr(), cb());
    }

    @Override
    public String toString() {
        return MessageFormat.format("YCrCb: ({0},{1},{2})", y(), cr(), cb());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YCrCb)) return false;
        YCrCb yCrCb = (YCrCb) o;
        return compareWith(yCrCb.y(), yCrCb.cr(), yCrCb.cb());
    }

    @Override
    public int hashCode() {
        return y() << 16 | cr() << 8 | cb();
    }

    /**
     * Parse out the YCrCb value from a string
     * 从一个字符串解析出 YCrCb 值
     *
     * @param color String of YCrCb / YCrCb 字符串
     * @return YCrCb Object / YCrCb 对象
     */
    public static YCrCb fromString(String color) {
        int[] values = Basic.extractFromStringAsInt(color);
        // 检查格式
        if (values.length != 3)
            return null;
        // 检查范围
        int y = values[0];
        int cr = values[1];
        int cb = values[2];
        if (y < 0 || y > 255 || cr < 0 || cr > 255 || cb < 0 || cb > 255) {
            return null;
        }
        // 返回对象
        return new YCrCb(y, cr, cb);
    }
}
