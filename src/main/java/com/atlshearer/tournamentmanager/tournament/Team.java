package com.atlshearer.tournamentmanager.tournament;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class Team {
	private ArrayList<Player> players;
	
	public final int teamNumber;
	
	public Team(int teamNumber) {
		this.players = new ArrayList<Player>();
		
		this.teamNumber = teamNumber;
	}
	
	public String getTeamName() {
		return "Team " + this.teamNumber;
	}
	
	public void addPlayer(Player player) {
		// TODO: Raise error/allow client code to check success
		if(!players.contains(player)) {
			players.add(player);
		}
	}
	
	public void removePlayer(Player player) {
		players.remove(player);
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
}
