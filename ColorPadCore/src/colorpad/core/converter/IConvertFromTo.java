package colorpad.core.converter;

/**
 * Standard convert method
 */
@FunctionalInterface
public interface IConvertFromTo<TSource, TTarget> {
    /**
     * Convert to target.
     *
     * @param source Source color model
     * @return Target color model
     */
    TTarget convert(TSource source);
}
