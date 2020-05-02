package com.atlshearer.tournamentmanager.commands;

import org.bukkit.entity.Player;

public class Test implements SubCommand {

	@Override
	public boolean onCommand(Player player, String[] args) {
		for (String str : args) {
			player.sendMessage(str);
		}
		
		return true;
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String permission() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "test";
	}
		
}
