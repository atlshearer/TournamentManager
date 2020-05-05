package com.atlshearer.tournamentmanager.commands.player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.DatabaseUtils;
import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.commands.InvalidCommandNameException;
import com.atlshearer.tournamentmanager.tournament.SimplePlayer;

public class PlayerRoot extends Command {

	public PlayerRoot(Command parent) {
		super(parent);
		
		addChild(new Score(this));
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		
		if (args == null || args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
			sender.sendMessage(ChatColor.GREEN + getHelp());
		} else if (args.length == 1) {
			pargs.add(args[0]);
			
			//children.get("info").onCommand(sender, command, label, new String[0], pargs);
		} else {
			try {				
				String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
				pargs.add(args[0]);
				
				passToChild(sender, command, label, newArgs, pargs);
			} catch (InvalidCommandNameException e) {
				sender.sendMessage(ChatColor.RED + args[1] + " is not a valid sub-command of /" + label);
			}

		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias,
			String[] args) {
		ArrayList<String> suggestions = new ArrayList<String>();
		ArrayList<String> playerNames = new ArrayList<String>();
		
		
		if (args == null || args.length == 0) {
			return new ArrayList<String>();
		}
		
		
		try {
			for (SimplePlayer player : DatabaseUtils.getPlayers()) {
				playerNames.add(player.username);
			}
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
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
		return "player";
	}

	@Override
	public String getHelp() {
		return "Usage - /tm player <player_name> ...";
	}
	
	@Override
	protected String getBasePermission() {
		return "player";
	}

}
