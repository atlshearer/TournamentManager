package com.atlshearer.tournamentmanager;

import org.bukkit.plugin.java.JavaPlugin;

import com.atlshearer.tournamentmanager.listeners.LogoutEvent;
import com.atlshearer.tournamentmanager.tournament.Tournament;

public class TournamentManager extends JavaPlugin {
	public CommandHandler commandHandler;
	public Tournament tournament;
	
	@Override
	public void onEnable() {
		getLogger().info("Test log to console!");
		
		this.saveDefaultConfig();
		
		this.getLogger().info("jdbc:mysql://" + this.getConfig().getString("data.address"));
		
		commandHandler = new CommandHandler(this);
		
		getCommand("tournamentmanager").setExecutor(commandHandler);
		getCommand("tournamentmanager").setTabCompleter(commandHandler);
		
		getServer().getPluginManager().registerEvents(new LogoutEvent(this), this);
		
		tournament = new Tournament(this, 8);
	}
	
	@Override
	public void onDisable() {
		
	}
	
}
