package me.linbo.web.common.sequence;

import me.linbo.web.common.sequence.impl.NumberCacheSequence;
import org.junit.Test;

public class MixedSequenceTest {

    @Test
    public void test() {
        MixedSequence sequence = MixedSequence.build("test").date("HH:ss:SSS-").num(1000).str("-no");
        int count = 10000;
        do {
            String next = sequence.next();
            System.out.println(next);
        } while (count-- > 0);
    }

    @Test
    public void test1() {
        NumberCacheSequence sequence = new NumberCacheSequence("test-seq");

    }

}