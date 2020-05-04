package com.atlshearer.tournamentmanager.listeners;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.atlshearer.tournamentmanager.DatabaseUtils;
import com.atlshearer.tournamentmanager.TournamentManager;
import com.thundergemios10.survivalgames.Game;
import com.thundergemios10.survivalgames.api.PlayerJoinArenaEvent;
import com.thundergemios10.survivalgames.api.PlayerKilledEvent;
import com.thundergemios10.survivalgames.api.PlayerLeaveArenaEvent;

public class SurvivalGamesListeners implements Listener {
	@EventHandler
	public void playerJoin(PlayerJoinArenaEvent event) {
		Player p = event.getPlayer();
		Game g = event.getGame();
		
		Bukkit.getLogger().info("<" + p.getDisplayName() + "> joined game " + g.getID());
	}
	
	@EventHandler
	public void playerLeave(PlayerLeaveArenaEvent event) {
		Player p = event.getPlayer();
		Game g = event.getGame();
		
		Bukkit.getLogger().info("<" + p.getDisplayName() + "> left game " + g.getID());
	}
	
	@EventHandler
	public void playerDied(PlayerKilledEvent event) {
		Player p = event.getPlayer();
		Player killer = event.getKiller();
		Game g = event.getGame();
		
		Bukkit.getLogger().info("<" + p.getDisplayName() + "> was killed by <" + killer.getDisplayName() + "> in game " + g.getID());
		
		TournamentManager tournamentManager = (TournamentManager) Bukkit.getPluginManager().getPlugin("TournamentManager");
		
		if (tournamentManager.isTournamentEnabled()) {
			Bukkit.getScheduler().runTaskAsynchronously(tournamentManager, new Runnable() {
				@Override
				public void run() {
					try {
						int oldScore;
						oldScore = DatabaseUtils.getPlayerScore(tournamentManager.getCurrentTournament(), killer.getUniqueId().toString());
						DatabaseUtils.setPlayerScore(tournamentManager.getCurrentTournament(), killer.getUniqueId().toString(), oldScore + 10);
					} catch (SQLException e) {
						Bukkit.broadcast(ChatColor.DARK_RED + "An SQL error occured. Please check logs.", "tournamentmanager.admin");
						e.printStackTrace();
					}					
				}
			});
			
			Bukkit.broadcastMessage("Giving player " + killer.getDisplayName() + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + " 10 Points for killing " + p.getDisplayName());
		} else {
			Bukkit.broadcast(ChatColor.RED + "No tournament is enable. No scores being given.", "tournamentmanager.admin");
		}
		
	}
}
