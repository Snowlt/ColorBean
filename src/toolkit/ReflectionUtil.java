package toolkit;

import java.lang.reflect.Field;

/**
 * 反射工具
 *
 * @author Snow
 */
public class ReflectionUtil {
    /**
     * 尝试解析字符串，并写入到字段的值
     *
     * @param field  字段
     * @param target 写入的目标对象
     * @param value  值
     * @throws NumberFormatException    无法解析数字
     * @throws IllegalArgumentException 参数错误
     * @throws IllegalAccessException   反射异常
     */
    public static void setFieldValueFromString(Field field, Object target, String value) throws IllegalAccessException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        Class<?> type = field.getType();
        if (type.isAssignableFrom(String.class)) {
            field.set(target, value);
        } else if (type == Boolean.class || type == Boolean.TYPE) {
            field.setBoolean(target, Boolean.parseBoolean(value));
        } else if (type == Byte.class || type == Byte.TYPE) {
            field.set(target, Byte.parseByte(value));
        } else if (type == Short.class || type == Short.TYPE) {
            field.set(target, Short.parseShort(value));
        } else if (type == Integer.class || type == Integer.TYPE) {
            field.set(target, Integer.parseInt(value));
        } else if (type == Long.class || type == Long.TYPE) {
            field.set(target, Long.parseLong(value));
        } else if (type == Float.class || type == Float.TYPE) {
            field.set(target, Float.parseFloat(value));
        } else if (type == Double.class || type == Double.TYPE) {
            field.set(target, Double.parseDouble(value));
        } else if (type == Character.class || type == Character.TYPE) {
            if (value.length() != 1) {
                throw new IllegalArgumentException("Cannot convert to Character, String length is not 1");
            }
            field.set(target, value.charAt(0));
        } else {
            throw new IllegalArgumentException("Not support to convert String to target class type: " + type);
        }
    }

}
