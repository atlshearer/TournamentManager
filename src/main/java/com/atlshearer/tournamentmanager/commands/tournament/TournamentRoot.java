package com.atlshearer.tournamentmanager.commands.tournament;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.DatabaseUtils;
import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.commands.InvalidCommandNameException;
import com.atlshearer.tournamentmanager.tournament.Tournament;

public class TournamentRoot extends Command {

	public TournamentRoot(Command parent) {
		super(parent);
		
		addChild(new Create(this));
		addChild(new Enable(this));
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		
		if (args == null || args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
			sender.sendMessage(ChatColor.GREEN + "Usage - /tm tournament <tournamen_name> ...");
		} else if (args.length == 1) {
			// Only tournament name is entered
			sender.sendMessage("Tournament name: " + args[0]);
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
		ArrayList<String> tournamentNames = new ArrayList<String>();
		
		
		if (args == null || args.length == 0) {
			return new ArrayList<String>();
		}
		
		
		try {
			for (Tournament tournament : DatabaseUtils.getTournaments()) {
				tournamentNames.add(tournament.name);
			}
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
		
		
		if (tournamentNames.contains(args[0])) {
			return super.onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length));
		}		
		
		
		for (String name : tournamentNames) {
			if (name.toLowerCase().startsWith(args[0].toLowerCase())) {
				suggestions.add(name);
			}
		}
		
		return suggestions;
	}
	
	@Override
	public String getName() {
		return "tournament";
	}

	@Override
	protected String getBasePermission() {
		return "tournament";
	}

}
