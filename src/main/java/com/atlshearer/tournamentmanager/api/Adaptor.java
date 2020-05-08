package com.atlshearer.tournamentmanager.api;

import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.commands.Command;

public interface Adaptor {
	enum STATE {
		DISABLED, // Players unable to join 
		ENABLED,  // Players are able to join
		RUNNING,  // Game has started - player cannot join
		ENDED     // Game has ended
	}
	
	public Command getCommand();
	
	public void playerJoin(Player player);
		
	public void enable();
	
	public void gameStart();
	
	public String getAdaptorName();
	
	public STATE getState();
	
}
