package com.atlshearer.tournamentmanager.commands.leaderboard;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.commands.InvalidCommandNameException;

public class LeaderboardRoot extends Command {

	public LeaderboardRoot(Command parent) {
		super(parent);
		
		addChild(new Team(this));
		addChild(new Player(this));
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		if (args.length != 1 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
			sender.sendMessage(ChatColor.RED + getHelp());
			return;
		}
		
		try {					
			passToChild(sender, command, label, args, pargs);
		} catch (InvalidCommandNameException e) {
			sender.sendMessage(ChatColor.RED + args[0] + " is not a valid sub-command of /" + label);
		}

	}

	@Override
	public String getName() {
		return "leaderboard";
	}

	@Override
	public String getHelp() {
		return "Usage - /tm leaderboard <team|player>";
	}
	
	@Override
	protected String getBasePermission() {
		return "leaderboard";
	}

}
