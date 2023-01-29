package fit.ini;

import java.util.*;

/**
 * 表示 INI 对象内部的一个区块
 */
public class Section implements Iterable<Map.Entry<String, String>> {

    protected Map<String, String> entries = new HashMap<>();
    protected List<Record> itemList = new ArrayList<>();

    /**
     * 获取此区块中键值对的值
     *
     * @param key 键名
     * @return key 对应的值
     */
    public String getItem(String key) {
        return entries.get(key == null ? "" : key);
    }

    /**
     * 设置此区块中键值对的值
     *
     * @param key   键名
     * @param value 值(不能为空)
     */
    public void setItem(String key, String value) {
        Objects.requireNonNull(value);
        if (key == null) {
            key = "";
        }
        if (!entries.containsKey(key)) {
            itemList.add(new Record(key, RecordType.KeyValue));
        }
        entries.put(key, value);
    }

    /**
     * 设置此区块中键值对的值
     *
     * @param key   键名
     * @param value 值(不能为空)
     */
    public void setItem(String key, Object value) {
        setItem(key, value.toString());
    }

    /**
     * 获取此区块内的条目(键值对)总数
     *
     * @return 条目数
     */
    public int count() {
        return entries.size();
    }

    /**
     * 构造一个 Section 对象
     */
    public Section() {
    }

    /**
     * 获取此区块中一个条目的值，当键不存在时返回默认值
     *
     * @param key 键名
     * @param def 当键不存在时的默认返回值
     * @return 找到的值或默认值
     */
    public String getOrDefault(String key, String def) {
        return Optional.ofNullable(getItem(key)).orElse(def);
    }

    /**
     * 检测此区块中是否包含某条目(键值对)
     *
     * @param key 键名
     * @return 包含时返回 True, 不包含返回 False
     */
    public boolean contains(String key) {
        return entries.containsKey(key == null ? "" : key);
    }

    /**
     * 移除某一个条目(键值对)
     *
     * @param key 键名
     * @return 成功移除返回 True, 否则返回 False
     */
    public boolean remove(String key) {
        return entries.remove(key == null ? "" : key) != null;
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
        if (!oldName.equals(newName) && entries.containsKey(oldName)) {
            String value = entries.remove(oldName);
            entries.put(newName, value);
            return true;
        }
        return false;
    }

    /**
     * 创建一个当前 Section 对象的副本(深拷贝)
     *
     * @return 当前对象的副本
     */
    public Section clone() {
        Section section = new Section();
        section.entries.putAll(this.entries);
        for (Record r : this.itemList) {
            section.itemList.add(new Record(r.content, r.type));
        }
        return section;
    }

    /**
     * 清空当前 Section 对象的所有内容
     */
    public void clear() {
        entries.clear();
        itemList.clear();
    }

    /**
     * 将文本内容追加到最后一条内容上（键值对的值/注释/其他文本）
     * 此方法主要用于构造INI时处理多行内容
     * 若尝试合并失败会自动跳过
     *
     * @param content 要追加的内容(不能为空)
     */
    void appendToLast(String content) {
        Objects.requireNonNull(content);
        if (itemList.isEmpty()) {
            // 如果当前区块为空，添加为非注释项
            addComment(content, false);
            return;
        }
        Record record = itemList.get(itemList.size() - 1);
        if (record.isKeyValue()) {
            String value = entries.get(record.content);
            entries.put(record.content, value + content);
        } else {
            record.content += content;
        }
    }

    /**
     * 添加一条注释
     *
     * @param content 注释的内容(不能为空)
     */
    public void addComment(String content) {
        addComment(content, true);
    }

    /**
     * 添加一条注释或纯文本
     *
     * @param content   注释的内容
     * @param isComment 是普通的注释(True)还是非注释的纯文本(False)
     */
    public void addComment(String content, boolean isComment) {
        Objects.requireNonNull(content);
        itemList.add(new Record(content, isComment ? RecordType.Comment : RecordType.Other));
    }

    /**
     * 获取所有的注释和文本 (除键值对数据以外的内容)
     *
     * @return 字符串数组
     */
    public String[] getComments() {
        ArrayList<String> list = new ArrayList<>();
        for (Record record : itemList) {
            if (!record.isKeyValue()) {
                list.add(record.content);
            }
        }
        return list.toArray(new String[0]);
    }

    /**
     * 移除所有的注释和文本 (除键值对数据以外的内容)
     */
    public void removeAllComments() {
        itemList.removeIf(record -> !record.isKeyValue());
    }

    /**
     * 获取所有的键
     *
     * @return 所有的键
     */
    public String[] getKeys() {
        return entries.keySet().toArray(new String[0]);
    }

    /**
     * 按行生成文本的列表
     *
     * @param commentPrefix 表示行注释的前缀字符
     * @return 返回包含每行内容的列表
     */
    public List<String> toList(String commentPrefix) {
        if (commentPrefix == null) {
            commentPrefix = "";
        }
        ArrayList<String> list = new ArrayList<>();
        for (Record record : itemList) {
            if (record.isKeyValue()) {
                list.add(record.content + "=" + entries.get(record.content));
            } else if (record.type == RecordType.Comment) {
                list.add(commentPrefix + record.content);
            } else {
                list.add(record.content);
            }
        }
        return list;
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return new Ini.Itr<>(entries.entrySet().iterator());
    }

    /**
     * 数据的种类
     */
    enum RecordType {
        KeyValue,
        Comment,
        Other
    }

    /**
     * 表示区块中的某条具体数据
     */
    static class Record {
        RecordType type;
        String content;

        public Record(String content, RecordType type) {
            this.type = type;
            this.content = content;
        }

        public boolean isKeyValue() {
            return this.type == RecordType.KeyValue;
        }

        @Override
        public String toString() {
            return String.format("{Type = %s, Content = \"%s\"}", type, content);
        }
    }

}
