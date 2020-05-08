package com.atlshearer.tournamentmanager.utils;

import java.sql.SQLException;
import java.util.TreeSet;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.tournament.SimplePlayer;

public class PlayerUtils {

	private static TournamentManager plugin;
	
	private PlayerUtils() {}
	
	public static void onEnable(TournamentManager plugin) {
		PlayerUtils.plugin = plugin;
	}
	
	// Modifiers

	
	
	
	public static TreeSet<String> getPlayerNames() {
		TreeSet<String> names = new TreeSet<>();
		
		for (SimplePlayer player : DatabaseUtils.PlayerUtils.getPlayers()) {
			names.add(player.username);
		}
		
		return names;
	}
	
	public static TreeSet<String> getPlayersNamesNotInDB() {
		TreeSet<String> names = new TreeSet<>();
		
		for (OfflinePlayer player : plugin.getServer().getOnlinePlayers()) {
			names.add(player.getName());
		}
		
		names.removeAll(getPlayerNames());
		
		return names;
	}
}
