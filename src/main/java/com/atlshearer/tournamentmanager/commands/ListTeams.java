package com.atlshearer.tournamentmanager.commands;

import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.tournament.Team;

public class ListTeams implements SubCommand {
	
	private TournamentManager plugin;
	
	public ListTeams(TournamentManager plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(Player player, String[] args) {
		// Handle command
    	player.sendMessage("You executed the listteams command");
    	
    	for (Team team : plugin.tournament.getTeams()) {
    		player.sendMessage(team.getTeamName());
    	}
    	
    	return true;
	}

	public String help() {
		return "Help message for listteams command";
	}

	public String permission() {
		return "tournamentmanager.listteams";
	}

	public String name() {
		return "listteams";
	}

}
