package com.atlshearer.tournamentmanager.commands.tournament;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.commands.InvalidCommandNameException;

public class TournamentRoot extends Command {

	public TournamentRoot(Command parent) {
		super(parent);
		
		addChild(new TournamentCreate(this));
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
				// Rearrange args so that next sub-command is args[0]
				
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
		
		if (args == null || args.length == 0) {
			return new ArrayList<String>();
		}
		
		// Load all tournament names
		ArrayList<String> tournamentNames = new ArrayList<String>();
		tournamentNames.add("a_tournament_name");
		
		// Check if given name is in there
			// if yes pass to super function super.onTabComplete without tournament name
			// else suggest from tournament list
		if (tournamentNames.contains(args[0])) {
			return super.onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length));
		}
		
		ArrayList<String> suggestions = new ArrayList<String>();
		
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
