package me.linbo.web.common.sequence.impl.custom;

import me.linbo.web.common.sequence.ISequence;

import java.util.Objects;

/**
 * @author LinBo
 * @date 2019-11-22 11:25
 */
public class StringSequence implements ISequence<String> {

    private String namespace;

    private String source;

    public StringSequence(String namespace, String source) {
        Objects.requireNonNull(source, "字符串不能为空");
        this.namespace = namespace;
        this.source = source;
    }

    @Override
    public String next() {
        return source;
    }

}
