package fit.simplification;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.IntFunction;

/**
 * 整合了一些用于判断和检测方法
 * <pre>
 * 此类中的方法主要是为了简化例如 if 等条件判断语句，支持类型自适应
 *
 * 没有特殊注明外，此类中的所有方法都支持空值检测（null安全）
 * P.S. 这个类中提供了一些类似其他语言的写法或逻辑
 * </pre>
 *
 * @author Snow
 * @version 1.4
 */
public class Check {
    /**
     * 判断字符序列是否不为空
     * 类似于 Python、JavaScript 的 if 语法
     *
     * @param s 字符串
     * @return 判断结果
     */
    public static boolean notEmpty(CharSequence s) {
        return s != null && s.length() > 0;
    }

    /**
     * 判断集合是否不为空
     * 类似于 Python、JavaScript 的 if 语法
     *
     * @param c 集合
     * @return 判断结果
     */
    public static boolean notEmpty(Collection<?> c) {
        return c != null && !c.isEmpty();
    }

    /**
     * 判断 Map 是否不为空
     * 类似于 Python、JavaScript 的 if 语法
     *
     * @param map 集合
     * @return 判断结果
     */
    public static boolean notEmpty(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    /**
     * 判断数组长度是否不为 0
     * 类似于 Python 的 if 语法
     *
     * @param array 数组
     * @return 判断结果
     */
    public static <T> boolean notEmpty(T[] array) {
        return array != null && array.length > 0;
    }

    /**
     * 判断可迭代对象是否还没到末尾
     *
     * @param iterable 可迭代对象
     * @return 判断结果
     */
    public static boolean notEmpty(Iterable<?> iterable) {
        return iterable != null && iterable.iterator().hasNext();
    }

    /**
     * 判断迭代器是否还没到末尾
     *
     * @param iterator 迭代器
     * @return 判断结果
     */
    public static boolean notEmpty(Iterator<?> iterator) {
        return iterator != null && iterator.hasNext();
    }

    /**
     * 判断字符序列是否为空
     * 类似于 Python、JavaScript 的 if not exp / if (!exp)语法
     *
     * @param s 字符串
     * @return 判断结果
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断集合是否为空
     * 类似于 Python、JavaScript 的 if not exp / if (!exp) 语法
     *
     * @param c 集合
     * @return 判断结果
     */
    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    /**
     * 判断 Map 是否为空
     * 类似于 Python、JavaScript 的 if not exp / if (!exp) 语法
     *
     * @param map 集合
     * @return 判断结果
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断数组长度是否为 0
     * 类似于 Python 的 if not 语法
     *
     * @param array 数组
     * @return 判断结果
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length <= 0;
    }

    /**
     * 判断可迭代对象是否达到了末尾
     *
     * @param iterable 可迭代对象
     * @return 判断结果
     */
    public static boolean isEmpty(Iterable<?> iterable) {
        return iterable == null || !iterable.iterator().hasNext();
    }

    /**
     * 判断迭代器是否达到了末尾
     *
     * @param iterator 迭代器
     * @return 判断结果
     */
    public static boolean isEmpty(Iterator<?> iterator) {
        return iterator == null || !iterator.hasNext();
    }

    /**
     * 判断布尔值是否等于 true
     * 类似于 Python、.Net 的 if 语法
     *
     * @param b 布尔值
     * @return 判断结果
     */
    public static boolean isTrue(Boolean b) {
        return b != null && b;
    }

    /**
     * 判断字符不为结束符（ASCII 码 0 值）
     * 参考 C 语言的字符数组结尾
     *
     * @param c 字符
     * @return 不为 0 时返回 true
     */
    public static boolean isTrue(Character c) {
        return c != null && c != 0;
    }

    /**
     * 判断数字是否不等于 0，可避免转换精度问题（使用 BigDecimal 封装）
     * 类似于 Python、.Net 的 if 语法
     *
     * @param n 数字
     * @return 判断结果
     */
    public static boolean isTrue(Number n) {
        if (n == null) {
            return false;
        }
        if (n.longValue() != 0) {
            return true;
        }
        // 如果整数部分为0，再单独判断小数部分
        if (!isIntegerNumber(n)) {
            return new BigDecimal(n.toString()).compareTo(BigDecimal.ZERO) != 0;
        }
        return false;
    }

    /**
     * 判断条件是否成立，会自动针对不同的类型进行处理（参考其他重载）
     * 类似于 Python、.Net 的 if 语法
     *
     * @param o 被检测对象
     * @return 满足条件返回 true
     */
    public static boolean isTrue(Object o) {
        if (o == null) {
            return false;
        }
        Class<?> cls = o.getClass();
        if (cls.isArray()) {
            return Array.getLength(o) > 0;
        } else if (o instanceof CharSequence) {
            return notEmpty((CharSequence) o);
        } else if (o instanceof Collection) {
            return notEmpty((Collection<?>) o);
        } else if (o instanceof Map) {
            return notEmpty((Map<?, ?>) o);
        } else if (o instanceof Number) {
            return isTrue((Number) o);
        } else if (o instanceof Boolean) {
            return (boolean) o;
        } else if (o instanceof Iterable) {
            return notEmpty((Iterable<?>) o);
        } else if (o instanceof Iterator) {
            return notEmpty((Iterator<?>) o);
        } else if (o instanceof Character) {
            return isTrue((Character) o);
        }
        return true;
    }

    /**
     * 对左右两端的值进行异或运算
     * 参考 .Net 的 Xor
     *
     * @param left  左值
     * @param right 右值
     * @return 异或运算结果
     */
    public static boolean xor(Object left, Object right) {
        return isTrue(left) ^ isTrue(right);
    }

    /**
     * 判断是否所有条件都成立
     * <pre>
     *     and(条件1, 条件2, ...)
     *     等同于： iff(条件1) && iff(条件2) && iff(...)
     * </pre>
     *
     * @param condition 判断条件
     * @param others    其他的判断条件
     * @return 所有条件都成立时返回 true
     */
    public static boolean and(Object condition, Object... others) {
        if (isTrue(condition)) {
            return Arrays.stream(others).allMatch(Check::isTrue);
        }
        return false;
    }

    /**
     * 判断是否有任意一个条件成立
     * <pre>
     *     or(条件1, 条件2, ...)
     *     等同于： iff(条件1) || iff(条件2) || iff(...)
     * </pre>
     *
     * @param condition 判断条件
     * @param others    其他的判断条件
     * @return 有任意一个条件成立时返回 true
     */
    public static boolean or(Object condition, Object... others) {
        if (isTrue(condition)) {
            return true;
        } else {
            return Arrays.stream(others).anyMatch(Check::isTrue);
        }
    }

    /**
     * 判断左右两个字符序列的文本内容是否相同（区分大小写）
     * <pre>
     * e.g.
     *     equals("1", "1") -> true
     *     equals(new StringBuilder("AAA"), "AAA") -> true
     *     equals("1.0", "1") -> false
     *     equals("Abc", "abc") -> false
     * </pre>
     *
     * @param left  左
     * @param right 右
     * @return 两者内容相同时返回 true
     */
    public static boolean equals(CharSequence left, CharSequence right) {
        // 自身检测和空值检测
        if (left == right) {
            return true;
        } else if (left == null || right == null) {
            return false;
        }
        // 如果存在 String，使用 String 的 contentEquals 方法
        if (left instanceof String) {
            return ((String) left).contentEquals(right);
        } else if (right instanceof String) {
            return ((String) right).contentEquals(left);
        }
        // 生成 String 后再判断
        return left.toString().contentEquals(right);
    }

    /**
     * 判断左侧字符序列与右侧字符数组的文本内容是否相同（重载方法，区分大小写）
     * <pre>
     * e.g.
     *     equals(new char[] {'1', 'A'}, "1A") -> true
     *     equals(null, null) -> true
     *     equals(null, "sth") -> false
     * </pre>
     *
     * @param left  左（字符数组）
     * @param right 右（字符序列）
     * @return 两者内容相同时返回 true
     * @see #equals(CharSequence, char[])
     */
    public static boolean equals(char[] left, CharSequence right) {
        return equals(right, left);
    }

    /**
     * 判断左侧字符序列与右侧字符数组的文本内容是否相同（区分大小写）
     * <pre>
     * e.g.
     *     equals("1A", new char[] {'1', 'A'}) -> true
     *     equals(null, null) -> true
     *     equals("sth", null) -> false
     * </pre>
     *
     * @param left  左（字符序列）
     * @param right 右（字符数组）
     * @return 两者内容相同时返回 true
     */
    public static boolean equals(CharSequence left, char[] right) {
        // 空值检测
        if (left == null && right == null) {
            return true;
        } else if (left == null || right == null) {
            return false;
        }
        if (left.length() != right.length) {
            return false;
        }
        return String.valueOf(right).contentEquals(left);
    }

    /**
     * 判断左右两个浮点数的数值是否相同，可避免类型转换时的精度问题（使用 BigDecimal 封装）
     * <pre>
     * e.g.
     *     equals(233, 233) -> true
     *     equals(233, 233.0D) -> true
     *     equals(66.66D, 66.66F) -> true
     *     equals(2L, new BigDecimal("2.0")) -> true
     *     equals(new BigDecimal("1.00"), new BigDecimal("1")) -> true
     *     equals(66.0, (Number) null) -> false
     *     66.66D == 66.66F -> false（Java 原生写法，非此方法）
     * </pre>
     *
     * @param left  左
     * @param right 右
     * @return 两者内容相同时返回 true
     */
    public static boolean equals(Number left, Number right) {
        // 空值检测
        if (left == right) {
            return true;
        } else if (left == null || right == null) {
            return false;
        }
        // 整形直接比较
        if (isIntegerNumber(left) && isIntegerNumber(right)) {
            return left.longValue() == right.longValue();
        }
        // 浮点数使用 BigDecimal 转换后再比较，防止精度损失
        BigDecimal d1 = left instanceof BigDecimal ?
                ((BigDecimal) left) : new BigDecimal(left.toString());
        BigDecimal d2 = right instanceof BigDecimal ?
                ((BigDecimal) right) : new BigDecimal(right.toString());
        return d1.compareTo(d2) == 0;
    }

    /**
     * 判断左侧的字符和右侧对象的内容是否相同
     * 支持和以下类型进行比较：
     * 字符、字符序列（CharSequence）、数字（Number）
     * <pre>
     * 右侧对象的类型有如下情况：
     * 与 Character/char 比较为常规的判断
     *      contentEquals('A', 'A') -> true
     *      contentEquals('A', 'B') -> false
     * 与 CharSequence 比较，会在右侧长度为1时提取首个字符进行比较
     *      contentEquals('A', "A") -> true
     *      contentEquals('A', "B") -> false
     *      contentEquals('A', "ABC") -> false
     * 与 Number 比较，会使用 left 的 ASCII 码与右侧比较
     *      contentEquals('A', 65) -> true
     *      contentEquals('A', 55) -> false
     * </pre>
     *
     * @param left  左（字符）
     * @param right 右（任意对象）
     * @return 两者内容相同时返回 true
     */
    public static boolean contentEquals(Character left, Object right) {
        // 空值检测
        if (left == right) {
            return true;
        } else if (left == null || right == null) {
            return false;
        }
        char ch = left;
        // 字符
        if (right instanceof Character) {
            return ch == (Character) right;
        }
        // 检测另一个对象的类型，并分类处理，字符序列
        if (right instanceof CharSequence) {
            CharSequence cs = (CharSequence) right;
            return cs.length() == 1 && ch == cs.charAt(0);
        }
        // 数字
        if (right instanceof Number) {
            return isIntegerNumber(right) ? ch == ((Number) right).longValue() :
                    equals((int) ch, (Number) right);
        }
        return false;
    }

    /**
     * 判断左侧的数字和右侧字符串的内容是否相同
     * 会尝试将字符串转为数字进行比较，如果失败则将数字转为字符串对比文本
     * <pre>
     * 例如有如下情况：
     *      contentEquals(233, "233") -> true
     *      contentEquals(233.0, "233") -> true
     *      contentEquals(1.0, "1.00") -> true
     *      contentEquals(2, "AAA") -> false
     * </pre>
     *
     * @param left  左（数字）
     * @param right 右（字符序列）
     * @return 两者内容相同时返回 true
     */
    public static boolean contentEquals(Number left, CharSequence right) {
        // 空值检测
        if (left == null && right == null) {
            return true;
        } else if (left == null || right == null) {
            return false;
        }
        // 尝试将字符串转为数字
        try {
            BigDecimal decimal = new BigDecimal(right.toString());
            return equals(decimal, left);
        } catch (Exception e) {
            // 如果字符串的内容不是数字，则比较文本内容
            return equals(left.toString(), right);
        }
    }

    /**
     * 判断左侧的字符串和右侧数字的内容是否相同（重载方法）
     * 会尝试将字符串转为数字进行比较，如果失败则将数字转为字符串对比文本
     * <pre>
     * 例如有如下情况：
     *      contentEquals("233", 233) -> true
     *      contentEquals("233", 233.0) -> true
     *      contentEquals("1.00", 1.0) -> true
     *      contentEquals("AAA", 2) -> false
     * </pre>
     *
     * @return 两者内容相同时返回 true
     * @see #contentEquals(Number, CharSequence)
     */
    public static boolean contentEquals(CharSequence left, Number right) {
        return contentEquals(right, left);
    }

    /**
     * 判断左右两个对象的内容是否相同，在遇到不同类型时会尝试自动转换
     * <pre>
     * 根据左右侧参数的以下情况进行处理（由前到后匹配和转换）：
     * (*)存在空值（null）：
     *      两者同为 null 则视为相同，否则不相同
     *
     * (*)存在集合（Collection，可能会引发递归判断）：
     *      (**)如果一个对象是 Set，另一个是不为 Set 的集合：
     *      尝试将另一个对象转化成 Set 后，进行判断
     *      例如（代码是伪代码）：
     *          contentEquals(Set(1, 2), List(1, 1, 2)) -> true
     *          contentEquals(Set(1), List(1, 2)) -> false
     *      但如果转换失败，直接认为两者内容不相同
     *      (**)如果两个对象都是 Set：
     *      调用 {@link Set#equals(Object)} 进行判断（区分对象的类型）
     *      例如（代码是伪代码）：
     *          contentEquals(Set(1, 2, 3), Set(3, 2, 1)) -> true
     *          contentEquals(Set(1), Set(1, 2)) -> false
     *      (**)如果两个对象都是集合：
     *      忽略集合的类型（泛型），按顺序比较其中两个集合元素的内容（不区分元素的类型）
     *      如果两个集合长度、内容、顺序都一样，会视为内容相同
     *      例如（代码是伪代码）：
     *          contentEquals(Queue(1, 2, 3), List(1, 2, 3)) -> true
     *          contentEquals(List(3, 2), List(2, 3)) -> false
     *          contentEquals(List(0), List(0, 1)) -> false
     *      (**)如果另一个对象不是集合，是其他的类型
     *      如果集合的长度不为1，直接视为不相同，否则提取集合的首个元素与另一个对象比较
     *      例如：
     *          contentEquals(1, Arrays.asList("1.0")) -> true
     *          contentEquals(1, Arrays.asList("1", "2")) -> false
     *
     * (*)存在数组：
     *      同集合的处理方式（方法内部会将数组包装为只读的 Collection 实现类）
     *      依次比较其中的元素内容是否相等（不区分数组中元素的类型）
     *      例如：
     *          contentEquals(new int[][]{{1, 2}}, new Long[][]{{1L, 2L}}) -> true
     *          contentEquals(Arrays.asList(1), new String[]{"1.0"}) -> true
     *          contentEquals(new int[]{1, 2}, new int[]{3, 4}) -> false
     *
     * (*)两者都为数字（Number）：
     *      比较数字的数值是否相等，不受对象类型的限制（Integer/Double 等），同 {@link #equals(Number, Number)}
     *
     * (*)两者都为字符串/字符序列（CharSequence） - 同 {@link #equals(CharSequence, CharSequence)}：
     *      比较文本的内容是否相等，不受对象类型的限制
     *      比较的方式是标准的逐字比较，区分大小写且不会忽略空白字符
     *
     * (*)一个数字（Number）和一个字符序列（CharSequence） - 同 {@link #contentEquals(Number, CharSequence)}：
     *      首先尝试将文本（字符序列）转化为数字，然后比较两者的数值是否相等
     *      若失败，则将数字转为字符串后再对比文本
     *
     * (*)一个字符序列（CharSequence）和一个字符数组（char[]） - 同 {@link #equals(CharSequence, char[])}：
     *      将字符数组视为字符序列，比较文本的内容是否相等
     *
     * (*)存在字符（Character） - 同 {@link #contentEquals(Character, Object)}：
     *      尝试将另一个对象转为字符对比，或与数字对比 ASCII 码
     *
     * (*)如果存在字典（Map）：
     *      使用左侧对象的 equals 方法进行判断，参见 {@link Map#equals(Object)}
     *      例如：
     *          contentEquals(new HashMap<>(), new Hashtable<>()) -> true
     *              ==> 等同 new HashMap<>().equals(new Hashtable<>())
     *          contentEquals(new Object(), new HashMap<>()) -> false
     *              ==> 等同 new Object().equals(new HashMap<>())
     *
     * (*)其他情况（左右两者都是其他的对象）：
     *      调用左侧对象自身的 equals 方法进行判断（left.equals(right)）
     * </pre>
     *
     * @param left  左
     * @param right 右
     * @return 两者内容相同时返回 true
     */
    public static boolean contentEquals(Object left, Object right) {
        if (left == right) {
            return true;
        } else if (left == null || right == null) {
            return false;
        }
        // 左右存在一个 Collection（针对 Set 有不同的处理方式，Map 直接跳至 equals 方法）
        if (left instanceof Collection) {
            return collectionRecursiveEquals((Collection<?>) left, right, new HashSet<>());
        } else if (right instanceof Collection) {
            return collectionRecursiveEquals((Collection<?>) right, left, new HashSet<>());
        }
        // 左右存在一个数组
        if (left.getClass().isArray()) {
            return collectionRecursiveEquals(new ArrayCollectionAdaptor(left), right, new HashSet<>());
        } else if (right.getClass().isArray()) {
            return collectionRecursiveEquals(new ArrayCollectionAdaptor(right), left, new HashSet<>());
        }
        return objectEquals(left, right);
    }

    /**
     * 判断两个对象的内容是否相同【非空】
     * Note: 这个方法是 {@link #contentEquals(Object, Object)} 方法中的主逻辑
     *
     * @param left  任意对象
     * @param right 任意对象
     * @return 结果
     */
    protected static boolean objectEquals(Object left, Object right) {
        Class<?> c1 = left.getClass();
        Class<?> c2 = right.getClass();
        // 都是数字（此处已经被自动装箱/拆箱，不用处理基本数据类型）
        if (left instanceof Number && right instanceof Number) {
            return equals((Number) left, (Number) right);
        }
        // 字符串和其他比较
        if (left instanceof CharSequence) {
            if (right instanceof Number) {
                return contentEquals((Number) right, (CharSequence) left);
            } else if (right instanceof char[]) {
                return equals((CharSequence) left, (char[]) right);
            }
        } else if (right instanceof CharSequence) {
            if (left instanceof Number) {
                return contentEquals((Number) left, (CharSequence) right);
            } else if (left instanceof char[]) {
                return equals((CharSequence) right, (char[]) left);
            }
        }
        // 其他类型，如果左右两侧是相同类型，直接用 equals 方法
        if (c1 == c2) {
            return left.equals(right);
        }
        // 都是字符序列
        if (left instanceof CharSequence && right instanceof CharSequence) {
            return equals((CharSequence) left, (CharSequence) right);
        }
        // 左右存在字符类型
        if (c1 == Character.class) {
            return contentEquals((Character) left, right);
        } else if (c2 == Character.class) {
            return contentEquals((Character) right, left);
        }
        // 其他情况，直接使用对象的 equals 方法
        return left.equals(right);
    }

    /**
     * 判断集合（Collection）的内容是否相同【固定类型/非空】
     * Note: 这个方法是用于拆分 {@link #contentEquals(Object, Object)} 方法中代码，内部会进行多层递归
     *
     * @param left      集合
     * @param right     任意对象
     * @param detectSet 检测递归引用的集合（在调用栈开始时传入）
     * @return 结果
     */
    protected static boolean collectionRecursiveEquals(Collection<?> left, Object right, Set<Object> detectSet) {
        // 如果另一个对象是数组，转为 List 后交给后面处理 Collection 的代码处理
        if (right.getClass().isArray()) {
            right = new ArrayCollectionAdaptor(right);
        }
        // 如果另一个对象是 Collection，则分别比较两个数组中的内容是否相同
        if (right instanceof Collection) {
            Collection<?> otherCollection = (Collection<?>) right;
            // 如果左侧是 Set，尝试把右侧对象也转为 Set 处理
            if (left instanceof Set) {
                if (right instanceof Set) {
                    return left.equals(otherCollection);
                } else {
                    try {
                        return left.equals(new HashSet<>(otherCollection));
                    } catch (Exception e) {
                        return false;
                    }
                }
            }
            // 检测长度是否相同
            if (left.size() != otherCollection.size()) {
                return false;
            }
            // 其他可迭代的集合，比较内容
            Iterator<?> iterator1 = left.iterator();
            Iterator<?> iterator2 = otherCollection.iterator();
            while (iterator1.hasNext()) {
                Object raw = iterator1.next();
                Object value2 = iterator2.next();
                // 如果左侧是集合，使用 Set 检测，避免内部递归引用自身时导致无限循环拆解
                if (isContainer(raw) && detectSet.contains(raw)) {
                    return false;
                }
                detectSet.add(raw);
                // 如果左侧取出的值是数组，将数组包装成 Collection
                Object value1 = raw.getClass().isArray() ? new ArrayCollectionAdaptor(raw) : raw;
                boolean equal = value1 instanceof Collection ?
                        collectionRecursiveEquals((Collection<?>) value1, value2, detectSet) :
                        objectEquals(value1, value2);
                if (!equal) {
                    return false;
                }
                // 检测完集合的一个对象，从 Set 中移除
                detectSet.remove(raw);
            }
            return true;
        }
        // 如果右侧不是集合，且左侧的长度为1，直接把内容和另一个对象比较
        if (left.size() == 1) {
            Object nested = unpackNestedItem(left);
            return nested != null && objectEquals(nested, right);
        } else {
            return false;
        }
    }

    /**
     * 判断数字是否是整数的类型（不包括原始类型）
     *
     * @param o 判断的对象
     * @return 结果
     */
    private static boolean isIntegerNumber(Object o) {
        return o instanceof Integer || o instanceof Long ||
                o instanceof Byte || o instanceof Short;
    }

    /**
     * 判断对象是否是可以嵌套装载自身的容器类型
     *
     * @param o 对象
     * @return 如果是容器，返回 true
     */
    private static boolean isContainer(Object o) {
        return o instanceof Collection ||
                (o != null && o.getClass().isArray());
    }

    /**
     * 拆解长度为 1 的嵌套集合/数组中的内容
     * 如果存在循环嵌套，会直接返回 null 从而让调用处直接返回 false，防止递归调用的问题
     *
     * @param container 数组
     * @return 拆解后的内容
     */
    private static Object unpackNestedItem(Object container) {
        Object o = container;
        HashSet<Object> set = new HashSet<>();
        while (o != null) {
            if (o instanceof Collection && ((Collection<?>) o).size() == 1) {
                o = ((Collection<?>) o).iterator().next();
            } else if (o.getClass().isArray() && Array.getLength(o) == 1) {
                o = Array.get(o, 0);
            } else {
                break;
            }
            if (set.contains(o)) {
                return null;
            }
            set.add(o);
        }
        return o;
    }

    /**
     * 将数组包装为 Collection，为减少开销迭代器设计为不可嵌套使用
     */
    static class ArrayCollectionAdaptor extends AbstractCollection<Object> implements Iterator<Object> {
        protected int length;
        protected int index = 0;
        protected IntFunction<Object> getArrayContent;

        public ArrayCollectionAdaptor(Object array) {
            if (array instanceof byte[]) {
                byte[] bytes = (byte[]) array;
                initialize(bytes.length, value -> bytes[value]);
            } else if (array instanceof short[]) {
                short[] shorts = (short[]) array;
                initialize(shorts.length, value -> shorts[value]);
            } else if (array instanceof int[]) {
                int[] integers = (int[]) array;
                initialize(integers.length, value -> integers[value]);
            } else if (array instanceof long[]) {
                long[] longs = (long[]) array;
                initialize(longs.length, value -> longs[value]);
            } else if (array instanceof char[]) {
                char[] chars = (char[]) array;
                initialize(chars.length, value -> chars[value]);
            } else if (array instanceof float[]) {
                float[] floats = (float[]) array;
                initialize(floats.length, value -> floats[value]);
            } else if (array instanceof double[]) {
                double[] doubles = (double[]) array;
                initialize(doubles.length, value -> doubles[value]);
            } else if (array instanceof boolean[]) {
                boolean[] booleans = (boolean[]) array;
                initialize(booleans.length, value -> booleans[value]);
            } else {
                // 其他对象数组、多维数组都可以转为 Object[]
                Object[] objects = (Object[]) array;
                initialize(objects.length, value -> objects[value]);
            }
        }

        protected void initialize(int length, IntFunction<Object> getArrayContent) {
            this.length = length;
            this.getArrayContent = getArrayContent;
        }

        @Override
        public Iterator<Object> iterator() {
            index = 0;
            return this;
        }

        @Override
        public int size() {
            return length;
        }

        @Override
        public boolean hasNext() {
            return index < length;
        }

        @Override
        public Object next() {
            return getArrayContent.apply(index++);
        }
    }
}
