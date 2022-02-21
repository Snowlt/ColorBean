package colorpad.core;

import colorpad.core.algorithm.DefaultAlgorithm;
import colorpad.core.algorithm.GrayComponentAlgorithm;
import colorpad.core.algorithm.interfaces.IConvertFromTo;
import colorpad.core.algorithm.interfaces.IGetGray;

import java.util.HashMap;
import java.util.Map;

/**
 * Entry of process color models conversion
 *
 * @author Snow
 */
public final class ConversionManager {

    private static final Map<Class<?>, Map<Class<?>, IConvertFromTo<?, ?>>> REGISTERED_CONVERTERS = new HashMap<>();

    private static IGetGray grayscaleAlgorithm = new GrayComponentAlgorithm();

    /**
     * Get current grayscale algorithm
     *
     * @return Grayscale algorithm
     */
    public static IGetGray getGrayscaleAlgorithm() {
        return grayscaleAlgorithm;
    }

    /**
     * Replace the default algorithm of grayscale
     *
     * @param algorithm Grayscale algorithm
     */
    public static void registerGrayscaleAlgorithm(IGetGray algorithm) {
        grayscaleAlgorithm = algorithm;
    }

    /**
     * Register a convert method (converter) to convert from one color to another
     *
     * @param <TSource> Source type
     * @param <TTarget> Target type
     * @param source    Class of source model
     * @param target    Class of target model
     * @param converter Convert method(Converter)
     */
    public static <TSource, TTarget> void register(Class<TSource> source,
                                                   Class<TTarget> target,
                                                   IConvertFromTo<TSource, TTarget> converter) {
        if (source == null || target == null || converter == null)
            throw new NullPointerException("All arguments cannot be null");
        synchronized (REGISTERED_CONVERTERS) {
            Map<Class<?>, IConvertFromTo<?, ?>> targetMap =
                    REGISTERED_CONVERTERS.computeIfAbsent(source, k -> new HashMap<>());
            targetMap.put(target, converter);
        }
    }

    /**
     * Check if source type can be converted to target type
     *
     * @param sourceClass Class of source model
     * @param targetClass Class of target model
     * @return true if conversion is possible
     */
    public static boolean isConvertable(Class<?> sourceClass, Class<?> targetClass) {
        return getConverter(sourceClass, targetClass) != null;
    }

    /**
     * Convert <i>source type</i> to <i>target type</i>
     *
     * @param <TSource>   Source type
     * @param <TTarget>   Target type
     * @param source      Source model
     * @param targetClass Class of target model
     * @return Target model
     * @throws IllegalArgumentException No converter found for TSource and TTarget
     */
    public static <TSource, TTarget> TTarget convert(TSource source, Class<TTarget> targetClass) {
        if (source == null || targetClass == null)
            throw new NullPointerException("All arguments cannot be null");
        Class<?> sourceClass = source.getClass();
        IConvertFromTo<TSource, TTarget> converter = getConverter(sourceClass, targetClass);
        if (converter == null)
            throw new IllegalArgumentException("Cannot find converter for type " + sourceClass.getName()
                    + " and " + targetClass.getName() + ". Consider use register() to add a converter first?");
        return converter.convert(source);
    }

    @SuppressWarnings("unchecked")
    private static <TSource, TTarget> IConvertFromTo<TSource, TTarget> getConverter(Class<?> source,
                                                                                    Class<TTarget> targetClass) {
        // Find converter for type TSource to TTarget
        Map<Class<?>, IConvertFromTo<?, ?>> targetMap = REGISTERED_CONVERTERS.get(source);
        if (targetMap == null) {
            return null;
        }
        return (IConvertFromTo<TSource, TTarget>) targetMap.get(targetClass);
    }

    static {
        DefaultAlgorithm.registerDefaultAlgorithm();
    }
}
