package com.atlshearer.tournamentmanager.commands.team;

import java.sql.SQLException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.DatabaseUtils;
import com.atlshearer.tournamentmanager.commands.Command;

public class Create extends Command {

	public Create(Command parent) {
		super(parent);
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		
		if (args.length != 0) {
			sender.sendMessage(ChatColor.GREEN + "Usage - /tm team <team_name> create");
		} else if (pargs.get(0).length() > 16) {
			sender.sendMessage(ChatColor.RED + "Name cannot be longer than 16 characters.");
		} else {
			try {
				if (DatabaseUtils.getTeamByName(pargs.get(0)) != null) {
					sender.sendMessage(ChatColor.RED + String.format("Team with the name '%s' already exists.", pargs.get(0)));
				} else {
					DatabaseUtils.createTeam(pargs.get(0));
					sender.sendMessage(ChatColor.GREEN + "Successfully added team to db.");
				}
			} catch (SQLException e) {
				sender.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getName() {
		return "create";
	}

	@Override
	protected String getBasePermission() {
		return "create";
	}

}
