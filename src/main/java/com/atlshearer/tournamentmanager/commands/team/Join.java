package com.atlshearer.tournamentmanager.commands.team;

import java.sql.SQLException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.tournament.SimplePlayer;
import com.atlshearer.tournamentmanager.tournament.Team;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;

public class Join extends Command {

	public Join(Command parent) {
		super(parent);
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		if (args.length != 0) {
			sender.sendMessage(ChatColor.RED + getHelp());
			return;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
			return;
		}
		
		try {
			Player player = (Player) sender;
			
			Team playerTeam = DatabaseUtils.isPlayerInAnyTeam(player.getUniqueId().toString());
			
			if (playerTeam != null) {
				sender.sendMessage(ChatColor.RED + "You are already in a team.");
				sender.sendMessage(String.format("Use §1/tm team %s leave§r to leave", playerTeam.name));
				return;
			}
			
			Team team = DatabaseUtils.getTeamByName(pargs.get(0));
			
			if (team == null) {
				sender.sendMessage(ChatColor.RED + String.format("No team by the name %s was found.", pargs.get(0)));
				return;
			}
			
			DatabaseUtils.addPlayerToTeam(new SimplePlayer(player), team);
			
			sender.sendMessage(ChatColor.GREEN + "Successfully joined '" + team.name + "'.");
			
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "join";
	}

	@Override
	public String getHelp() {
		return "Usage - /tm team <team_name> join";
	}
	
	@Override
	protected String getBasePermission() {
		return "join";
	}

}
