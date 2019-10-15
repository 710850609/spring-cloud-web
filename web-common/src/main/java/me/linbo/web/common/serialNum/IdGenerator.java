package me.linbo.web.common.serialNum;

/**
 * @author LinBo
 * @date 2019-10-12 16:52
 */
@FunctionalInterface
public interface IdGenerator<T> {

    T next();
}
