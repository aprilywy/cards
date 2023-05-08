package com.april.game;

//import java.util.Timer;
//import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerDemo {
    private int time = 0;

    /**
     * Timer雖然有倒數計時和計時的功能，但在遊戲中需要的是一出牌就停止計時，因此這邊不適用；
     * ScheduledExecutorService一樣也是倒數計時和計時功能，不過可以透過awaitTermination()
     * 的設定，等前一個計時結束後，開始下一個倒數計時
     * Java学习：Timer与ScheduledExecutorService的区别
     * https://blog.csdn.net/nalw2012/article/details/49633413
     */
//    public void run() {
//        // 建立Timer
//        Timer timer = new Timer();
//
//        // 要執行的工作
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                doSomething(time--);
//            }
//        };
//
//        // 開始執行，第一次執行延遲0秒，每次執行間隔1秒(1000毫秒)
//        timer.schedule(task, 0, 1000);
//    }

    public void run(int task) throws InterruptedException {
        // 要執行什麼
        // 也定義延遲 0 秒開始執行，每次間隔 1 秒，最方便的是，可以透過 TimeUnit.SECONDS 來自定義單位，不必自己換算
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(() -> {
            doSomething(task, time++);

            // 設定一下中斷點，執行 5 秒就結束
            if (time > 5) {
                timer.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);

        // 等 timer 執行 7 秒
        timer.awaitTermination(7, TimeUnit.SECONDS);
    }

    public void doSomething(int task, int n) {
        System.out.println(task + ": " + n);
    }
}
