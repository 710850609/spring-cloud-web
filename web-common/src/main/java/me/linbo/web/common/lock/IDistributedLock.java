package me.linbo.web.common.lock;


/**
 * @author LinBo
 * @date 2019-11-12 9:59
 */
public interface IDistributedLock {

    void lock() ;

    void lockInterruptibly() throws InterruptedException ;

    boolean tryLock();

    boolean tryLock(long timeout) throws Exception ;

    void unlock() ;
}
