package com.atlshearer.tournamentmanager.commands.team;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.tournament.Team;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;

public class ListTeams extends Command {
	
	public ListTeams(Command parent) {
		super(parent);
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		if (args.length != 0) {
			sender.sendMessage(ChatColor.RED + getHelp());
			return;
		}
		
		List<Team> teams = DatabaseUtils.TeamUtils.getTeams();
		
		if (teams.isEmpty()) {
			sender.sendMessage(ChatColor.RED + "Warning:" + ChatColor.RESET + " No teams found....");
			return;
		}
		
		sender.sendMessage(ChatColor.BOLD + "Team Name" + ChatColor.ITALIC + " (Team ID)");
		for (Team team : teams) {
			sender.sendMessage(String.format("%s (%d)", team.name, team.id));
		}
	}

	@Override
	public String getName() {
		return "listteams";
	}

	@Override
	public String getHelp() {
		return "Usage - /tm listteams";
	}
	
	@Override
	protected String getBasePermission() {
		return "team.list";
	}

}
