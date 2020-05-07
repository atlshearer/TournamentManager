package com.atlshearer.tournamentmanager.commands.player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.tournament.SimplePlayer;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;

public class ListPlayers extends Command {

	public ListPlayers(Command parent) {
		super(parent);
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		if (args.length != 0) {
			sender.sendMessage(ChatColor.RED + getHelp());
			return;
		}
		
		try {
			List<SimplePlayer> players = DatabaseUtils.getPlayers();
			
			if (players.isEmpty()) {
				sender.sendMessage(ChatColor.RED + "Warning:" + ChatColor.RESET + " No players found....");
				return;
			}
			
			sender.sendMessage(ChatColor.BOLD + "Player Name");
			for (SimplePlayer simplePlayer : players) {
				sender.sendMessage(simplePlayer.username);
			}
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
		
	}

	@Override
	public String getName() {
		return "listplayers";
	}

	@Override
	public String getHelp() {
		return "Usage - /tm listplayers";
	}
	
	@Override
	protected String getBasePermission() {
		return "player.list";
	}

}
