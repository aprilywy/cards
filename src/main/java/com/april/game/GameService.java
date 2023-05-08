package com.april.game;

import com.april.card.Card;
import com.april.card.Deck;
import com.april.card.Number;
import com.april.card.PlayedCards;
import com.april.card.Suit;
import com.april.room.Room;
import com.april.room.RoomList;
import com.april.room.RoomService;
import com.april.room.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class GameService {
	@Autowired
	private RoomList roomList;

	@Autowired
	private GameStatus gameStatus;

	@Autowired
	private RoomService roomService;

	@Autowired
	private GameTimerList gameTimerList;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private UserStatus userStatus;

	// 進入房間，開始遊戲
	public void initializeGameStatus(String roomId) {
        Room room = roomList.getRoomById(roomId);

		// 洗牌和發牌
        Deck deck = new Deck();
        deck.shuffle();
        ArrayList<ArrayList<Card>> hands = deck.deal();

		// 建立 Player，並分配手牌
        ArrayList<String> roomMembers = room.getAllMembers();

        Player[] players = new Player[4];
        for (int i = 0; i < 4; i ++) {
        	
            Player newPlayer = new Player(roomMembers.get(i));
            newPlayer.setHands(hands.get(i));
            players[i] = newPlayer;
        }

        gameStatus.add(roomId);
        gameStatus.setPlayers(roomId, players);
        gameStatus.setCurrentPlayer(roomId, this.findFirstPlayer(roomId));
    }

	// 取得指定玩家手牌
	public ArrayList<Card> getHandsByPlayerName(String name) {
	    return gameStatus.getHandsByPlayerName(name);
	}

	// 發送各玩家手牌
	public void sendHandsInfo(String roomId) {
	    Player[] players = this.gameStatus.getPlayers(roomId);
	    Map<String, Integer> handsNumber = new HashMap<>();
	    for (Player player : players) {
	        handsNumber.put(player.getName(), player.countHands());
	    }
	    this.roomService.sendMessageToRoom(roomId, "/queue/hands-info", handsNumber);
	}

	// 找到持有「梅花 3 」的玩家出牌開始遊戲
	public String findFirstPlayer(String roomId) {
	    Player[] players = this.gameStatus.getPlayers(roomId);
	    ArrayList<Card> hands;
	    for (Player player : players) {
	        hands = player.getHands();
	        for (Card card : hands) {
	            if (card.getSuit().equals(Suit.CLUB) && card.getNumber().equals(Number.THREE)) {
	                return player.getName();
	            }
	        }
	    }
	    return null;
	}

	// 從gameStatus取得資料，調用、修改play()
	public void sendMyHands(String name) {
		ArrayList<Card> myHands = this.getHandsByPlayerName(name);
		this.simpMessagingTemplate.convertAndSendToUser(name, "/queue/my-hands", myHands);
	}

	public void play(String roomId) {
	    Player[] players = gameStatus.getPlayers(roomId);
	    int index = 0;
	    // 找出目前出牌的玩家的 index
	    for (int i = 0; i < 4; i ++) {
	        if (players[i].getName().equals(this.gameStatus.getCurrentPlayer(roomId))) {
	            index = i;
	            break;
	        }
	    }

	    String currentPlayer;
	    String previousPlayer = null;
	    GameTimer timer = new GameTimer();
	    this.gameTimerList.add(roomId, timer);

	    // 延遲 5 秒再開始，這是為了等 Client 載入頁面、連接 webscoket 等動作
	    try {
	        Thread.sleep(5000);
	    } catch(Exception e) {
	        System.out.println(e.getMessage());
	    }

	 // 開始無窮迴圈，直到有人贏為止
	    while (true) {
	        currentPlayer = players[index].getName();
	        this.gameStatus.setCurrentPlayer(roomId, currentPlayer);

	        // 如果目前的玩家跟上一次出牌的玩家一樣，就說明其他玩家都 pass 了一輪，那這時候就可以自由出牌
	        if (currentPlayer.equals(previousPlayer)) {
	            this.gameStatus.setPreviousPlayer(roomId, null);
	            this.gameStatus.setPreviousPlayedCards(roomId, null);
	        }

	        timer.init(20);

			String finalCurrentPlayer = currentPlayer;
			String finalPreviousPlayer = previousPlayer;

			// 開始定義計時器每次倒數 1 秒要做什麼事情
	        timer.countDown((n) -> {
	            Map<String, Object> status = new HashMap<>();

	            // 取得當前出牌的玩家
	            status.put("currentPlayer", this.gameStatus.getCurrentPlayer(roomId));

	            // 取得上一玩家打出的牌
	            PlayedCards tmp = this.gameStatus.getPreviousPlayedCards(roomId);
	            if (tmp == null) {
	                status.put("previousPlayedCards", null);
	            } else {
	                status.put("previousPlayedCards", tmp.get());
	            }

				// 數到 1 ，就自動出牌了
				if (n.equals("1")) {
					boolean isFirstPlayer = this.findFirstPlayer(roomId) != null;
					// 如果是第一個出牌的玩家
					if (isFirstPlayer) {
						ArrayList<Card> cards = new ArrayList<>();
						cards.add(new Card(Suit.CLUB, Number.THREE));
						this.autoPlay(roomId, finalCurrentPlayer, cards);
						sendMyHands(finalCurrentPlayer);
						sendHandsInfo(roomId);
						return;
					}

					// 如果都 pass 一輪了
					if (finalCurrentPlayer.equals(finalPreviousPlayer)) {
						this.autoPlay(roomId, finalCurrentPlayer);
						sendMyHands(finalCurrentPlayer);
						sendHandsInfo(roomId);
					}
				}
			});

			timer.await(25);

			// 如果有玩家的手牌沒了就結束了
	        // 透過發送結束訊息給所有玩家，使他們結束遊戲，並顯示結算的畫面
	        if (previousPlayer != null && gameStatus.getHandsByPlayerName(previousPlayer).size() == 0) {

				// 定義結束遊戲的訊息
				Map<String, Object> endMessage = new HashMap<>();
				endMessage.put("winner", currentPlayer);
				endMessage.put("hands", this.gameStatus.getAllPlayersHands(roomId));
				roomService.sendMessageToRoom(roomId, "/queue/end", endMessage);

				this.gameStatus.remove(roomId);
	            this.gameTimerList.remove(roomId);
				for (Player player : players) {
					this.userStatus.setUserReady(player.getName(), false);
				}

				return;
			}

			// 新的一回合
			index = (index + 1) % 4;
			// 新的一輪
	        previousPlayer = currentPlayer;
	        gameStatus.setPreviousPlayer(roomId, previousPlayer);
	        index = (index + 1) % 4;
		}
	}

	public Player findPlayer(String roomId, String name) {
	    for (Player player : this.gameStatus.getPlayers(roomId)) {
	        if (player.getName().equals(name)) {
	            return player;
	        }
	    }
	    return null;
	}

	// 自動出牌，以防梅花三一直沒出牌
	public void autoPlay(String roomId, String playerName) {
		Player player = this.findPlayer(roomId, playerName);
		ArrayList<Card> cards = new ArrayList<>(player.getHands().subList(0, 1));
		player.play(cards);
		this.gameStatus.setPreviousPlayer(roomId,playerName);
		this.gameStatus.setPreviousPlayedCards(roomId, new PlayedCards(cards));
	}

	// 找出上一回合最後一位出牌者自動出牌，以防每個人一直pass
	public void autoPlay(String roomId, String playerName, ArrayList<Card> cards) {
		Player player = this.findPlayer(roomId, playerName);
		player.play(cards);
		this.gameStatus.setPreviousPlayer(roomId, playerName);
		this.gameStatus.setPreviousPlayedCards(roomId, new PlayedCards(cards));
	}
}
