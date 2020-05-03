package com.atlshearer.tournamentmanager.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.tournament.Team;

public class LogoutEvent implements Listener {

	final private TournamentManager plugin;
	
	public LogoutEvent(TournamentManager plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        /*Team team = plugin.tournament.getTeamFromPlayer(player);
        if (team != null) {
        	team.removePlayer(player);
        }*/
    }
	
}
