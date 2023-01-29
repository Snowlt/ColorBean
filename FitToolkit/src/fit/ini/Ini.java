package fit.ini;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 表示一个 INI 对象
 */
public class Ini implements Iterable<Map.Entry<String, Section>> {
    protected Map<String, Section> sections = new LinkedHashMap<>();
    protected Section defaultSection = new Section();
    protected String commentPrefix;

    /**
     * 默认的注释前缀
     */
    public static final String DEFAULT_COMMENT_PREFIX = ";";

    /**
     * 获取此 INI 中的某一个区块
     *
     * @param name 区块的名字
     * @return 若区块不存在时返回 null
     */
    public Section getSection(String name) {
        return sections.get(name == null ? "" : name);
    }

    /**
     * 设置此 INI 中的某一个区块
     *
     * @param name    区块的名字
     * @param section 区块(不能为空)
     */
    public void setSection(String name, Section section) {
        if (name == null) {
            name = "";
        }
        sections.put(name, Objects.requireNonNull(section));
    }

    /**
     * 获取默认区块(没有指定区块名的键值对和数据会被存在这里)
     *
     * @return 默认区块
     */
    public Section getDefaultSection() {
        return defaultSection;
    }

    /**
     * 获取此 INI 的行注释前缀字符
     *
     * @return 注释前缀
     */
    public String getCommentPrefix() {
        return commentPrefix;
    }

    /**
     * 设置此 INI 的行注释前缀字符
     *
     * @param commentPrefix 注释前缀(不能为空)
     */
    public void setCommentPrefix(String commentPrefix) {
        this.commentPrefix = Objects.requireNonNull(commentPrefix);
    }

    /**
     * 获取区块的总数(不含默认区块)
     *
     * @return 区块数量
     */
    public int count() {
        return sections.size();
    }

    /**
     * 构造一个 INI 对象
     */
    public Ini() {
        commentPrefix = DEFAULT_COMMENT_PREFIX;
    }

    /**
     * 获取一个区块，如果指定的区块名不存在，则新建一个同名区块并返回
     *
     * @param name 区块名
     * @return 区块
     */
    public Section getOrAdd(String name) {
        if (name == null) {
            name = "";
        }
        Section section = sections.get(name);
        if (section == null) {
            section = new Section();
            sections.put(name, section);
        }
        return section;
    }

    /**
     * 获取一个区块，如果指定的区块名不存在，则返回给定的区块默认值
     *
     * @param name 区块名
     * @param def  当区块名不存在时的默认返回值
     * @return 找到的区块或默认值
     */
    public Section getOrDefault(String name, Section def) {
        return Optional.ofNullable(getSection(name)).orElse(def);
    }

    /**
     * 访问此 INI 中的条目
     *
     * @param name 区块名字
     * @param key  键名
     * @return 返回值或 null (区块或键不存在时)
     */
    public String getEntry(String name, String key) {
        return getEntry(name, key, null);
    }

    /**
     * 访问此 INI 中的条目
     *
     * @param name 区块名字
     * @param key  键名
     * @param def  不存在区块或键名时的返回值
     * @return 返回值或 def
     */
    public String getEntry(String name, String key, String def) {
        return Optional.ofNullable(getSection(name)).map(sec -> sec.getItem(key)).orElse(def);
    }

    /**
     * 写入此 INI 中的条目(不存在的区块和键值对会被自动添加)
     *
     * @param name  区块名字
     * @param key   键名
     * @param value 值(不能为空)
     * @see Section#setItem(String, String)
     */
    public void setEntry(String name, String key, String value) {
        getOrAdd(name).setItem(key, value);
    }

    /**
     * 检测此 INI 中是否包含某区块
     *
     * @param sectionName 区块的名字
     * @return 包含时返回 True, 不包含返回 False
     */
    public boolean contains(String sectionName) {
        return sections.containsKey(sectionName == null ? "" : sectionName);
    }

    /**
     * 移除某一个区块
     *
     * @param sectionName 区块的名字
     * @return 成功移除返回 True, 否则返回 False
     */
    public boolean remove(String sectionName) {
        return sections.remove(sectionName == null ? "" : sectionName) != null;
    }

    /**
     * 修改键值中对的键名
     *
     * @param oldName 键名
     * @param newName 键的新名字
     * @return 成功修改返回 True, 否则返回 False
     */
    public boolean rename(String oldName, String newName) {
        if (oldName == null) {
            oldName = "";
        }
        if (newName == null) {
            newName = "";
        }
        if (!oldName.equals(newName) && sections.containsKey(oldName)) {
            Section section = sections.remove(oldName);
            sections.put(newName, section);
            return true;
        }
        return false;
    }

    /**
     * 创建一个当前 INI 对象的副本(深拷贝)
     *
     * @return 当前对象的副本
     */
    public Ini clone() {
        Ini ini = new Ini();
        ini.commentPrefix = this.commentPrefix;
        ini.defaultSection = this.defaultSection.clone();
        for (Map.Entry<String, Section> kv : this.sections.entrySet()) {
            ini.sections.put(kv.getKey(), kv.getValue().clone());
        }
        return ini;
    }

    /**
     * 清空当前 Section 对象的所有内容
     */
    public void clear() {
        clear(true);
    }

    /**
     * 清空当前 Section 对象的所有内容
     *
     * @param includingDefault 包含默认区块
     */
    public void clear(boolean includingDefault) {
        if (includingDefault) {
            defaultSection.clear();
        }
        sections.forEach((k, v) -> v.clear());
        sections.clear();
    }

    /**
     * 移除所有的注释和文本 (除键值对数据以外的内容)
     */
    public void removeAllComments() {
        defaultSection.removeAllComments();
        sections.forEach((k, v) -> v.removeAllComments());
    }

    public String[] getSectionNames() {
        return sections.keySet().toArray(new String[0]);
    }

    /**
     * 按行生成文本的列表
     *
     * @return 返回包含每行内容的列表
     */
    public List<String> toList() {
        List<String> list = defaultSection.toList(commentPrefix);
        for (Map.Entry<String, Section> kv : sections.entrySet()) {
            list.add("[" + kv.getKey() + "]");
            list.addAll(kv.getValue().toList(commentPrefix));
        }
        return list;
    }

    public void saveToFile(String path) {
        try (BufferedWriter file = new BufferedWriter(new FileWriter(path))) {
            List<String> list = toList();
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                file.write(iterator.next());
                if (iterator.hasNext()) {
                    file.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Iterator<Map.Entry<String, Section>> iterator() {
        return new Itr<>(sections.entrySet().iterator());
    }

    static class Itr<T> implements Iterator<T> {
        private final Iterator<T> iterator;

        Itr(Iterator<T> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            return iterator.next();
        }
    }
}
