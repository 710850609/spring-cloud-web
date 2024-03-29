package me.linbo.web.common.sequence.impl;

import lombok.extern.slf4j.Slf4j;
import me.linbo.web.common.sequence.DistributedSequenceException;
import me.linbo.web.common.sequence.ISequence;
import me.linbo.web.common.spring.SpringContextHolder;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 雪花算法，自动配置生成序列
 * @author LinBo
 * @date 2019-11-22 16:47
 */
@Slf4j
public class SnowFlakeAutoSequence implements ISequence<Long> {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private String redisKey;
    /** 注册有效时间1分钟 */
    private static final long VALID_SECONDS = 60L;

    private String namespace;

    public SnowFlakeAutoSequence(String namespace) {
        this.namespace = "concurrent:snow-flake:" + namespace;
        register();
        scheduler.scheduleAtFixedRate(() -> renewalMachine(), 1, VALID_SECONDS - 1, TimeUnit.SECONDS);
    }

    /**
     * 注册入口
     */
    private void register() {
        boolean isRegister = false;
        int dataCenterId = 1;
        do {
            if (++datacenterId > (1 << DATACENTER_LEFT)) {
                break;
            }
            isRegister = registerMachine(dataCenterId);
        } while (!isRegister);
        if (isRegister) {
            this.datacenterId = dataCenterId;
        } else {
            throw new DistributedSequenceException("注册雪花算法失败");
        }
        log.debug("雪花算法注册成功，数据中心: {}, 机器: {}, redisKey: {}", this.datacenterId, this.machineId, this.redisKey);
    }

    /**
     * 注册机器
     * @param dataCenterKey
     * @return
     */
    private boolean registerMachine(int dataCenterKey) {
        StringRedisTemplate redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
        int machineId = 0;
        Boolean isRegister = false;
        do {
            if (machineId++ > 1 << MACHINE_LEFT) {
                break;
            }
            String machineKey = this.namespace + ":" + dataCenterKey + ":" + machineId;
            isRegister = redisTemplate.opsForValue().setIfAbsent(machineKey, "", VALID_SECONDS, TimeUnit.SECONDS);
            this.redisKey = machineKey;
            this.machineId = machineId;
        } while (isRegister == null || !isRegister);
        return isRegister;
    }

    /**
     * 机器续期
     */
    private void renewalMachine() {
        try {
            log.debug("雪花算法续期, redisKey= {}", this.redisKey);
            StringRedisTemplate redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
            redisTemplate.opsForValue().set(this.redisKey, "", VALID_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("雪花算法续期失败", e);
        }
    }

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
