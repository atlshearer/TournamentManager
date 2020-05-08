package com.atlshearer.tournamentmanager.api;

import org.bukkit.entity.Player;

public interface TournamentManagerAPI {
	public void registerAdaptor(Adaptor adaptor);
	
	public void addPoints(Adaptor adaptor, Player player, int points);
	
	public void gameEnded(Adaptor adaptor);
}
