package me.linbo.web.common.lock;


/**
 * @author LinBo
 * @date 2019-11-12 9:59
 */
public interface IDistributedLock {

    void lock() throws DistributedLockException;

    boolean tryLock();

    boolean tryLock(long timeout) throws DistributedLockException ;

    void unlock() throws DistributedLockException;
}
