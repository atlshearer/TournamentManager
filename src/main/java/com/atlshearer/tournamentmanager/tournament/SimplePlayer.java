package com.atlshearer.tournamentmanager.tournament;

import org.bukkit.entity.Player;

public class SimplePlayer {

	public final String uuid;
	public final String username;
	public final Integer score;
	
	public SimplePlayer(Player player) {
		this.uuid = player.getUniqueId().toString();
		this.username = player.getName();
		this.score = null;
	}
	
	public SimplePlayer(String uuid, String username) {
		this.uuid = uuid;
		this.username = username;
		this.score = null;
	}
	
	public SimplePlayer(String uuid, String username, int score) {
		this.uuid = uuid;
		this.username = username;
		this.score = score;
	}
	
}
