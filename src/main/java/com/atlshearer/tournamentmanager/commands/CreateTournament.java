package com.atlshearer.tournamentmanager.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.TournamentManager;

public class CreateTournament implements SubCommand {

	private TournamentManager plugin;
	
	public CreateTournament(TournamentManager plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if (args.length < 1) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage.");
    		player.sendMessage(help());
    		return true;
		}
		
		String name = String.join(" ", args);
		
		if (name.length() > 16) {
    		player.sendMessage(ChatColor.RED + "Name must be less than 16 characters long.");
    		player.sendMessage(ChatColor.GRAY + "(" + name.length() + " > 16)");
    		return true;
		}
		
		try {
			String prefix = this.plugin.getConfig().getString("data.table_prefix");
			
			this.plugin.database.update(
					"INSERT INTO " + prefix + "tournament (name) VALUES ('" + name + "')");
		} catch (SQLException e) {
			player.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public String help() {
		return "Help message for createtournament command";
	}

	@Override
	public String permission() {
		return "tournamentmanager.tournament.create";
	}

	@Override
	public String name() {
		return "createtournament";
	}
	
	
	
}