package com.atlshearer.tournamentmanager.commands;

import org.bukkit.entity.Player;

public class ListTeams implements SubCommand {

	public boolean onCommand(Player player, String[] args) {
		// Handle command
    	player.sendMessage("You executed the listteams command");
    	return true;
	}

	public String help() {
		return "Help message for listteams command";
	}

	public String permission() {
		return "tournamentmanager.listteams";
	}

	public String name() {
		return "listteams";
	}

}
