package com.april.room;

import com.april.game.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.ArrayList;

@Controller
public class RoomWsController {
	@Autowired
	private RoomList roomList;

	@Autowired
	private UserStatus userStatus;

	@Autowired
	private RoomService roomService;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private GameService gameService;

	@MessageMapping("/room-list")
	@SendTo("/topic/room-list")
	public RoomListResponse getAllRooms() {
		RoomListResponse roomListResponse = new RoomListResponse();
		roomListResponse.setRooms(roomList.getRooms());

		return roomListResponse;
	}

	@MessageMapping("/room-info")
	public void getRoomInfo(Principal principal) {
		String username = principal.getName();
		String roomId = this.userStatus.getUserRoomId(username);
		this.roomService.sendRoomInfo(roomId);
	}

	@MessageMapping("/ready")
	public void readyToPlay(UserReadyMessage readyMessage, Principal principal) {
		// 設定使用者的準備狀態
		String username = principal.getName();
	    this.userStatus.setUserReady(username, readyMessage.isReady());

		// 取出所有房間內的成員
	    String roomId = this.userStatus.getUserRoomId(username);
	    Room room = roomList.getRoomById(roomId);
	    ArrayList<String> roomMembers = room.getAllMembers();

	    UserReadyResponse response = new UserReadyResponse();

		// counter 用於確定是否全部都準備了，如果不是就會給予回饋
	    int counter = 0;
	    for (String member : roomMembers) {
	        boolean isReady = this.userStatus.isUserReady(member);
	        if (isReady) {
	            counter += 1;
	        }
	        response.add(member, isReady);
	    }

		// 如果沒有全部都準備好，就把房主的準備狀態取消
	    String owner = room.getOwner();
	    if (counter == 4) {
	        response.setAllReady(true);
	    } else {
	        this.userStatus.setUserReady(owner, false);
	        response.add(owner, false);
	    }

		// 設定回饋給所有成員的訊息
		for (String member : roomMembers) {
	        response.setMessage("");
	        if (counter != 4 && member.equals(username)) {
	            if (member.equals(owner)) {
	                if (room.count() == 4) {
	                    response.setMessage("尚有人未準備好開始遊戲!");
	                } else {
	                    response.setMessage("人數不足!");
	                }
	            } else {
	                if (!readyMessage.isReady()) {
	                    response.setMessage("請快點準備!");
	                }
	            }
	        }

			// 個別發送訊息
	        simpMessagingTemplate.convertAndSendToUser(member, "/queue/ready", response);
	    }
		// 開始遊戲！記得注入GameService
		if (response.isAllReady()) {
	        this.gameService.initializeGameStatus(roomId);
	    }
	}
}
