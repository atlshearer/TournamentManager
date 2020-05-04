package com.atlshearer.tournamentmanager.commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.TournamentManager;

public class TeamAddPlayer implements SubCommand {

	private TournamentManager plugin;
	
	public TeamAddPlayer(TournamentManager plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		if (args.length != 2) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage.");
    		player.sendMessage(help());
    		return true;
		}
		
		if (!args[0].chars().allMatch(Character::isDigit)) {
    		player.sendMessage(ChatColor.RED + "Incorrect usage. Team ID must be and integer.");
    		player.sendMessage(help());
    		return true;
		}
		
		Player target = Bukkit.getPlayer(args[1]);
		
		if (target == null) {
			player.sendMessage(ChatColor.RED + "Player not found.");
			return true;
		}
		
		try {
			String prefix = this.plugin.getConfig().getString("data.table_prefix");
			
			String requestStr = String.format(
					"INSERT INTO %1$steam_member (player_uuid, team_id) VALUE ('%2$s', %3$s);", 
					prefix,
					target.getUniqueId(),
					args[0]);
			
			this.plugin.database.update(requestStr);
		} catch (SQLException e) {
			player.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public String help() {
		return "Usage - /tm teamaddplayer <team_id> <player_name>";
	}

	@Override
	public String permission() {
		// TODO Auto-generated method stub
		return "tournamentmanager.team.player.add";
	}

	@Override
	public String name() {
		return "teamaddplayer";
	}
}
