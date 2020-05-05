package com.atlshearer.tournamentmanager.commands.team;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.commands.InvalidCommandNameException;

public class TeamCreate extends Command {

	public TeamCreate(Command parent) {
		super(parent);
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		if (args == null || args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
			sender.sendMessage(getHelp());
		} else {
			sender.sendMessage(ChatColor.RED + args[0] + " is not a valid sub-command of /" + label);
		}
		
		return true;
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
