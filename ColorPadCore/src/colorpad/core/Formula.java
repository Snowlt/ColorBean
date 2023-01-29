package colorpad.core;


import colorpad.core.model.Hsb;

import java.util.Arrays;

/**
 * Some basic calculation of color formula
 * 配色方案的一些基本计算
 */
public class Formula {
    /**
     * 以同类色计算色相
     *
     * @param hue 色相
     * @return 计算得出的色相
     */
    public static double[] Monochromatic(double hue) {
        return new double[]{hue};
    }

    /**
     * 以互补色计算色相
     *
     * @param hue 色相
     * @return 计算得出的色相
     */
    public static double[] Complementary(double hue) {
        return new double[]{hue, (hue + 180d) % 360d};
    }

    /**
     * 以分裂互补色计算色相(夹角一般取 90 - 180)
     *
     * @param hue   色相
     * @param angle 夹角
     * @return 计算得出的色相
     */
    public static double[] SplitComplementary(double hue, double angle) {
        return new double[]{hue, (hue + angle) % 360d, (hue + 360d - angle) % 360d};
    }

    /**
     * 以分裂互补色计算色相，夹角 150
     *
     * @param hue 色相
     * @return 计算得出的色相
     */
    public static double[] SplitComplementary(double hue) {
        return SplitComplementary(hue, 150);
    }

    /**
     * 以临近色计算色相(夹角一般取 0 - 90)
     *
     * @param hue   色相
     * @param angle 夹角
     * @return 计算得出的色相
     */
    public static double[] Analogous(double hue, double angle) {
        return new double[]{hue, (hue + angle) % 360d, (hue + 360d - angle) % 360d};
    }

    /**
     * 以临近色计算色相，夹角 60
     *
     * @param hue 色相
     * @return 计算得出的色相
     */
    public static double[] Analogous(double hue) {
        return Analogous(hue, 60d);
    }

    /**
     * 以三角色计算色相
     *
     * @param hue 色相
     * @return 计算得出的色相
     */
    public static double[] Triadic(double hue) {
        return new double[]{hue, (hue + 120d) % 360d, (hue + 240d) % 360d};
    }

    /**
     * 以四角色计算色相(夹角一般取 0 - 90)
     *
     * @param hue   色相
     * @param angle 夹角
     * @return 计算得出的色相
     */
    public static double[] Tetradic(double hue, double angle) {
        return new double[]{hue, (hue + angle) % 360d, (hue + 180d) % 360d, (hue + 180d + angle) % 360d};
    }

    /**
     * 以四角色计算色相，夹角 60
     *
     * @param hue 色相
     * @return 计算得出的色相
     */
    public static double[] Tetradic(double hue) {
        return Tetradic(hue, 60d);
    }

    /**
     * 按照指定的方式计算颜色方案
     *
     * @param hue   色相
     * @param type  计算方式
     * @param angle 主色调和第二色调的色相角度差值(仅对部分方案有效)
     * @return 计算得出的色相
     */
    public static double[] GetFormula(double hue, FormulaType type, Double angle) {
        switch (type) {
            case Monochromatic:
                return Monochromatic(hue);
            case Complementary:
                return Complementary(hue);
            case SplitComplementary:
                if (angle != null) {
                    return SplitComplementary(hue, Basic.getFixRange(angle, 90d, 179.99d));
                } else {
                    return SplitComplementary(hue);
                }
            case Analogous:
                if (angle != null) {
                    return Analogous(hue, Basic.getFixRange(angle, 1d, 90d));
                } else {
                    return Analogous(hue);
                }
            case Triadic:
                return Triadic(hue);
            case Tetradic:
                if (angle != null) {
                    return Tetradic(hue, Basic.getFixRange(angle, 1d, 90d));
                } else {
                    return Tetradic(hue);
                }
        }
        return new double[0];
    }

    /**
     * 按照指定的方式计算颜色方案
     *
     * @param hsb   HSB 对象
     * @param type  计算方式
     * @param angle 主色调和第二色调的色相角度差值(仅对部分方案有效)
     * @return 计算得出的 HSB (色相不同其他相同)
     */
    public static Hsb[] GetFormula(Hsb hsb, FormulaType type, Double angle) {
        double[] formulas = Formula.GetFormula(hsb.h(), type, angle);
        return Arrays.stream(formulas).mapToObj(h -> Hsb.from(h, hsb.s(), hsb.b())).toArray(Hsb[]::new);
    }

}
