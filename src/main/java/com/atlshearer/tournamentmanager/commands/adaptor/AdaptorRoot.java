package com.atlshearer.tournamentmanager.commands.adaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.api.Adaptor;
import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.commands.InvalidCommandNameException;

public class AdaptorRoot extends Command {

	public AdaptorRoot(Command parent) {
		super(parent);
		
		addChild(new Enable(this));
		addChild(new StartGame(this));
		addChild(new State(this));
	}
	
	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + getHelp());
			return;
		}
		
		if (args.length == 1) {
			// TODO adaptor info
			return;
		}
		
		try {				
			String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
			pargs.add(args[0]);
			
			passToChild(sender, command, label, newArgs, pargs);
		} catch (InvalidCommandNameException e) {
			sender.sendMessage(ChatColor.RED + args[1] + " is not a valid sub-command of /" + label);
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias,
			String[] args) {
		ArrayList<String> suggestions = new ArrayList<>();
		TreeSet<String> adaptorNames = new TreeSet<>();
		
		if (args == null || args.length == 0) {
			return new ArrayList<>();
		}
		
		for (Adaptor adaptor : plugin.getAdaptors().values()) {
			adaptorNames.add(adaptor.getAdaptorName());
		}
		
		if (adaptorNames.contains(args[0])) {
			return super.onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length));
		}		
		
		for (String name : adaptorNames) {
			if (name.toLowerCase().startsWith(args[0].toLowerCase())) {
				suggestions.add(name);
			}
		}
		
		return suggestions;
	}
	
	@Override
	public String getName() {
		return "adaptor";
	}
	
	@Override
	public String getHelp() {
		return "Usage - /tm adaptor <adaptor_name>";
	}

	@Override
	protected String getBasePermission() {
		return "adaptor";
	}

}
