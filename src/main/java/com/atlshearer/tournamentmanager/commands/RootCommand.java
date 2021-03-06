package com.atlshearer.tournamentmanager.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.adaptor.AdaptorRoot;
import com.atlshearer.tournamentmanager.commands.leaderboard.LeaderboardRoot;
import com.atlshearer.tournamentmanager.commands.player.ListPlayers;
import com.atlshearer.tournamentmanager.commands.player.PlayerRoot;
import com.atlshearer.tournamentmanager.commands.team.Join;
import com.atlshearer.tournamentmanager.commands.team.ListTeams;
import com.atlshearer.tournamentmanager.commands.team.TeamRoot;
import com.atlshearer.tournamentmanager.commands.tournament.ListTournament;
import com.atlshearer.tournamentmanager.commands.tournament.TournamentRoot;

public class RootCommand extends Command {

	public RootCommand() {
		super();
		
		// Register sub-commands
		addChild(new PlayerRoot(this));
		addChild(new TeamRoot(this));
		addChild(new TournamentRoot(this));
		addChild(new LeaderboardRoot(this));
		addChild(new AdaptorRoot(this));
		
		addChild(new ListPlayers(this));
		addChild(new ListTeams(this));
		addChild(new ListTournament(this));

		// Leaf nodes
		addChild(new AddPlayer(this));
		addChild(new JoinNext(this));
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		if (args == null || args.length == 0) {
			sender.sendMessage("Version " + this.plugin.getDescription().getVersion() + " by Happy3");
			sender.sendMessage("Type /tm help for command information");
			return true;
		}
		
		try {
			passToChild(sender, command, label, args, new ArrayList<String>());
		} catch (InvalidCommandNameException e) {
			sender.sendMessage(ChatColor.RED + args[0] + " is not a valid sub-command of /" + label);
		}
		
		return true;
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		throw new UnsupportedOperationException("If you see this error please report to developer. " + getName());
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
