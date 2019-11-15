package me.linbo.web.common;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebCommonApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WebCommonApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        new Thread(() -> {
//            long time = System.currentTimeMillis();
//            System.out.println(Thread.currentThread().getName() + "上锁");
//            lock.lock();
//            System.out.println(Thread.currentThread().getName() + "释放锁, " + (System.currentTimeMillis() - time));
//        }).start();
//        new Thread(() -> {
//            long time = System.currentTimeMillis();
//            System.out.println(Thread.currentThread().getName() + "上锁");
//            lock.lock();
//            System.out.println(Thread.currentThread().getName() + "释放锁, " + (System.currentTimeMillis() - time));
//        }).start();
//
//        boolean isLock = lock.tryLock(500L);
//        System.out.println(isLock);

    }
}
