package com.atlshearer.tournamentmanager.commands;

import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.DatabaseUtils;
import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.tournament.Team;

public class ListTeams implements SubCommand {
	
	private TournamentManager plugin;
	
	public ListTeams(TournamentManager plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(Player player, String[] args) {
    	if (args.length > 1) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage.");
    		player.sendMessage(help());
    		return true;
    	}
    	
    	if (args.length == 1 && args[0].compareToIgnoreCase("all") != 0 && !args[0].chars().allMatch(Character::isDigit)) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage.");
    		player.sendMessage(help());
    		return true;
    	}
    	
    	ArrayList<Team> teams = new ArrayList<Team>();
		
		try {
	    	if (args.length == 0) {
	    		// TODO process request for current tournament
	    	} else if (args[0].compareToIgnoreCase("all") == 0) {
	    		teams = DatabaseUtils.getTeams();
	    	} else {
	    		teams = DatabaseUtils.getTeamsInTournament(Integer.parseInt(args[0]));
	    	}
		} catch (SQLException e) {
			player.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
			return true;
		}
		
		player.sendMessage(String.format(ChatColor.GRAY + "%d teams found.", teams.size()));
		player.sendMessage(ChatColor.BLUE + "Team ID - " + ChatColor.BOLD + "Team Name");
		for (Team team : teams) {
			player.sendMessage(team.id + " - " + ChatColor.BOLD + team.name);
		}    	
    	
    	return true;
	}

	public String help() {
		return "Usage - /tm listteam <all|tournament_id>";
	}

	public String permission() {
		return "tournamentmanager.listteams";
	}

	public String name() {
		return "listteams";
	}

}
