package com.atlshearer.tournamentmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import com.atlshearer.tournamentmanager.commands.*;


public class CommandHandler implements CommandExecutor, TabCompleter {
	private static CommandHandler instance;
	
	private TournamentManager plugin;
	private HashMap < String, SubCommand > commands;
	public List<String> tabCompletionList = new ArrayList<String>();
	
	public CommandHandler(TournamentManager plugin) {
		CommandHandler.instance = this;
		this.plugin = plugin;
		commands = new HashMap < String, SubCommand > ();
		
		// Load commands
		/*addCommand(new Join(this.plugin));
		addCommand(new ListTeams(this.plugin));
		addCommand(new Test());
		addCommand(new TeamInfo(this.plugin));
		addCommand(new Leave(this.plugin));
		addCommand(new ListTournaments(this.plugin));
		addCommand(new TournamentAddTeam(this.plugin));
		addCommand(new AddPlayer(this.plugin));
		addCommand(new TeamAddPlayer(this.plugin));
		addCommand(new ListPlayers(this.plugin));
		addCommand(new SetTournament(this.plugin));
		addCommand(new GetPlayerScore(this.plugin));
		addCommand(new SetPlayerScore(this.plugin));
		addCommand(new GetTeamScore(this.plugin));
		addCommand(new Leaderboard(this.plugin));*/
		
		// Add help command to tabCompletion
		tabCompletionList.add("help");
	}

	public void addCommand(SubCommand command) {
		commands.put(command.name(), command);
		tabCompletionList.add(command.name());
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
		PluginDescriptionFile pdfFile = plugin.getDescription();

		if (command.getName().equalsIgnoreCase("tournamentmanager")) {
			if (args == null || args.length < 1) {
				sender.sendMessage("Version " + pdfFile.getVersion() + " by Happy3");
				sender.sendMessage("Type /tm help for command information");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("help")) {
				
				if (args.length == 1) {
					sender.sendMessage("Implement generic help message");
					
					for (SubCommand subCommand : commands.values()) {
						sender.sendMessage(subCommand.help());
					}
					
				} else {
					sender.sendMessage("Implement specific help message");
				}
				
				return true;
			}
			
			String subCommand = args[0];
			
			// Stripping first argument from command?
			Vector < String > l = new Vector < String > ();
			l.addAll(Arrays.asList(args));
			l.remove(0);
			args = (String[]) l.toArray(new String[0]);
			
			if (!commands.containsKey(subCommand)) {
				sender.sendMessage("Command doesn't exist.");
				sender.sendMessage("Type /tm help for command information");
				return true;
			}
			
			try {
				sender.sendMessage(ChatColor.GRAY + "Handle command [" + subCommand + "] " + Arrays.toString(args));
				if (sender instanceof Player) {
					SubCommand subCmdObj = commands.get(subCommand);
					if (sender.hasPermission(subCmdObj.permission())) {
						commands.get(subCommand).onCommand((Player) sender, args);
					} else {
						sender.sendMessage(ChatColor.RED + "You do not have the required permission. " + subCmdObj.permission());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return true;
		}
		return false;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (command.getName().equalsIgnoreCase("tournamentmanager")) {
			if(args.length == 1) {
				List<String> list = new ArrayList<String>();	
				
				for (SubCommand subCommand : commands.values()) {
					if (subCommand.name().startsWith(args[0].toLowerCase()) && sender.hasPermission(subCommand.permission())) {
						list.add(subCommand.name());
					}
				}
				
				return list;
				
			} else if(args.length == 2) {
				List<String> helpList = new ArrayList<String>();
				List<String> helpListOut = new ArrayList<String>();
				
				if(args[0].equalsIgnoreCase("help")) {
					helpList.add("player");
					helpList.add("staff");
					helpList.add("admin");
					
					for(String string : helpList) {
						if (string.startsWith(args[1].toLowerCase())) {
							helpListOut.add(string);
						}
					}
				} else if(args[0].equalsIgnoreCase("reload")) {
					helpList.add("all");
					helpList.add("games");
					helpList.add("settings");
					
					for(String string : helpList) {
						if (string.startsWith(args[1].toLowerCase())) {
							helpListOut.add(string);
						}
					}
				}
				
				return helpListOut;
			}
		}
		
		return new ArrayList<String>();
	}
	
	public static CommandHandler getInstance() {
		return instance;
	}

}
