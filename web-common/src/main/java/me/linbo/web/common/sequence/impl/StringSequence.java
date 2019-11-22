package me.linbo.web.common.sequence.impl;

import me.linbo.web.common.sequence.ISequence;

import java.util.Objects;

/**
 * 常量字符串生成
 * @author LinBo
 * @date 2019-11-22 11:25
 */
public class StringSequence implements ISequence<String> {

    private String source;

    public StringSequence(String source) {
        Objects.requireNonNull(source, "字符串不能为空");
        this.source = source;
    }

    @Override
    public String next() {
        return source;
    }

}
