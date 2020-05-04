package com.atlshearer.tournamentmanager.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.DatabaseUtils;
import com.atlshearer.tournamentmanager.TournamentManager;

public class GetTeamScore implements SubCommand {
	
	private TournamentManager plugin;
	
	public GetTeamScore(TournamentManager plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(Player player, String[] args) {
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
		
		if (!this.plugin.isTournamentEnabled()) {
			player.sendMessage(ChatColor.RED + "Please enable a tournament.");
			return true;
		}
		
		try {

			int score = DatabaseUtils.getTeamScoreByID(
					this.plugin.getCurrentTournament(), 
					Integer.parseInt(args[0]));
			player.sendMessage(args[0] + " - " + ChatColor.BOLD + Integer.toString(score) + " points");
		} catch (SQLException e) {
			player.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}		
		
		return true;
	}

	@Override
	public String help() {
		return "Usage - /tm getteamscore teamid";
	}

	@Override
	public String permission() {
		return "tournamentmanager.team.score.get";
	}

	@Override
	public String name() {
		return "getteamscore";
	}
	
}
