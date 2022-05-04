package colorpad.core;

import colorpad.core.algorithm.grayscale.GrayComponentAlgorithm;
import colorpad.core.algorithm.grayscale.IGrayscaleAlgorithm;
import colorpad.core.converter.DefaultModelConverters;
import colorpad.core.converter.IConvertFromTo;

import java.util.HashMap;
import java.util.Map;

/**
 * Entry of process color models conversion and calculation
 *
 * @author Snow
 */
public final class ModelsManager {

    private static final Map<Class<?>, Map<Class<?>, IConvertFromTo<?, ?>>> REGISTERED_CONVERTERS = new HashMap<>();

    private static IGrayscaleAlgorithm grayscaleAlgorithm = new GrayComponentAlgorithm();

    /**
     * Get current grayscale algorithm
     *
     * @return Grayscale algorithm
     */
    public static IGrayscaleAlgorithm getGrayscaleAlgorithm() {
        return grayscaleAlgorithm;
    }

    /**
     * Replace the default algorithm of grayscale
     *
     * @param algorithm Grayscale algorithm
     */
    public static void registerGrayscaleAlgorithm(IGrayscaleAlgorithm algorithm) {
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
     * @param source      Source model (including subclass of source type)
     * @param targetClass Class of target model
     * @return Target model
     * @throws IllegalArgumentException No converter found for TSource and TTarget
     */
    public static <TSource, TTarget> TTarget convert(TSource source, Class<TTarget> targetClass) {
        if (source == null || targetClass == null)
            throw new NullPointerException("All arguments cannot be null");
        Class<?> sourceClass = source.getClass();
        IConvertFromTo<TSource, TTarget> converter = getConverter(sourceClass, targetClass);
        while (converter == null && !Object.class.equals(sourceClass)) {
            sourceClass = sourceClass.getSuperclass();
            converter = getConverter(sourceClass, targetClass);
        }
        if (converter == null)
            throw new IllegalArgumentException("Cannot find converter for type " + source.getClass().getName()
                    + "(or superclass) and " + targetClass.getName() + ". Consider use register() to add a converter first?");
        return converter.convert(source);
    }

    /**
     * Using specified converter for <i>source type</i> and <i>target type</i> to convert model.
     * This method cannot find converter if <i>source</i> is subclass of <i>source type</i>,
     * which is different to {@link #convert(Object, Class)}.
     *
     * @param <TSource>      Source type
     * @param <TTarget>      Target type
     * @param <TSourceModel> Source type of actual model (might be subclass of TSource)
     * @param source         Source model
     * @param sourceClass    Class of source model (to find converter)
     * @param targetClass    Class of target model
     * @return Target model
     * @throws IllegalArgumentException No converter found for TSource and TTarget
     */
    public static <TSource, TTarget, TSourceModel extends TSource> TTarget convert(
            TSourceModel source, Class<TSource> sourceClass, Class<TTarget> targetClass) {
        if (source == null || sourceClass == null || targetClass == null)
            throw new NullPointerException("All arguments cannot be null");
        IConvertFromTo<TSource, TTarget> converter = getConverter(sourceClass, targetClass);
        if (converter == null)
            throw new IllegalArgumentException("Cannot find converter for type " + sourceClass.getName()
                    + " and " + targetClass.getName() + ". Consider use register() to add a converter first?");
        return converter.convert(source);
    }

    @SuppressWarnings("unchecked")
    private static <TSource, TTarget> IConvertFromTo<TSource, TTarget> getConverter(
            Class<?> source, Class<TTarget> targetClass) {
        // Find converter for type TSource to TTarget
        Map<Class<?>, IConvertFromTo<?, ?>> targetMap = REGISTERED_CONVERTERS.get(source);
        if (targetMap == null) {
            return null;
        }
        return (IConvertFromTo<TSource, TTarget>) targetMap.get(targetClass);
    }

    static {
        DefaultModelConverters.registerToManager();
    }
}
