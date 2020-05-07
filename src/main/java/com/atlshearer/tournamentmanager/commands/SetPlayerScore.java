package com.atlshearer.tournamentmanager.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.tournament.SimplePlayer;
import com.atlshearer.tournamentmanager.tournament.Tournament;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;

public class SetPlayerScore implements SubCommand {
	
	private TournamentManager plugin;
	
	public SetPlayerScore(TournamentManager plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if (args.length != 2) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage.");
    		player.sendMessage(help());
    		return true;
		}
		
		if (!args[1].chars().allMatch(Character::isDigit)) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage.");
    		player.sendMessage(help());
    		return true;
		}
		
		if (!this.plugin.isTournamentEnabled()) {
			player.sendMessage(ChatColor.RED + "Please enable a tournament.");
			return true;
		}		
		
		try {
			SimplePlayer target = null;
			
			target = DatabaseUtils.getPlayerByName(args[0]);
			
			if (target == null) {
				player.sendMessage(ChatColor.RED + "Player not found.");
				return true;
			}
			
			DatabaseUtils.setPlayerScore(
					this.plugin.getCurrentTournament(), 
					target.uuid, 
					Integer.parseInt(args[1]));
			player.sendMessage(target.username + " - " + ChatColor.BOLD + args[1] + " points");
		} catch (SQLException e) {
			player.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}		
		
		return true;
	}

	@Override
	public String help() {
		return "Usage - /tm setplayerscore <player_name> <score>";
	}

	@Override
	public String permission() {
		return "tournamentmanager.player.score.set";
	}

	@Override
	public String name() {
		return "setplayerscore";
	}
	
}
