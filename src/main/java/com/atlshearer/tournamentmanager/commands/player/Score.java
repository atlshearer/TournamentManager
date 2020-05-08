package com.atlshearer.tournamentmanager.commands.player;

import java.sql.SQLException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.tournament.SimplePlayer;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;

public class Score extends Command {

	public Score(Command parent) {
		super(parent);
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		if (args.length != 0) {
			sender.sendMessage(ChatColor.RED + getHelp());
			return;
		}
		
		if (!plugin.isTournamentEnabled()) {
			sender.sendMessage(ChatColor.RED + "Please enable a tournament.");
			return;
		}
		
		try {
			SimplePlayer player = DatabaseUtils.getPlayerByName(pargs.get(0));
			
			if (player == null) {
				sender.sendMessage(ChatColor.RED + String.format("No player by the name %s was found.", args[0]));
				return;
			}
			
			int score = DatabaseUtils.getPlayerScore(plugin.getCurrentTournament(), player.uuid);
			
			sender.sendMessage(String.format("%s - §d§l%d points", player.username, score));
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "score";
	}

	@Override
	public String getHelp() {
		return "Usage - /tm player <player_name> score";
	}
	
	@Override
	protected String getBasePermission() {
		return "score";
	}

}
