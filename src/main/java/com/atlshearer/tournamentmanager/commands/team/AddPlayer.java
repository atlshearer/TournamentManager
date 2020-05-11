package com.atlshearer.tournamentmanager.commands.team;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.tournament.SimplePlayer;
import com.atlshearer.tournamentmanager.tournament.Team;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;

public class AddPlayer extends Command {

	public AddPlayer(Command parent) {
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
			Team team = DatabaseUtils.TeamUtils.getTeamByName(pargs.get(0));
			SimplePlayer player = DatabaseUtils.PlayerUtils.getPlayerByName(args[0]);
			
			if (team == null) {
				sender.sendMessage(ChatColor.RED + String.format("No team by the name %s was found.", pargs.get(0)));
				return;
			}
			
			if (player == null) {
				sender.sendMessage(ChatColor.RED + String.format("No player by the name %s was found.", args[0]));
				return;
			}
			
			DatabaseUtils.TeamUtils.addPlayerToTeam(player, team);
			
			sender.sendMessage(ChatColor.GREEN + "Successfully added player to team.");
			
			plugin.getSignManager().updateTeamSign(team.name);			
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias,
			String[] args) {
		ArrayList<String> suggestions = new ArrayList<>();
		ArrayList<String> playerNames = new ArrayList<>();
		
		if (args == null || args.length == 0) {
			return new ArrayList<>();
		}
		
		
		for (SimplePlayer player : DatabaseUtils.PlayerUtils.getPlayers()) {
			playerNames.add(player.username);
		}
		
		
		if (playerNames.contains(args[0])) {
			return super.onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length));
		}		
		
		
		for (String name : playerNames) {
			if (name.toLowerCase().startsWith(args[0].toLowerCase())) {
				suggestions.add(name);
			}
		}
		
		return suggestions;
	}

	@Override
	public String getName() {
		return "addplayer";
	}

	@Override
	protected String getBasePermission() {
		return "addplayer";
	}

	@Override
	public String getHelp() {
		return "Usage - /tm team addplayer <playername>";
	}
}
