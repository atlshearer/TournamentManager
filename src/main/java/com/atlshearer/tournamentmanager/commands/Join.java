package com.atlshearer.tournamentmanager.commands;

import org.bukkit.entity.Player;

public class Join implements SubCommand {

    public boolean onCommand(Player player, String[] args) {
    	// Handle command
    	player.sendMessage("You executed the join command");
    	return true;
    }

    public String help() {
    	return "Help message for join command";
    }
    
    public String permission() {
    	return "tournamentmanager.join";
    }

	public String name() {
		return "join";
	}
	
}
