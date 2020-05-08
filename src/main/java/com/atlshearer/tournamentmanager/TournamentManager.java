package com.atlshearer.tournamentmanager;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.atlshearer.tournamentmanager.api.Adaptor;
import com.atlshearer.tournamentmanager.api.TournamentManagerAPI;
import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.commands.RootCommand;
import com.atlshearer.tournamentmanager.listeners.LogoutEvent;
import com.atlshearer.tournamentmanager.listeners.TNTRunListeners;
import com.atlshearer.tournamentmanager.tournament.Tournament;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;
import com.atlshearer.tournamentmanager.utils.PlayerUtils;

public class TournamentManager extends JavaPlugin implements TournamentManagerAPI {
	private Command rootCommand;
	private Tournament tournament;
	
	private HashMap<String, Adaptor> adaptors;
	private Adaptor nextGame = null;	
	
	@Override
	public void onEnable() {
		adaptors = new HashMap<>();
		
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
		this.getLogger().info(() -> "Tournament now set to " + tournament.name);
	}
	
	public void createTournament(String name) {
		this.getLogger().warning("Create tournament not impl.");
		this.getLogger().warning(() -> "Name req: " + name);
	}
	
	public HashMap<String, Adaptor> getAdaptors() {
		return adaptors;
	}
	
	public void setNextGame(Adaptor adaptor) {
		nextGame = adaptor;
	}
	
	public Adaptor getNextGame() {
		return nextGame;
	}
	
	// API

	@Override
	public void registerAdaptor(Adaptor adaptor) {
		getLogger().info("Regiestering adaptor: " + adaptor.getAdaptorName());
		adaptors.put(adaptor.getAdaptorName(), adaptor);
		
		Command command = adaptor.getCommand();
		if (command != null) {
			// Add to relevant command node
		}
	}

	@Override
	public void addPoints(Adaptor adaptor, Player player, int points) {
		getLogger().info(() -> String.format("Giving player %s %d points as requested by %s", 
				player.getName(), points, adaptor.getAdaptorName()));
		
		// TODO make sure request is async
		/*
		if (tournamentManager.isTournamentEnabled()) {
			Bukkit.getScheduler().runTaskAsynchronously(tournamentManager, new Runnable() {
				@Override
				public void run() {
					try {
						DatabaseUtils.addToPlayerScore(tournamentManager.getCurrentTournament(), killer.getUniqueId().toString(), 10);

					} catch (SQLException e) {
						Bukkit.broadcast(ChatColor.DARK_RED + "An SQL error occured. Please check logs.", "tournamentmanager.admin");
						e.printStackTrace();
					}					
				}
			});
			
			Bukkit.broadcastMessage("Giving player " + killer.getDisplayName() + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + " 10 Points for killing " + p.getDisplayName());
		} else {
			Bukkit.broadcast(ChatColor.RED + "No tournament is enable. No scores being given.", "tournamentmanager.admin");
		}
		*/
	}

	@Override
	public void gameEnded(Adaptor adaptor) {
		getLogger().info(() -> String.format("Adaptor '%s' reports game end", 
				adaptor.getAdaptorName()));		
	}
}
