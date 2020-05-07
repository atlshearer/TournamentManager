package com.atlshearer.tournamentmanager.commands.leaderboard;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;

public class Team extends Command {

	public Team(Command parent) {
		super(parent);
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		if (args.length != 0) {
			sender.sendMessage(ChatColor.RED + getHelp());
			return;
		}

		if (!this.plugin.isTournamentEnabled()) {
			sender.sendMessage(ChatColor.RED + "No tournament currently enabled.");
			return;
		}
				
		try {
			List<com.atlshearer.tournamentmanager.tournament.Team> teams = 
					DatabaseUtils.getTeamScores(plugin.getCurrentTournament());
			
			if (teams.isEmpty()) {
				sender.sendMessage(ChatColor.RED + "Warning:" + ChatColor.RESET + " No teams found...");
				return;
			}
			
			sender.sendMessage(ChatColor.BLUE + "Team Name - " + ChatColor.BOLD + "points");
			for (com.atlshearer.tournamentmanager.tournament.Team team : teams) {
				sender.sendMessage(team.name + " - " + ChatColor.BOLD + team.score + " points");				
			}
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "team";
	}
	
	@Override
	public String getHelp() {
		return "Usage - /tm leaderboard team";
	}

	@Override
	protected String getBasePermission() {
		return "team";
	}

}
