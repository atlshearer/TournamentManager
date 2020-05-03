package com.atlshearer.tournamentmanager.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.TournamentManager;

public class TournamentAddTeam implements SubCommand {

	private TournamentManager plugin;
	
	public TournamentAddTeam(TournamentManager plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(Player player, String[] args) {
	   	if (args.length != 2) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage.");
    		player.sendMessage(help());
    		return true;
    	}
	   	
    	if (!args[0].chars().allMatch(Character::isDigit) || !args[1].chars().allMatch(Character::isDigit)) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage.");
    		player.sendMessage(help());
    		
    		return true;
    	}
	   	
    	
		try {
			String prefix = this.plugin.getConfig().getString("data.table_prefix");
			
			StringBuilder requestStr = new StringBuilder("INSERT INTO ");
			requestStr.append(prefix);
			requestStr.append("tournament_team (team_id, tournament_id) VALUES (");
			requestStr.append(args[0]);
			requestStr.append(", ");
			requestStr.append(args[1]);
			requestStr.append(");");
			
			this.plugin.database.update(requestStr.toString());
		} catch (SQLException e) {
			player.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			player.sendMessage(ChatColor.GRAY + "Make sure ids are correct.");
			e.printStackTrace();
		}
	   	
		return true;
	}

	@Override
	public String help() {
		return "Usage - /tm tournamentaddteam <team_id> [tournament_id]";
	}

	@Override
	public String permission() {
		return "tournamentmanager.tournament.addteam";
	}

	@Override
	public String name() {
		return "tournamentaddteam";
	}

}
