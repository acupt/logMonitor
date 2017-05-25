package com.acupt.monitor;

import com.acupt.processor.LogProcessor;
import com.acupt.processor.NotifyLogProcessor;
import com.acupt.service.RedisService;
import com.acupt.util.BeanFactory;
import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by liujie on 2017/5/15.
 */
public class LogMonitor extends Thread {

    private Logger logger = Logger.getLogger(getClass());

    private RedisService redisService = BeanFactory.getBean(RedisService.class);

    private AtomicBoolean working = new AtomicBoolean(false);

    private AtomicBoolean started = new AtomicBoolean(false);

    private static String logKey = "logstash_risky_log";

    private long sleepTime = 1000;

    private LogProcessor notifyLogProcessor = new NotifyLogProcessor();

    @Override
    public synchronized void start() {
        working.set(true);
        if (started.getAndSet(true)) {
            return;
        }
        super.start();
    }

    @Override
    public void run() {
        while (true) {
            // 开启状态下才去队列取数据
            if (working.get()) {
                String log = redisService.lpop(logKey);
                if (log != null) {
                    // 取出日志后可以根据需要交给不同的处理器进行处理
                    notifyLogProcessor.process(log);// 报警处理器，满足条件的日志会发送给用户
                    // 可以交给更多处理器处理，这里根据实际情况添加
                }
            }
            try {
                sleep(sleepTime);// 线程暂停一段时间
            } catch (InterruptedException e) {
                logger.error("sleep error " + e.getMessage(), e);
            }
        }
    }

    public void pause() {
        working.set(false);
    }

    public boolean isWorking() {
        return working.get();
    }

    public void switchChange(boolean on) {
        if (on) {
            start();
        } else {
            pause();
        }
    }
}
