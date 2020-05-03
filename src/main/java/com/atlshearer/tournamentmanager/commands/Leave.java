package com.atlshearer.tournamentmanager.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.tournament.Team;

public class Leave implements SubCommand {
	private TournamentManager plugin;
	
	public Leave(TournamentManager plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(Player player, String[] args) {
    	/*// Handle command
    	if (args.length != 0) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage.");
    		player.sendMessage(help());
    		
    		return true;
    	} 
    	
    	if (plugin.tournament.getTeamFromPlayer(player) == null) {
    		player.sendMessage(ChatColor.RED + "You are not on a team.");
    		
    		return true;
    	}
    	
    	Team team = plugin.tournament.getTeamFromPlayer(player);
    	team.removePlayer(player);    	
    	
    	
    	player.sendMessage("Leaving team '" + team.getTeamName() + "'");*/
    	return true;
    }

    public String help() {
    	return "Help message for leave command";
    }
    
    public String permission() {
    	return "tournamentmanager.leave";
    }

	public String name() {
		return "leave";
	}
}
