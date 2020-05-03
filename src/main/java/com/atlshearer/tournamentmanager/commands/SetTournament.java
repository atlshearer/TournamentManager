package com.atlshearer.tournamentmanager.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.DatabaseUtils;
import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.tournament.Tournament;

public class SetTournament implements SubCommand {
	
	private TournamentManager plugin;
	
	public SetTournament(TournamentManager plugin) {
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
		
		try {
			Tournament tournament = DatabaseUtils.getTournamentByID(Integer.parseInt(args[0]));
			
			if (tournament == null) {
				player.sendMessage(ChatColor.RED + "Tournament not found. No change has been made.");
				return true;
			}
			
			this.plugin.setCurrentTournament(tournament);
			player.sendMessage("Tournament updated to " + ChatColor.BOLD + tournament.name);
		} catch (SQLException e) {
			player.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public String help() {
		return "Usage - /tm settournament <tournament_id>";
	}

	@Override
	public String permission() {
		return "tournamentmanager.tournament.set";
	}

	@Override
	public String name() {
		return "settournament";
	}
}
