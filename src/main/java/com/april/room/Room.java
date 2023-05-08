package com.april.room;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Room {
	@Getter @Setter
	private String owner;

	@Getter
	private final ArrayList<String> guests;

	@Getter
	private final String roomId;


	public Room(String roomId,String ownerName) {
		this.roomId = roomId;
		this.owner = ownerName;
		this.guests = new ArrayList<>();
	}

	public boolean addGuest(String guestName) {
		if (this.guests.size() >= 3) {
			return false;
		}
		this.guests.add(guestName);
		return true;
	}

	public void removeGuest(String guestName) {
		this.guests.remove(guestName);
	}

	public int count() {
		return 1 + this.guests.size();
	}

	public ArrayList<String> getAllMembers() {
		ArrayList<String> allPeople = new ArrayList<>();
		allPeople.add(this.owner);
		allPeople.addAll(this.guests);
		return allPeople;
	}

	public Map<String, Object> getInfo() {
		Map<String , Object> info = new HashMap<>();
		info.put("roomId", this.roomId);
		info.put("owner", this.owner);
		info.put("guests", this.guests);
		info.put("number", this.count());
		return info;
	}
}
