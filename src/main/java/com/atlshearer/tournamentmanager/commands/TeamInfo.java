package com.atlshearer.tournamentmanager.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.tournament.Team;

public class TeamInfo implements SubCommand {

	private TournamentManager plugin;
	
	public TeamInfo(TournamentManager plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(Player player, String[] args) {
    	// Handle command
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
    	
    	Team team = plugin.tournament.getTeam(Integer.parseInt(args[0]));
    	
    	StringBuilder membersString = new StringBuilder("Players: ");
    	
    	for (Player teamPlayer : team.getPlayers()) {
    		membersString.append(teamPlayer.getDisplayName());
    		membersString.append(' ');
    	}
    	
    	player.sendMessage("Team name: " + team.getTeamName());   	
    	player.sendMessage(membersString.toString());
    	return true;		
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return "Help message for teaminfo command";
	}

	@Override
	public String permission() {
		return "tournamentmanager.teaminfo";
	}

	@Override
	public String name() {
		return "teaminfo";
	}

}
