package colorpad.core.model;

import colorpad.core.ModelsManager;

/**
 * Interfaces Of Color Models
 * 颜色模型的接口
 */
public interface IColorModel {
    /**
     * Make a string of Color Model
     * 生成表示颜色模型的字符串
     *
     * @param separator the separator 分隔符
     * @return Text of color 表示颜色的字符串
     */
    String toString(String separator);

    /**
     * Convert to target model type
     * 转换到目标颜色模型
     *
     * @param <T>         target type parameter
     * @param targetClass Class of target model 目标颜色模型的Class
     * @return Target model 目标颜色模型
     * @throws IllegalArgumentException No converter found 找不到转换器
     * @see ModelsManager#convert(Object, Class)
     */
    default <T> T convertTo(Class<T> targetClass) {
        return ModelsManager.convert(this, targetClass);
    }
}
