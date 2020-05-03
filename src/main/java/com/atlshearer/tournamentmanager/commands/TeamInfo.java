package com.atlshearer.tournamentmanager.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.TournamentManager;

public class TeamInfo implements SubCommand {

	private TournamentManager plugin;
	
	public TeamInfo(TournamentManager plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(Player player, String[] args) {
    	// Handle command
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
    	
    	
		
    	StringBuilder membersString = new StringBuilder("Players: ");
    	
		try {
			String prefix = this.plugin.getConfig().getString("data.table_prefix");
			
			String requestStr = String.format(
					"SELECT %1$splayer.username FROM %1$splayer " + 
					"JOIN %1$steam_member ON %1$steam_member.player_uuid = %1$splayer.uuid " + 
					"WHERE %1$steam_member.team_id = %2$s", 
					prefix,
					args[0]);
			
			this.plugin.getLogger().info(requestStr);
			
			player.sendMessage(ChatColor.BLUE + "Team ID - " + ChatColor.BOLD + "Team Name");

			
			this.plugin.database.query(requestStr, results -> {
				if (results != null) {
					while(results.next()) {
						String username = results.getString("username");
						membersString.append(username).append(" ");
					}
				} else {
					player.sendMessage("No teams found.");
				}
			});
		} catch (SQLException e) {
			player.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
    	
    	player.sendMessage(String.format("%1$s - " + ChatColor.BOLD + "TeamName", args[0]));   	
    	player.sendMessage(membersString.toString());
    	return true;		
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return "Usage - /tm teaminfo <team_id>";
	}

	@Override
	public String permission() {
		return "tournamentmanager.teaminfo";
	}

	@Override
	public String name() {
		return "teaminfo";
	}

}
