package com.atlshearer.tournamentmanager.commands.team;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.commands.InvalidCommandNameException;
import com.atlshearer.tournamentmanager.commands.RootCommand;

public class TeamRoot extends Command {

	public TeamRoot(Command parent) {
		super(parent);

		addChild(new TeamCreate(this));
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		if (args == null || args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
			sender.sendMessage(getHelp());
		} else {
			try {
				passToChild(sender, command, label, args);
			} catch (InvalidCommandNameException e) {
				sender.sendMessage(ChatColor.RED + args[0] + " is not a valid sub-command of /" + label);
				e.printStackTrace();
			}
		}
		
		return true;
	}

	@Override
	public String getBasePermission() {
		return "team";
	}

	@Override
	public String getName() {
		return "team";
	}

}
