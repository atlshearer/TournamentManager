package com.atlshearer.tournamentmanager.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.team.TeamRoot;

public class RootCommand extends Command {

	public RootCommand() {
		super();
		
		// Register sub-commands
		addChild(new TeamRoot(this));
	}
	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		if (args == null || args.length == 0) {
			sender.sendMessage("Version " + this.plugin.getDescription().getVersion() + " by Happy3");
			sender.sendMessage("Type /tm help for command information");
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
		return "tournamentmanager";
	}

	@Override
	public String getName() {
		return "tm";
	}

}
