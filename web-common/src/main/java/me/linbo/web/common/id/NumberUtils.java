package me.linbo.web.common.id;

import me.linbo.web.common.id.impl.SnowFlakeGenerator;

/**
 * @author LinBo
 * @date 2019-10-15 11:22
 */
public class NumberUtils {

    private static SnowFlakeGenerator snowFlakeGenerator = new SnowFlakeGenerator(1, 1);

    public static Long nextId() {
        return snowFlakeGenerator.next();
    }
}
