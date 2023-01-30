package colorpad.core.model;

import colorpad.core.ArgumentOutOfRangeException;
import colorpad.core.Basic;

import java.text.MessageFormat;

/**
 * Represents YCrCb Color Model
 * 表示 YCrCb 颜色模型
 */
public final class YCrCb implements IColorModel {

    private final int y;
    private final int cr;
    private final int cb;

    /**
     * Y (0 - 255)
     */
    public int y() {
        return y;
    }

    /**
     * Cr (0 - 255)
     */
    public int cr() {
        return cr;
    }

    /**
     * Cb (0 - 255)
     */
    public int cb() {
        return cb;
    }

    /**
     * Initialize the YCrCb object
     * 初始化 YCrCb 对象
     *
     * @param y  Y (0 - 255)
     * @param cr Cr (0 - 255)
     * @param cb Cb (0 - 255)
     */
    private YCrCb(int y, int cr, int cb) {
        this.y = y;
        this.cr = cr;
        this.cb = cb;
    }

    public boolean compareWith(int y, int cr, int cb) {
        return y() == y && cr() == cr && cb() == cb;
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

    @Override
    public String toString() {
        return MessageFormat.format("YCrCb: ({0},{1},{2})", y(), cr(), cb());
    }

    @Override
    public String toString(String separator) {
        return MessageFormat.format("{1}{0}{2}{0}{3}", separator, y(), cr(), cb());
    }

    /**
     * Create the YCrCb model
     * 创建 YCrCb 模型
     *
     * @param y  Y (0 - 255)
     * @param cr Cr (0 - 255)
     * @param cb Cb (0 - 255)
     * @return YCrCb
     * @throws ArgumentOutOfRangeException Value out of range / 数值超出范围
     */
    public static YCrCb from(int y, int cr, int cb) {
        checkRange(y, cr, cb);
        return new YCrCb(y, cr, cb);
    }

    /**
     * Parse out the YCrCb value from a string
     * 从一个字符串解析出 YCrCb 值
     *
     * @param color String of YCrCb / YCrCb 字符串
     * @return YCrCb
     * @throws IllegalArgumentException    Unable to parse / 无法解析
     * @throws ArgumentOutOfRangeException Value out of range / 数值超出范围
     */
    public static YCrCb fromString(String color) {
        int[] cm = Basic.extractFromStringAsInt(color);
        if (cm.length != 3) throw new IllegalArgumentException();
        checkRange(cm[0], cm[1], cm[2]);
        return new YCrCb(cm[0], cm[1], cm[2]);
    }

    private static void checkRange(int y, int cr, int cb) {
        if (y < 0 || y > 255 || cr < 0 || cr > 255 || cb < 0 || cb > 255)
            throw new ArgumentOutOfRangeException();
    }
}
