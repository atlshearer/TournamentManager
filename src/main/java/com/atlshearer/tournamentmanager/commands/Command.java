package com.atlshearer.tournamentmanager.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.atlshearer.tournamentmanager.TournamentManager;

public abstract class Command implements CommandExecutor, TabCompleter {	
	
	private HashMap<String, Command> children = new HashMap<String, Command>();
	protected TournamentManager plugin = (TournamentManager) Bukkit.getPluginManager().getPlugin("TournamentManager");
	public final String permission;
	
	
	// ----- Constructors ----- // 
	
	
	public Command() {
		permission = getBasePermission();
	}
	
	public Command(Command parent) {
		permission = parent.permission + '.' + getBasePermission();
	}	
	
	
	// ----- Interface ----- //
	
	@Override
	abstract public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args);
	
	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias,
			String[] args) {
		if (args == null || args.length == 0) {
			return new ArrayList<String>();
		}
		
		if (children.containsKey(args[0])) {
			String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
			return children.get(args[0]).onTabComplete(sender, command, alias, newArgs);
		}
		
		ArrayList<String> suggestions = new ArrayList<String>();
		
		for (Command child : children.values()) {
			if (child.getName().startsWith(args[0].toLowerCase()) && child.hasPermission(sender)) {
				suggestions.add(child.getName());
			}
		}
		
		return suggestions;
	}
	
	
	// ----- Public Getters ----- //
	
	public String getHelp() {
		return String.format("Generic help for (%s). Required permission (%s)", getName(), permission);
	}
	
	abstract public String getName();
	
	
	// ----- Protected Helpers ----- //
	
	protected void passToChild(CommandSender sender, org.bukkit.command.Command command, String label, 
			String[] args) throws InvalidCommandNameException {
		if (!children.containsKey(args[0])) {
			throw new InvalidCommandNameException(String.format("%s is not a valid child-command of %s", args[0], getName()));
		}
		
		// Strip args[0] off command
		
		String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
		String newLabel = label + ' ' + args[0];
		children.get(args[0]).onCommand(sender, command, newLabel, newArgs);
	}
	
	protected void addChild(Command child) {
		if (children == null) {
			children = new HashMap<String, Command>();
		}
		children.put(child.getName(), child);
	}
	
	/**
	 * Performs a DFS through the children to find a leaf node where the permission is true
	 * 
	 * @param sender
	 * @return true if the sender has the required permission for ANY child-leaf
	 */
	protected boolean hasPermission(CommandSender sender) {
		// Base case
		if (children.size() == 0) {
			return sender.hasPermission(permission);
		}
		
		// Recursive case
		for (Command child : children.values()) {
			if (child.hasPermission(sender)) {
				return true;
			}
		}
		
		return false;
	}
	
	abstract protected String getBasePermission();
}
