package me.linbo.web.common.id;

/**
 * 分布式id异常
 * @author LinBo
 * @date 2019-11-18 15:38
 */
public class DistributedIdException extends RuntimeException {

    public DistributedIdException() {
    }

    public DistributedIdException(Throwable cause) {
        super(cause);
    }
}
