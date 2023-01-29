package fit.simplification;

/**
 * 辅助转换的工具
 * 提供类似于 .Net 中 System.Convert 的相关方法
 *
 * @author Snow
 * @version 1.3
 * @since 2020/9/5
 */
public class Convert {

    /**
     * 转换到整型
     *
     * @param value 值
     * @param def   转换失败时，返回的默认值
     * @return 转换后的结果
     */
    public static Integer toInt(Object value, Integer def) {
        try {
            if (value == null) {
                return def;
            } else if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            return Integer.parseInt(value.toString());
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 转换到整型，转换失败时返回 0
     *
     * @param value 值
     * @return 转换后的结果
     */
    public static Integer toInt(Object value) {
        return toInt(value, 0);
    }

    /**
     * 转换到长整型
     *
     * @param value 值
     * @param def   转换失败时，返回的默认值
     * @return 转换后的结果
     */
    public static Long toLong(Object value, Long def) {
        try {
            if (value == null) {
                return def;
            } else if (value instanceof Number) {
                return ((Number) value).longValue();
            }
            return Long.parseLong(value.toString());
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 转换到长整型，转换失败时返回 0
     *
     * @param value 值
     * @return 转换后的结果
     */
    public static Long toLong(Object value) {
        return toLong(value, 0L);
    }

    /**
     * 转换到单精度浮点型
     *
     * @param value 值
     * @param def   转换失败时，返回的默认值
     * @return 转换后的结果
     */
    public static Float toFloat(Object value, Float def) {
        try {
            if (value == null) {
                return def;
            } else if (value instanceof Number) {
                return ((Number) value).floatValue();
            }
            return Float.parseFloat(value.toString());
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 转换到单精度浮点型，转换失败时返回 0
     *
     * @param value 值
     * @return 转换后的结果
     */
    public static Float toFloat(Object value) {
        return toFloat(value, 0F);
    }

    /**
     * 转换到双精度浮点型
     *
     * @param value 值
     * @param def   转换失败时，返回的默认值
     * @return 转换后的结果
     */
    public static Double toDouble(Object value, Double def) {
        try {
            if (value == null) {
                return def;
            } else if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
            return Double.parseDouble(value.toString());
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 转换到双精度浮点型，转换失败时返回 0
     *
     * @param value 值
     * @return 转换后的结果
     */
    public static Double toDouble(Object value) {
        return toDouble(value, 0D);
    }

    /**
     * 转换到布尔值(null 时返回<i>默认值</i>)
     *
     * @param value 值
     * @param def   转换失败时，返回的默认值
     * @return 转换后的结果
     */
    public static Boolean toBoolean(Object value, Boolean def) {
        try {
            if (value == null) {
                return def;
            } else if (value instanceof Number) {
                return toBoolean((Number) value);
            } else if (value instanceof Boolean) {
                return (Boolean) value;
            }
            return "true".equalsIgnoreCase(value.toString());
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 转换到布尔值，转换失败或为空时返回 false
     *
     * @param value 值
     * @return 转换后的结果
     */
    public static Boolean toBoolean(Object value) {
        return toBoolean(value, false);
    }

    /**
     * 将数字转换到布尔值，为 0 或 null 时返回 false，否则返回 true
     *
     * @param value 值
     * @return 转换后的结果
     */
    public static Boolean toBoolean(Number value) {
        return value != null && value.doubleValue() != 0.0;
    }

    /**
     * 枚举转为整数（序号），使用 {@link Enum#ordinal()}
     *
     * @param enumerate 枚举
     * @return 转换后的结果
     */
    public static Integer enumToInt(Enum<?> enumerate) {
        return enumerate != null ? enumerate.ordinal() : null;
    }

    /**
     * 尝试将转为
     *
     * @param <T>      枚举类型（泛型）
     * @param enumType 枚举的Class
     * @param ordinal  枚举的序号
     * @param def      转换失败时，返回的默认值
     * @return 转换后的结果
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T toEnumByImplicit(Class<? extends Enum<?>> enumType, Integer ordinal, T def) {
        if (enumType == null) {
            throw new IllegalArgumentException("enumType cannot be null");
        }
        if (ordinal == null) return def;
        try {
            Enum<?>[] values = enumType.getEnumConstants();
            if (values == null || ordinal < 0 || ordinal >= values.length) {
                return def;
            }
            return (T) values[ordinal];
        } catch (Exception e) {
            return def;
        }
    }

    public static <T extends Enum<T>> T toEnum(Class<T> enumType, Integer ordinal, T def) {
        return toEnumByImplicit(enumType, ordinal, def);
    }
}
