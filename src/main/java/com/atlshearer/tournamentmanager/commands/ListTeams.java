package com.atlshearer.tournamentmanager.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.TournamentManager;

public class ListTeams implements SubCommand {
	
	private TournamentManager plugin;
	
	public ListTeams(TournamentManager plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(Player player, String[] args) {
    	if (args.length != 1 || (args[0].compareToIgnoreCase("all") != 0 && !args[0].chars().allMatch(Character::isDigit))) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage.");
    		player.sendMessage(help());
    		return true;
    	}
    	
		String prefix = this.plugin.getConfig().getString("data.table_prefix");
		
		try {
			StringBuilder requestStrBuilder = new StringBuilder("SELECT %1$steam.id, %1$steam.name FROM %1$steam");
			
			// Check all flag
			if (args[0].compareToIgnoreCase("all") != 0) {
				requestStrBuilder.append(" JOIN %1$stournament_team ON %1$stournament_team.team_id = %1$steam.id ");
				requestStrBuilder.append("WHERE %1$stournament_team.tournament_id = ");
				requestStrBuilder.append(args[0]);
			}
			
			requestStrBuilder.append(";");
			
			String requestStr = String.format(requestStrBuilder.toString(), prefix);
			
			this.plugin.getLogger().info(requestStr);
			
			player.sendMessage(ChatColor.BLUE + "Team ID - " + ChatColor.BOLD + "Team Name");
			this.plugin.database.query(requestStr, results -> {
				if (results != null) {
					while(results.next()) {
						int id = results.getInt("id");
						String name = results.getString("name");
						player.sendMessage(id + " - " + ChatColor.BOLD + name);
					}
				} else {
					player.sendMessage("No teams found.");
				}
			});
		} catch (SQLException e) {
			player.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
    	
    	
    	return true;
	}

	public String help() {
		return "Usage - /tm listteam <all|tournament_id>";
	}

	public String permission() {
		return "tournamentmanager.listteams";
	}

	public String name() {
		return "listteams";
	}

}
