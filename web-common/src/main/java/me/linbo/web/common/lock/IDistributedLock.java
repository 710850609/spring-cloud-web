package me.linbo.web.common.lock;


/**
 * 分布式锁接口
 * @author LinBo
 * @date 2019-11-12 9:59
 */
public interface IDistributedLock {

    /** 默认超时时间，如果超过超时时间，没有主动释放锁，该锁会自动释放 */
    long DEFAULT_TIMEOUT = 3000L;

    /**
     * 上锁
     * @return
     * @throws DistributedLockException
     **/
    void lock() throws DistributedLockException;

    /**
     * 尝试上锁
     * @return {@link boolean}
     **/
    boolean tryLock();

    /**
     * 尝试上锁，并锁住指定时间长度
     * @param timeout
     * @return {@link boolean}
     **/
    boolean tryLock(long timeout);

    /**
     * 解锁
     * @return
     * @throws DistributedLockException
     **/
    void unlock() throws DistributedLockException;
}
