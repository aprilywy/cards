package com.april.game;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GameTimerList {
    Map<String, GameTimer> timerList = new HashMap<>();

    // 取得該房間 Timer
    public GameTimer get(String roomId) {
        return this.timerList.get(roomId);
    }

    // 新增該房間 Timer
    public void add(String roomId, GameTimer timer) {
        this.timerList.put(roomId, timer);
    }

    // 移除該房間 Timer
    public void remove(String roomId) {
        this.timerList.remove(roomId);
    }
}
