package com.atlshearer.tournamentmanager.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.tournament.Team;

public class ListTeams implements SubCommand {
	
	private TournamentManager plugin;
	
	public ListTeams(TournamentManager plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(Player player, String[] args) {
    	player.sendMessage(ChatColor.BLUE + "Teams" + ChatColor.RESET);
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
