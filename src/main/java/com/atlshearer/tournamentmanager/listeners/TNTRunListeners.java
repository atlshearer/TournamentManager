package com.atlshearer.tournamentmanager.listeners;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;

import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.events.PlayerSpectateArenaEvent;
import tntrun.events.PlayerWinArenaEvent;

public class TNTRunListeners implements Listener {
	
	TournamentManager plugin;
	
	public TNTRunListeners(TournamentManager plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerFall(PlayerSpectateArenaEvent event) {
		TNTRun tntrun = (TNTRun) Bukkit.getPluginManager().getPlugin("TNTRun_reloaded");
		Arena arena = tntrun.amanager.getArenaByName(event.getArenaName());

		if (this.plugin.isTournamentEnabled()) {
			for (Player player : arena.getPlayersManager().getPlayers()) {
				player.sendMessage("You get " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "1 point" + ChatColor.RESET + " for surviving");
				
				Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
					@Override
					public void run() {
						try {
							DatabaseUtils.addToPlayerScore(plugin.getCurrentTournament(), player.getUniqueId().toString(), 1);
						} catch (SQLException e) {
							Bukkit.broadcast(ChatColor.DARK_RED + "An SQL error occured. Please check logs.", "tournamentmanager.admin");
							e.printStackTrace();
						}					
					}
				});
			}
		} else {
			Bukkit.broadcast(ChatColor.RED + "No tournament is enable. No scores being given.", "tournamentmanager.admin");
		}
	}
	
	@EventHandler
	public void onPlayerWin(PlayerWinArenaEvent event) {
		Player player = event.getPlayer();
		
		if (this.plugin.isTournamentEnabled()) {
			Bukkit.broadcastMessage(player.getDisplayName() + " has got " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "10 points" + ChatColor.RESET + " for winning.");
			
			Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
				@Override
				public void run() {
					try {
						DatabaseUtils.addToPlayerScore(plugin.getCurrentTournament(), player.getUniqueId().toString(), 10);
					} catch (SQLException e) {
						Bukkit.broadcast(ChatColor.DARK_RED + "An SQL error occured. Please check logs.", "tournamentmanager.admin");
						e.printStackTrace();
					}					
				}
			});
		} else {
			Bukkit.broadcast(ChatColor.RED + "No tournament is enable. No scores being given.", "tournamentmanager.admin");
		}
	}
}
