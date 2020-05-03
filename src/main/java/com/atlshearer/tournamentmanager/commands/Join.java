package com.atlshearer.tournamentmanager.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.tournament.Team;

public class Join implements SubCommand {

	private TournamentManager plugin;
	
	public Join(TournamentManager plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(Player player, String[] args) {
    	/*// Handle command
    	if (args.length != 1) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage.");
    		player.sendMessage(help());
    		
    		return true;
    	} 
    	
    	if (!args[0].chars().allMatch(Character::isDigit)) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage.");
    		player.sendMessage(help());
    		
    		return true;
    	}
    	
    	if (plugin.tournament.getTeamFromPlayer(player) != null) {
    		player.sendMessage(ChatColor.RED + "You are already on a team.");
    		player.sendMessage(ChatColor.RED + "You must leave that team before joining a new team.");
    		player.sendMessage(ChatColor.BLUE + "/tm leave");
    		
    		return true;
    	}
    	
    	Team team = plugin.tournament.getTeam(Integer.parseInt(args[0]));
    	team.addPlayer(player);
    	
    	
    	player.sendMessage("Joining team '" + team.getTeamName() + "'");   	*/
    	return true;
    }

    public String help() {
    	return "Help message for join command";
    }
    
    public String permission() {
    	return "tournamentmanager.join";
    }

	public String name() {
		return "join";
	}
	
}
