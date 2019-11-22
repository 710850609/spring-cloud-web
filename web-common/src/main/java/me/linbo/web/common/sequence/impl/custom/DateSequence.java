package me.linbo.web.common.sequence.impl.custom;

import me.linbo.web.common.sequence.ISequence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author LinBo
 * @date 2019-11-22 11:27
 */
public class DateSequence implements ISequence<String> {

    private String namespace;

    private DateTimeFormatter dateTimeFormatter;

    public DateSequence(String namespace, String pattern) {
        Objects.requireNonNull(pattern, "时间表达式为空");
        this.namespace = namespace;
        dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public String next() {
        return LocalDateTime.now().format(dateTimeFormatter);
    }
}
