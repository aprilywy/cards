package com.april.game;

import com.april.card.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter 
@Setter
public class Player {
    private String name;
    private ArrayList<Card> hands;

    public Player(String name) {
        this.name = name;
    }

    public int countHands() {
        return this.hands.size();
    }

    public void play(ArrayList<Card> cards) {
        for (Card card : cards) {
            this.hands.remove(card);
        }
    }
}
