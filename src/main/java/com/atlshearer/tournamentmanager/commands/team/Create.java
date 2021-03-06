package com.atlshearer.tournamentmanager.commands.team;

import java.sql.SQLException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;

public class Create extends Command {

	public Create(Command parent) {
		super(parent);
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		if (args.length != 0) {
			sender.sendMessage(ChatColor.RED + getHelp());
			return;
		}
		
		if (pargs.get(0).length() > 16) {
			sender.sendMessage(ChatColor.RED + "Name cannot be longer than 16 characters.");
			return;
		}
		
		try {
			if (DatabaseUtils.TeamUtils.getTeamByName(pargs.get(0)) != null) {
				sender.sendMessage(ChatColor.RED + String.format("Team with the name '%s' already exists.", pargs.get(0)));
				return;
			}

			DatabaseUtils.TeamUtils.createTeam(pargs.get(0));
			sender.sendMessage(ChatColor.GREEN + "Successfully added team to db.");
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "create";
	}

	@Override
	public String getHelp() {
		return "Usage - /tm team <team_name> create";
	}
	
	@Override
	protected String getBasePermission() {
		return "create";
	}

}
