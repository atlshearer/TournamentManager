package com.atlshearer.tournamentmanager.commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.DatabaseUtils;
import com.atlshearer.tournamentmanager.TournamentManager;

public class AddPlayer implements SubCommand {
	
	private TournamentManager plugin;
	
	public AddPlayer(TournamentManager plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if (args.length > 1) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage.");
    		player.sendMessage(help());
    		return true;
		}
		
		Player target = player;
		
		if (args.length == 1) {
			target = Bukkit.getPlayer(args[0]);
			
			if (target == null) {
				player.sendMessage(ChatColor.RED + "Player not found.");
				return true;
			}
		}
		
		try {
			DatabaseUtils.addPlayer(target);
			
			player.sendMessage(ChatColor.GREEN + target.getName() + " added to database successfully.");
		} catch (SQLException e) {
			player.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public String help() {
		return "Usage - /tm addplayer [player_name]";
	}

	@Override
	public String permission() {
		return "tournamentmanager.player.add";
	}

	@Override
	public String name() {
		return "addplayer";
	}
}
