package com.atlshearer.tournamentmanager.commands.tournament;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.DatabaseUtils;
import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.tournament.Team;
import com.atlshearer.tournamentmanager.tournament.Tournament;

public class AddTeam extends Command {

	public AddTeam(Command parent) {
		super(parent);
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		
		if (args.length != 1) {
			sender.sendMessage(ChatColor.RED + getHelp());
			return;
		}
		
		try {
			Tournament tournament = DatabaseUtils.getTournamentByName(pargs.get(0));
			Team team = DatabaseUtils.getTeamByName(args[0]);
			
			if (tournament == null) {
				sender.sendMessage(ChatColor.RED + String.format("No tournament by the name %s was found.", pargs.get(0)));
				return;
			}
			
			if (team == null) {
				sender.sendMessage(ChatColor.RED + String.format("No team by the name %s was found.", pargs.get(0)));
				return;
			}
			
			DatabaseUtils.addTeamToTournament(tournament, team);
			
			sender.sendMessage(ChatColor.GREEN + "Successfully added team to tournament.");
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias,
			String[] args) {
		ArrayList<String> suggestions = new ArrayList<String>();
		ArrayList<String> teamNames = new ArrayList<String>();
		
		
		if (args == null || args.length == 0) {
			return new ArrayList<String>();
		}
		
		
		try {
			for (Team team : DatabaseUtils.getTeams()) {
				teamNames.add(team.name);
			}
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
		
		
		
		for (String team : teamNames) {
			if (team.toLowerCase().startsWith(args[0].toLowerCase())) {
				suggestions.add(team);
			}
		}
		
		return suggestions;
	}

	@Override
	public String getName() {
		return "addteam";
	}
	
	@Override
	public String getHelp() {
		return "Usage - /tm tournament <tournament_name> addteam <team_name>";
	}

	@Override
	protected String getBasePermission() {
		return "addteam";
	}

}
