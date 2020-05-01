package com.atlshearer.tournamentmanager;

import org.bukkit.plugin.java.JavaPlugin;

public class TournamentManager extends JavaPlugin {
	CommandHandler commandHandler;
	@Override
	public void onEnable() {
		getLogger().info("Test log to console!");
		commandHandler = new CommandHandler(this);
		
		getCommand("tournamentmanager").setExecutor(commandHandler);
		getCommand("tournamentmanager").setTabCompleter(commandHandler);
	}
	
	@Override
	public void onDisable() {
		
	}
	
}
