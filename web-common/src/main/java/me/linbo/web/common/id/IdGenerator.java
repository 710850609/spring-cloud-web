package me.linbo.web.common.id;

/**
 * @author LinBo
 * @date 2019-10-12 16:52
 */
@FunctionalInterface
public interface IdGenerator<T> {

    /**
     * 产生下一个id
     * @Author LinBo
     * @Date 2019-11-11 16:48
     * @return {@link T}
     **/
    T next();
}
