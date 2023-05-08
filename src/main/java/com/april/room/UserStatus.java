package com.april.room;


import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
class Status {
	private boolean inRoom = false;
	private String roomId = "";
	private boolean ready = false;
}

@Component
public class UserStatus {
	Map<String, Status> userStatus = new HashMap<>();

	public void setUserInRoom(String username, boolean isInRoom) {
		this.userStatus.get(username).setInRoom(isInRoom);
	}

	public boolean isUserInRoom(String username) {
		return this.userStatus.get(username).isInRoom();
	}

	public void setUserRoomId(String username, String roomId) {
		this.userStatus.get(username).setRoomId(roomId);
	}

	public String getUserRoomId(String username) {
		return this.userStatus.get(username).getRoomId();
	}

	public void setUserReady(String username, boolean isReady) {
		this.userStatus.get(username).setReady(isReady);
	}

	public boolean isUserReady(String username) {
		return this.userStatus.get(username).isReady();
	}

	public boolean containsUser(String username) {
		return this.userStatus.containsKey(username);
	}

	public void initialize(String username) {
		this.userStatus.put(username, new Status());
	}
}