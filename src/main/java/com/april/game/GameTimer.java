package com.april.game;

import com.april.call.Callable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameTimer {
    private ScheduledExecutorService timer;
    private Integer time = 1;

    // 初始化
    public void init(int time) {
        this.timer = Executors.newSingleThreadScheduledExecutor();
        this.time = time;
    }

    // 倒數
    public void countDown(Callable printer) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        this.timer.scheduleAtFixedRate(()-> {
            printer.call(time.toString());
            time--;
            if (time <= 0) {
                stop();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    // 等待的最大秒數
    public void await(int timeoutWithSeconds) {
        try{
            this.timer.awaitTermination(timeoutWithSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 停止 Timer
    public void stop() {
        this.timer.shutdown();
    }
}
