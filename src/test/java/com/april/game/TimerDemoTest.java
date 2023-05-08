package com.april.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimerDemoTest {

    @Test
    public void testRun() throws InterruptedException {
        TimerDemo timer1 = new TimerDemo();
        timer1.run(1);

        TimerDemo timer2 = new TimerDemo();
        timer2.run(2);

        // 設定要等多久，不然他會直接停掉
        Thread.sleep(1000 * 5);
    }
}