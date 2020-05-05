package com.atlshearer.tournamentmanager.commands.team;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.Command;

public class TeamCreate extends Command {

	public TeamCreate(Command parent) {
		super(parent);
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		if (args.length != 0) {
			sender.sendMessage(getHelp());
		} else {
			sender.sendMessage(ChatColor.RED + args[0] + " is not a valid sub-command of /" + label);
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