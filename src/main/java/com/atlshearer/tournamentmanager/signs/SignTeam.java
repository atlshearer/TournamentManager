package com.atlshearer.tournamentmanager.signs;

import java.sql.SQLException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;

import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.tournament.SimplePlayer;
import com.atlshearer.tournamentmanager.tournament.Team;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;

public class SignTeam extends SignBase {

	private Team team;
	
	public SignTeam(TournamentManager plugin, Location location) {
		super(plugin, location);
		
		team = null;
	}

	@Override
	public void updateSign() {
		if (location.getBlock().getState() instanceof Sign) {
			Sign signBlock = (Sign) location.getBlock().getState();
			
			if (team == null) { 
				try {
					team = DatabaseUtils.TeamUtils.getTeamByName(signBlock.getLine(2));
				} catch (SQLException e) {
					plugin.getServer().broadcast(ChatColor.DARK_RED + "An SQL Exception occured.", "tournamentmanger.admin");
					e.printStackTrace();
				}
			}
			
			if (team != null) {				
				List<SimplePlayer> players = null;
				
				try {
					players = DatabaseUtils.PlayerUtils.getPlayersInTeam(team.id);
				} catch (SQLException e) {
					plugin.getServer().broadcast(ChatColor.DARK_RED + "An SQL Exception occured.", "tournamentmanger.admin");
					e.printStackTrace();
				}
				
				if (players != null) {
					signBlock.setLine(3, String.format(
							ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.playerCount")),
							players.size(),
							plugin.getConfig().getInt("team.maxsize")));					
				}
				
			} else {
				signBlock.setLine(2, 
						ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.error")));
			}
			
			signBlock.update();
		}
		// TODO throw something?
	}

	public boolean isForTeam(String teamName) {
		return team.name.equalsIgnoreCase(teamName);
	}
}
