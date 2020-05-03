package com.atlshearer.tournamentmanager.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.TournamentManager;

public class ListPlayers implements SubCommand {

	private TournamentManager plugin;
	
	public ListPlayers(TournamentManager plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if (args.length != 0) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage.");
    		player.sendMessage(help());
    		return true;
		}
		
		try {
			String prefix = this.plugin.getConfig().getString("data.table_prefix");
			
			String requestStr = String.format(
					"SELECT * FROM %1$splayer;", 
					prefix);
			
			player.sendMessage(ChatColor.BLUE + "uuid - " + ChatColor.BOLD + "username");
			this.plugin.database.query(requestStr, results -> {
				if (results != null) {
					while(results.next()) {
						String uuid = results.getString("uuid");
						String username = results.getString("username");
						player.sendMessage(uuid + " - " + ChatColor.BOLD + username);
					}
				} else {
					player.sendMessage("No players found.");
				}
			});
		} catch (SQLException e) {
			player.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return "Help message for listplayers command";
	}

	@Override
	public String permission() {
		return "tournamentmanager.player.list";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "listplayers";
	}
}
