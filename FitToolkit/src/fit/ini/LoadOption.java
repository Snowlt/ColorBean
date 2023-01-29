package fit.ini;

/**
 * 用于在解析 INI 文件数据时指定相关选项
 */
public class LoadOption {
    /**
     * 当遇到非标准类型的行文本时的选项
     */
    public enum LineOption {
        /**
         * 保留原文
         */
        Keep,
        /**
         * 合并到上一行（有换行符）
         */
        AsMultiLine,
        /**
         * 合并到上一行（无换行符）
         */
        AsMultiLineCombined,
        /**
         * 转换为注释
         */
        ForceToComment,
        /**
         * 丢弃此行
         */
        Drop
    }

    private boolean ignoreFileIoError = false;
    private boolean dropComment = false;
    private String commentPrefix = Ini.DEFAULT_COMMENT_PREFIX;
    private LineOption unknownLineOption;
    private boolean trimKey = true;
    private boolean trimValue = true;

    public boolean isIgnoreFileIoError() {
        return ignoreFileIoError;
    }

    /**
     * 是否忽略文件不存在的和IO异常的情况（是否抛出异常）
     *
     * @param ignoreFileIoError 当IO异常时，False 抛出异常，True 则仍会返回对象
     */
    public LoadOption setIgnoreFileIoError(boolean ignoreFileIoError) {
        this.ignoreFileIoError = ignoreFileIoError;
        return this;
    }

    public boolean isDropComment() {
        return dropComment;
    }

    /**
     * 读取时是否丢弃注释内容
     *
     * @param dropComment 设为 True 时注释会被丢弃，False 时注释会被留下
     */
    public LoadOption setDropComment(boolean dropComment) {
        this.dropComment = dropComment;
        return this;
    }

    public String getCommentPrefix() {
        return commentPrefix;
    }

    /**
     * 要解析的文件中注释的前缀
     *
     * @param commentPrefix 注释前缀
     */
    public LoadOption setCommentPrefix(String commentPrefix) {
        this.commentPrefix = commentPrefix;
        return this;
    }

    public LineOption getUnknownLineOption() {
        return unknownLineOption;
    }

    /**
     * 遇到无法解析的行的操作
     *
     * @param unknownLineOption 选项
     */
    public LoadOption setUnknownLineOption(LineOption unknownLineOption) {
        this.unknownLineOption = unknownLineOption;
        return this;
    }

    public boolean isTrimKey() {
        return trimKey;
    }

    /**
     * 读取区块中键名的时候是否去除首尾空白
     *
     * @param trimKey 设置为 true 去除空白，false 保留空白
     */
    public LoadOption setTrimKey(boolean trimKey) {
        this.trimKey = trimKey;
        return this;
    }

    public boolean isTrimValue() {
        return trimValue;
    }

    /**
     * 读取区块中值的时候是否去除首尾空白
     *
     * @param trimValue 设置为 true 去除空白，false 保留空白
     */
    public LoadOption setTrimValue(boolean trimValue) {
        this.trimValue = trimValue;
        return this;
    }
}
