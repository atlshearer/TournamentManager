package com.atlshearer.tournamentmanager.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.DatabaseUtils;
import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.tournament.SimplePlayer;

public class GetPlayerScore implements SubCommand {
	
	private TournamentManager plugin;
	
	public GetPlayerScore(TournamentManager plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if (args.length > 1) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage.");
    		player.sendMessage(help());
    		return true;
		}
		
		if (!this.plugin.isTournamentEnabled()) {
			player.sendMessage(ChatColor.RED + "Please enable a tournament.");
			return true;
		}
		
		SimplePlayer target = new SimplePlayer(player.getUniqueId().toString(), player.getName());
		
		try {
			if (args.length == 1) {
				target = DatabaseUtils.getPlayerByName(args[0]);
				
				if (target == null) {
					player.sendMessage(ChatColor.RED + "Player not found.");
					return true;
				}
			}
			
			int score = DatabaseUtils.getPlayerScore(this.plugin.getCurrentTournament(), target.uuid);
			player.sendMessage(target.username + " - " + ChatColor.BOLD + Integer.toString(score) + " points");
		} catch (SQLException e) {
			player.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}		
		
		return true;
	}

	@Override
	public String help() {
		return "Usage - /tm getplayerscore [playername]";
	}

	@Override
	public String permission() {
		return "tournamentmanager.player.score.get";
	}

	@Override
	public String name() {
		return "getplayerscore";
	}
	
	
}
