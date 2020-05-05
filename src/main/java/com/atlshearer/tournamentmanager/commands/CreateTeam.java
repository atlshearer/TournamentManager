package com.atlshearer.tournamentmanager.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.DatabaseUtils;
import com.atlshearer.tournamentmanager.TournamentManager;

public class CreateTeam implements SubCommand {

	private TournamentManager plugin;
	
	public CreateTeam(TournamentManager plugin) {
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
			DatabaseUtils.createTeam(name);
			
			player.sendMessage(ChatColor.GREEN + name + " added to database successfully.");
		} catch (SQLException e) {
			player.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public String help() {
		return "Usage - /tm createteam <team name>";
	}

	@Override
	public String permission() {
		return "tournamentmanager.team.create";
	}

	@Override
	public String name() {
		return "createteam";
	}

}
