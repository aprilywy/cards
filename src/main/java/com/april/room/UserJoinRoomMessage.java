package com.april.room;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserJoinRoomMessage {
	private String action;
	private String roomId;
}
