package com.atlshearer.tournamentmanager.utils;

import java.sql.SQLException;
import java.util.TreeSet;

import org.bukkit.OfflinePlayer;

import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.tournament.SimplePlayer;

public class PlayerUtils {

	private static TournamentManager plugin;
	
	public static void onEnable(TournamentManager plugin) {
		PlayerUtils.plugin = plugin;
	}
	
	public static TreeSet<String> getPlayerNames() throws SQLException {
		TreeSet<String> names = new TreeSet<String>();
		
		for (SimplePlayer player : DatabaseUtils.getPlayers()) {
			names.add(player.username);
		}
		
		return names;
	}
	
	public static TreeSet<String> getPlayersNamesNotInDB() throws SQLException {
		TreeSet<String> names = new TreeSet<String>();
		
		for (OfflinePlayer player : plugin.getServer().getOnlinePlayers()) {
			names.add(player.getName());
		}
		
		names.removeAll(getPlayerNames());
		
		return names;
	}
}
