package com.atlshearer.tournamentmanager.commands.leaderboard;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.DatabaseUtils;
import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.tournament.SimplePlayer;

public class Player extends Command {

	public Player(Command parent) {
		super(parent);
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		if (args.length != 0) {
			sender.sendMessage(ChatColor.RED + getHelp());
			return;
		}

		if (!this.plugin.isTournamentEnabled()) {
			sender.sendMessage(ChatColor.RED + "No tournament currently enabled.");
			return;
		}
				
		try {
			ArrayList<SimplePlayer> players = 
					DatabaseUtils.getPlayers(plugin.getCurrentTournament());
			
			if (players.size() == 0) {
				sender.sendMessage(ChatColor.RED + "Warning:" + ChatColor.RESET + " No players found...");
				return;
			}
			
			sender.sendMessage(ChatColor.BLUE + "Username - " + ChatColor.BOLD + "points");
			for (SimplePlayer player : players) {
				sender.sendMessage(player.username + " - " + ChatColor.BOLD + player.score + " points");				
			}
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "player";
	}
	
	@Override
	public String getHelp() {
		return "Usage - /tm leaderboard player";
	}

	@Override
	protected String getBasePermission() {
		return "player";
	}

}
