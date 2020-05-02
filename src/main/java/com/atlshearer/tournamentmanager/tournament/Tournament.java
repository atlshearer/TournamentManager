package com.atlshearer.tournamentmanager.tournament;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Tournament {

	private Plugin plugin;
	private ArrayList<Team> teams;

	
	public Tournament(Plugin plugin, int numTeams) {
		this.plugin = plugin;
		
		this.teams = new ArrayList<Team>();
		
		for (int i = 0; i < numTeams; i++) {
			this.teams.add(new Team(i + 1));
		}
	}
	
	public ArrayList<Team> getTeams() {
		return teams;
	}
	
	public Team getTeam(int teamNumber) {
		return teams.get(teamNumber - 1);
	}
	
	/**
	 * Returns the team that the given player is a part of, or returns
	 * null if player is not on a team.
	 * 
	 * @param player
	 * @return
	 */
	public Team getTeamFromPlayer(Player player) {
		for (Team team : teams) {
			if (team.getPlayers().contains(player)) {
				return team;
			}
		}
		
		return null;
	}
}
