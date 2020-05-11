package com.atlshearer.tournamentmanager.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.api.AdaptorStateChangeEvent;

public class AdaptorListener implements Listener {

	private final TournamentManager plugin;
	
	public AdaptorListener(TournamentManager plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onAdaptorStateChange(AdaptorStateChangeEvent e) {
		plugin.getLogger().info("Adaptor change state recognised.");
		plugin.getSignManager().updateSigns();
	}
}
