package com.atlshearer.tournamentmanager.commands.team;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.tournament.SimplePlayer;
import com.atlshearer.tournamentmanager.tournament.Team;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;

public class Info extends Command {

	public Info(Command parent) {
		super(parent);
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		if (args.length != 0) {
			sender.sendMessage(ChatColor.GREEN + getHelp());
		}

		try {
			Team team = null;
			if (plugin.isTournamentEnabled()) {
				team = DatabaseUtils.getTeamByName(pargs.get(0), plugin.getCurrentTournament());
			} 
			
			if (team == null) {
				// A team with no score will not be found by previous request
				team = DatabaseUtils.getTeamByName(pargs.get(0));				
			}
			
			if (team == null) {
				sender.sendMessage(ChatColor.RED + String.format("No team by the name %s was found.", pargs.get(0)));
				return;
			}
			
			ArrayList<SimplePlayer> players = null;
			
			if (plugin.isTournamentEnabled() && team.score != null) {
				players = DatabaseUtils.getPlayersInTeam(team.id, plugin.getCurrentTournament());
			}
			
			if (players == null) {
				players = DatabaseUtils.getPlayersInTeam(team.id);
			}
			
			sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "TEAM INFO");
			sender.sendMessage("Team Name: " + team.name);
			sender.sendMessage("Team ID: " + team.id);
			if (team.score != null) {
				sender.sendMessage("Team Score: " + team.score);
			}
			
			if (players != null && players.size() != 0) {
				sender.sendMessage(ChatColor.ITALIC + "Players" + (team.score == null ? "" : " - score"));
				for (SimplePlayer player : players) {
					if (player.score != null) {
						sender.sendMessage(String.format("%s - %d points", player.username, player.score));
					} else {
						sender.sendMessage(player.username);						
					}
				}	
			}
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
		
	}

	@Override
	public String getName() {
		return "info";
	}
	
	@Override
	public String getHelp() {
		return "Usage - /tm team <team_name> info";
	}

	@Override
	protected String getBasePermission() {
		return "info";
	}

}
