package com.april.game;

import com.april.card.Card;
import com.april.card.PlayedCards;
import com.april.room.UserStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter 
@Setter
class Status {
    private Player[] players = new Player[4];
    private String previousPlayer = null;
    private PlayedCards previousPlayedCards = null;
    private String currentPlayer = null;
}

@Component
public class GameStatus {
    @Autowired
    private UserStatus userStatus;

    private final Map<String, Status> game = new HashMap<>();

    public void add(String roomId) {
        this.game.put(roomId, new Status());
    }

    public void remove(String roomId) {
        this.game.remove(roomId);
    }

    public void setPlayers(String roomId, Player[] players) {
        this.game.get(roomId).setPlayers(players);
    }

    public Player[] getPlayers(String roomId) {
        return this.game.get(roomId).getPlayers();
    }

    public void setPreviousPlayer(String roomId, String playerName) {
        this.game.get(roomId).setPreviousPlayer(playerName);
    }

    public String getPreviousPlayer(String roomId) {
        return this.game.get(roomId).getPreviousPlayer();
    }

    public void setPreviousPlayedCards(String roomId, PlayedCards playedCards) {
        this.game.get(roomId).setPreviousPlayedCards(playedCards);
    }

    public PlayedCards getPreviousPlayedCards(String roomId) {
        return this.game.get(roomId).getPreviousPlayedCards();
    }

    public void setCurrentPlayer(String roomId, String playerName) {
        this.game.get(roomId).setCurrentPlayer(playerName);
    }

    public String getCurrentPlayer(String roomId) {
        return this.game.get(roomId).getCurrentPlayer();
    }

	// 依照牌堆取得玩家打出去的牌
    public ArrayList<Card> getHandsByPlayerName(String name) {
        String roomId = this.userStatus.getUserRoomId(name);
        Player[] players = this.getPlayers(roomId);
        for (Player player : players) {
            if (player.getName().equals(name)) {
                return player.getHands();
            }
        }
        return null;
    }

	// 依照發牌結果取得玩家的手牌
//	public ArrayList<ArrayList<Card>> getAllPlayersHands(String roomId) {
//    ArrayList<ArrayList<Card>> result = new ArrayList<>();
//    Player[] players = this.game.get(roomId).getPlayers();
//    for (Player player : players) {
//        result.add(player.getHands());
//    }
//    return result;
//}

	// 依照發牌結果取得玩家的手牌（跟上面一樣，只是改成HashMap<> ???
	public HashMap<String, Object> getAllPlayersHands(String roomId) {
		HashMap<String, Object> result = new HashMap<>();
		Player[] players = this.game.get(roomId).getPlayers();
		for (Player player : players) {
			result.put(player.getName(), player.getHands());
		}

		return result;
	}
}
