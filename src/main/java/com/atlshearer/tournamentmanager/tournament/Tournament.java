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
			this.teams.add(new Team(i));
		}
	}
	
	public ArrayList<Team> getTeams() {
		return teams;
	}
}
