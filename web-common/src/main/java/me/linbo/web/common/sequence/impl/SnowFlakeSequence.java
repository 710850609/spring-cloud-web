package me.linbo.web.common.sequence.impl;

import me.linbo.web.common.sequence.ISequence;

/**
 * Twitter的雪花算法实现
 * 产生64位数字:
 * 0 - 41位时间戳 - 5位数据中心标识 - 5位机器标识 - 12位序列号
 *
 * @author LinBo
 * @date 2019-10-12 16:53
 */
public class SnowFlakeSequence implements ISequence<Long> {

    /**
     * 产生下一个ID
     **/
    @Override
    public synchronized Long next() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStmp = currStmp;

        return (currStmp - START_STMP) << TIMESTMP_LEFT //时间戳部分
                | datacenterId << DATACENTER_LEFT      //数据中心部分
                | machineId << MACHINE_LEFT            //机器标识部分
                | sequence;                            //序列号部分
    }

    /**
     * 起始的时间戳，可以修改为服务第一次启动的时间
     * 一旦服务已经开始使用，起始时间戳就不应该改变
     */
    private final static long START_STMP = 1571103458728L;

    /**
     * 每一部分占用的位数
     */
    private final static long DATACENTER_BIT = 5;//数据中心占用的位数
    private final static long MACHINE_BIT = 5;   //机器标识占用的位数
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    /** 数据中心 */
    private long datacenterId;
    /** 机器标识 */
    private long machineId;
    /** 序列号 */
    private long sequence = 0L;
    /** 上一次时间戳 */
    private long lastStmp = -1L;

    /**
     * 通过单例模式来获取实例
     * 分布式部署服务时，数据节点标识和机器标识作为联合键必须唯一
     * @param datacenterId 数据节点标识ID
     * @param machineId 机器标识ID
     */
    public SnowFlakeSequence(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("dataCenterId can't be greater than " + MAX_DATACENTER_NUM + " or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than " + MAX_MACHINE_NUM + " or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

}
