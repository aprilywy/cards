package com.april.game;

import com.april.card.Card;
import com.april.card.PlayedCards;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter 
@Setter
public class PlayerActionMessage {
    private String action;
    private PlayedCards playedCards;

	
	// 把一張一張的Card放進ArrayList<Card>
    public void setPlayedCards(ArrayList<Card> cards) {
        this.playedCards = new PlayedCards(cards);
    }
}
