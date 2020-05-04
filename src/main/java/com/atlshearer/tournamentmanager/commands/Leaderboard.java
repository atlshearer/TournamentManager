package com.atlshearer.tournamentmanager.commands;

import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.DatabaseUtils;
import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.tournament.Team;

public class Leaderboard implements SubCommand {
	
	private TournamentManager plugin;
	
	public Leaderboard(TournamentManager plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if (args.length != 0) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage.");
    		player.sendMessage(help());
    		return true;
		}
		
		if (!this.plugin.isTournamentEnabled()) {
			player.sendMessage(ChatColor.RED + "Please enable a tournament.");
			return true;
		}
		
		try {
			ArrayList<Team> teams = DatabaseUtils.getTeamScores(this.plugin.getCurrentTournament());
			
			player.sendMessage(ChatColor.BLUE + "Team Name - " + ChatColor.BOLD + "points");
			for (Team team : teams) {
				player.sendMessage(team.name + " - " + ChatColor.BOLD + team.score + " points");				
			}
		} catch (SQLException e) {
			player.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public String help() {
		return "Usage - /tm leaderboard";
	}

	@Override
	public String permission() {
		return "tournamentmanager.leaderboard.team";
	}

	@Override
	public String name() {
		return "leaderboard";
	}
	
	
}
