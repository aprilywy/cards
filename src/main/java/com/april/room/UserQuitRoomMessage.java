package com.april.room;

import lombok.Getter;
import lombok.Setter;


public class UserQuitRoomMessage {
	@Getter
	@Setter
	private String username;

	@Getter
	@Setter
	private String roomId;
}
