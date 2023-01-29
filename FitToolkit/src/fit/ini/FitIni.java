package fit.ini;

import java.io.*;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * INI 读写入口类
 */
public class FitIni {

    /*
        数据 / 文件解析
     */

    /**
     * 从文件读取并解析成 INI
     * <p>默认去除键值对的首尾空白，保留非键值对，且忽略 IO 异常</p>
     *
     * @param path 文件路径
     * @return Ini 对象
     */
    public static Ini loadFromFile(String path) {
        LoadOption option = new LoadOption();
        option.setDropComment(false);
        option.setUnknownLineOption(LoadOption.LineOption.Keep);
        option.setIgnoreFileIoError(true);
        return loadFromFile(path, option);
    }

    /**
     * 从文件读取并解析成 INI
     *
     * @param path 文件路径
     * @param arg  解析文件的选项
     * @return Ini 对象
     */
    public static Ini loadFromFile(String path, LoadOption arg) {
        if (arg.getCommentPrefix() == null || arg.getCommentPrefix().isEmpty()) {
            throw new IllegalArgumentException("Argument arg.CommentPrefix cannot be empty.");
        }
        if (arg.getUnknownLineOption() == null) {
            throw new IllegalArgumentException("Argument arg.UnknownLineOption cannot be null.");
        }
        Ini ini;
        try (IterableReader reader = new IterableReader(new FileReader(path))) {
            ini = buildIni(reader, arg);
        } catch (RuntimeException e) {
            if (!arg.isIgnoreFileIoError()) throw e;
            ini = new Ini();
        } catch (IOException e) {
            if (!arg.isIgnoreFileIoError()) throw new RuntimeException(e);
            ini = new Ini();
        }
        ini.setCommentPrefix(arg.getCommentPrefix());
        return ini;
    }

    /**
     * 简化的从文件读取并解析成 INI
     * <p>仅读取键值对，不去除键值对的首尾空白，且忽略 IO 异常</p>
     *
     * @param path 文件路径
     * @return Ini 对象
     */
    public static Ini loadFromFileSimply(String path) {
        LoadOption option = new LoadOption();
        option.setDropComment(true);
        option.setUnknownLineOption(LoadOption.LineOption.Drop);
        option.setIgnoreFileIoError(true);
        option.setTrimKey(true);
        option.setTrimValue(true);
        return loadFromFile(path, option);
    }

    static Ini buildIni(Iterable<String> collection, LoadOption arg) {
        Ini ini = new Ini();
        Section sec = new Section();
        String secName = null;
        String[] commentPrefixes = Stream.of(";", "#", arg.getCommentPrefix()).distinct().toArray(String[]::new);
        // 按行分析文本
        loop:
        for (String line : collection) {
            if (line.startsWith("[") && line.endsWith("]")) {
                // 区块开头
                if (secName == null) {
                    // 如果还没有遇到过区块开头放入默认区块
                    ini.defaultSection = sec;
                } else {
                    // 已经读取过区块就按照区块名存入对象中
                    ini.setSection(secName, sec);
                }
                secName = line.substring(1, line.length() - 1);
                sec = new Section();
                continue;
            }
            for (String prefix : commentPrefixes) {
                if (line.startsWith(prefix)) {
                    // 注释
                    if (!arg.isDropComment()) {
                        sec.addComment(line.substring(prefix.length()));
                    }
                    continue loop;
                }
            }
            if (line.contains("=")) {
                // 键值对
                String[] kv = line.split("=", 2);
                if (arg.isTrimKey()) kv[0] = kv[0].trim();
                if (arg.isTrimValue()) kv[1] = kv[1].trim();
                sec.setItem(kv[0], kv[1]);
                continue;
            }
            // 其他文本
            switch (arg.getUnknownLineOption()) {
                case Drop:
                    continue;
                case Keep:
                    sec.addComment(line, false);
                    break;
                case AsMultiLine:
                    sec.appendToLast("\n" + line);
                    break;
                case AsMultiLineCombined:
                    sec.appendToLast(line);
                    break;
                case ForceToComment:
                    sec.addComment(line, true);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + arg.getUnknownLineOption());
            }
        }
        // 保存最后一个读取的区块
        if (secName == null) {
            // 如果还没有遇到过区块开头放入默认区块
            ini.defaultSection = sec;
        } else {
            // 已经读取过区块就按照区块名存入对象中
            ini.setSection(secName, sec);
        }
        return ini;
    }

    /**
     * Wrap {@link Reader} as an iterable item (Thread-unsafe)
     */
    static class IterableReader implements Iterable<String>, Iterator<String>, Closeable {

        private final BufferedReader reader;
        private String line;

        /**
         * Creating from character streams
         *
         * @param reader the reader
         */
        public IterableReader(Reader reader) {
            this.reader = new BufferedReader(reader);
            nextLine();
        }

        /**
         * Read the current line. Repeatably invokes will get the same result.
         * Use {@link #nextLine()} to read next line manually.
         *
         * @return The current line
         */
        public String getLine() {
            return line;
        }

        /**
         * Read the next line
         */
        public void nextLine() {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean hasNext() {
            return line != null;
        }

        /**
         * Return current line, then read next line.
         * Equals to this:
         * <pre>
         * String line = getLine();
         * nextLine();
         * </pre>
         *
         * @return The current line
         */
        @Override
        public String next() {
            String currentLine = line;
            nextLine();
            return currentLine;
        }

        @Override
        public Iterator<String> iterator() {
            return this;
        }

        @Override
        public void close() throws IOException {
            reader.close();
        }
    }
}
