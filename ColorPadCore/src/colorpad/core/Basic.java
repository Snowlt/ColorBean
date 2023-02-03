package colorpad.core;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * Basic Functions For Conversion And Color Models
 *
 * @author Snow
 */
public class Basic {

    /**
     * The PRECISION of decimal number(double type) comparison
     */
    public static final double PRECISION = 0.000001d;
    private static final MessageFormat DECIMAL_TWO_POINT_FORMATTER =
            new MessageFormat("{1,number,0.##}{0}{2,number,0.##}{0}{3,number,0.##}");
    private static final double[] EMPTY_DOUBLE_ARRAY = new double[0];

    public static int fIntValue(String s) {
        int num = 0;
        boolean negative = s.charAt(0) == '-';
        for (char c : s.toCharArray()) {
            if (c >= '0' && c <= '9') {
                num = num * 10 + (c - '0');
            }
        }
        return negative ? -num : num;
    }

    public static int fRound(double value) {
        return (int) Math.round(value);
    }

    public static int getFixRange(int value, int lower, int upper) {
        if (value > upper) {
            value = upper;
        } else if (value < lower) {
            value = lower;
        }
        return value;
    }

    public static double getFixRange(double value, double lower, double upper) {
        if (value > upper) {
            value = upper;
        } else if (value < lower) {
            value = lower;
        }
        return value;
    }

    public static boolean decimalEquals(double left, double right) {
        return Math.abs(left - right) < PRECISION;
    }

    public static double[] extractFromString(String color) {
        if (isEmpty(color)) {
            return EMPTY_DOUBLE_ARRAY;
        }
        // 处理并分割字符串
        String[] aString = color.split(",");
        // 转换为整型数组
        double[] aDouble = new double[aString.length];
        for (int i = 0; i < aString.length; i++) {
            try {
                aDouble[i] = Double.parseDouble(aString[i]);
            } catch (ArithmeticException | NumberFormatException ex) {
                return EMPTY_DOUBLE_ARRAY;
            }
        }
        return aDouble;
    }

    public static int[] extractFromStringAsInt(String color) {
        // 转换为整型数组
        return Arrays.stream(Basic.extractFromString(color)).mapToInt(d -> (int) Math.round(d)).toArray();
    }

    public static int parseHex(String s) {
        try {
            return Integer.parseInt(s, 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
    }

    public static String padLeft(String target, int length, char filled) {
        final StringBuilder builder = new StringBuilder(Math.max(target.length(), length));
        for (int i = 0; i < length - target.length(); i++) {
            builder.append(filled);
        }
        builder.append(target);
        return builder.toString();
    }

    public static String decimalFormat(String separator, double a, double b, double c) {
        return DECIMAL_TWO_POINT_FORMATTER.format(new Object[]{separator, a, b, c}, new StringBuffer(), null).toString();
    }

    static final char[] HEX_UPPER_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    static final char[] HEX_LOWER_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Convert number to hexadecimal string of length 6 (leading with zero if length is less than 6)
     *
     * <p>The unsigned integer value is the argument plus 2<sup>24</sup> if the argument is negative</p>
     * <p>This method is almost always faster than using Integer.toHexString and string concat</p>
     *
     * @param n     number
     * @param upper if true, return uppercase string
     * @return hex string
     */
    public static String toLengthSixHex(int n, boolean upper) {
        char[] chars = upper ? HEX_UPPER_CHARS : HEX_LOWER_CHARS;
        char[] t = new char[6];
        for (int i = t.length - 1; i >= 0; i--) {
            t[i] = chars[n & ((1 << 4) - 1)];
            n >>>= 4;
        }
        return new String(t);
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

}
