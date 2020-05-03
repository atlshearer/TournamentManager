package com.atlshearer.tournamentmanager.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.TournamentManager;

public class ListTournaments implements SubCommand {

	private TournamentManager plugin;
	
	public ListTournaments(TournamentManager plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(Player player, String[] args) {
		if (args.length != 0) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage.");
    		player.sendMessage(help());
    		return true;
		}
		
		String prefix = this.plugin.getConfig().getString("data.table_prefix");
		
		try {
			this.plugin.database.query("SELECT * FROM `" + prefix + "tournament`;", results -> {
				if (results != null) {
					while(results.next()) {
						int id = results.getInt("id");
						String name = results.getString("name");
						player.sendMessage(id + " - " + ChatColor.BOLD + name);
					}
				}
			});
		} catch (SQLException e) {
			player.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
				
		return true;
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return "Help message for listtournaments command";
	}

	@Override
	public String permission() {
		return "tournamentmanager.tournament.list";
	}

	@Override
	public String name() {
		return "listtournaments";
	}

}
