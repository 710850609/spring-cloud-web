package me.linbo.web.common.id;

/**
 * 分布式id异常
 * @author LinBo
 * @date 2019-11-18 15:38
 */
public class DistributedSequenceException extends RuntimeException {

    public DistributedSequenceException() {
    }

    public DistributedSequenceException(Throwable cause) {
        super(cause);
    }
}
