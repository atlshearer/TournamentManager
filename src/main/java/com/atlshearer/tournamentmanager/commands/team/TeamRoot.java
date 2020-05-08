package com.atlshearer.tournamentmanager.commands.team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.commands.InvalidCommandNameException;
import com.atlshearer.tournamentmanager.tournament.Team;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;

public class TeamRoot extends Command {

	public TeamRoot(Command parent) {
		super(parent);

		addChild(new Create(this));
		addChild(new AddPlayer(this));
		
		addChild(new Info(this));
		addChild(new Join(this));
		addChild(new Leave(this));
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		if (args == null || args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
			sender.sendMessage(ChatColor.GREEN + getHelp());
			return;
		}
		
		if (args.length == 1) {
			pargs.add(args[0]);
			
			children.get("info").onCommand(sender, command, label, new String[0], pargs);
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
		ArrayList<String> teamNames = new ArrayList<>();
		
		
		if (args == null || args.length == 0) {
			return new ArrayList<>();
		}
		
		
		List<Team> teams = DatabaseUtils.TeamUtils.getTeams();
		for (Team team : teams) {
			teamNames.add(team.name);
		}
		
		
		if (teamNames.contains(args[0])) {
			return super.onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length));
		}		
		
		
		for (String name : teamNames) {
			if (name.toLowerCase().startsWith(args[0].toLowerCase())) {
				suggestions.add(name);
			}
		}
		
		return suggestions;
	}
	
	@Override
	public String getBasePermission() {
		return "team";
	}

	@Override
	public String getHelp() {
		return "Usage - /tm tournament <tournamen_name> ...";
	}
	
	@Override
	public String getName() {
		return "team";
	}

}
