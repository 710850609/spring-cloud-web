package me.linbo.web.common.sequence;

import me.linbo.web.common.sequence.impl.DateSequence;
import me.linbo.web.common.sequence.impl.NumberSequence;
import me.linbo.web.common.sequence.impl.StringSequence;

import java.util.ArrayList;
import java.util.List;

/**
 * 混合类型序列
 * @author LinBo
 * @date 2019-11-22 11:03
 */
public class MixedSequence implements ISequence<String> {

    private String namespace;

    private List<ISequence> chains;

    @Override
    public String next() {
        StringBuilder buf = new StringBuilder();
        chains.forEach(c -> buf.append(c.next()));
        return buf.toString();
    }

    private MixedSequence nextSequence(ISequence chain) {
        chains.add(chain);
        return this;
    }

    public static MixedSequence build(String namespace) {
        MixedSequence mixedSequence = new MixedSequence();
        mixedSequence.chains = new ArrayList<>();
        mixedSequence.namespace = namespace;
        return mixedSequence;
    }

    public MixedSequence str(String source) {
        return nextSequence(new StringSequence(source));
    }


    public MixedSequence num(long maxLength) {
        return nextSequence(new NumberSequence(this.namespace, maxLength));
    }

    public MixedSequence date(String pattern) {
        return nextSequence(new DateSequence(this.namespace, pattern));
    }
}
