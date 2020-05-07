package com.atlshearer.tournamentmanager;

import org.bukkit.plugin.java.JavaPlugin;

import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.commands.RootCommand;
import com.atlshearer.tournamentmanager.listeners.LogoutEvent;
import com.atlshearer.tournamentmanager.listeners.SurvivalGamesListeners;
import com.atlshearer.tournamentmanager.listeners.TNTRunListeners;
import com.atlshearer.tournamentmanager.tournament.Tournament;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;
import com.atlshearer.tournamentmanager.utils.PlayerUtils;

public class TournamentManager extends JavaPlugin {
	public Command rootCommand;
	
	private Tournament tournament;
	
	@Override
	public void onEnable() {		
		this.saveDefaultConfig();
		
		this.getLogger().info(this.getConfig().getString("data.address"));
		
		// Enable database
		DatabaseUtils.onEnable(this);
		PlayerUtils.onEnable(this);
		
		// Enable command handling
		rootCommand = new RootCommand();
		
		getCommand("tournamentmanager").setExecutor(rootCommand);
		getCommand("tournamentmanager").setTabCompleter(rootCommand);
		
		// Register Events
		getServer().getPluginManager().registerEvents(new LogoutEvent(this), this);
		getServer().getPluginManager().registerEvents(new SurvivalGamesListeners(), this);
		getServer().getPluginManager().registerEvents(new TNTRunListeners(this), this);
		
		// Set tournament to null
		tournament = null;
	}
	
	@Override
	public void onDisable() {
		DatabaseUtils.onDisable();
	}
	
	public boolean isTournamentEnabled() {
		return this.tournament != null;
	}
	
	public Tournament getCurrentTournament() {
		return this.tournament;
	}
	
	public void setCurrentTournament(Tournament tournament) {
		this.tournament = tournament;
		this.getLogger().info("Tournament now set to " + tournament.name);
	}
	
	public void createTournament(String name) {
		this.getLogger().warning("Create tournament not impl.");
		this.getLogger().warning("Name req: " + name);
	}
}
