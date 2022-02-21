package colorpad.core.model;

/**
 * Interfaces Of Color Models
 * 颜色模型的接口
 */
public interface IToString {
    /**
     * Make a string of Color Model
     * 生成表示颜色模型的字符串
     *
     * @param separator the separator 分隔符
     * @return Text of color 表示颜色的字符串
     */
    String toString(String separator);
}
