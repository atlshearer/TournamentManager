package com.atlshearer.tournamentmanager;

import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.atlshearer.tournamentmanager.api.Adaptor;
import com.atlshearer.tournamentmanager.api.TournamentManagerAPI;
import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.commands.RootCommand;
import com.atlshearer.tournamentmanager.listeners.LogoutEvent;
import com.atlshearer.tournamentmanager.listeners.AdaptorListener;
import com.atlshearer.tournamentmanager.signs.SignListener;
import com.atlshearer.tournamentmanager.signs.SignManager;
import com.atlshearer.tournamentmanager.tournament.Tournament;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;
import com.atlshearer.tournamentmanager.utils.PlayerUtils;

public class TournamentManager extends JavaPlugin implements TournamentManagerAPI {
	private Command rootCommand;
	private Tournament tournament;
	
	private HashMap<String, Adaptor> adaptors;
	private Adaptor nextGame = null;
	
	private SignManager signManager;
	
	@Override
	public void onEnable() {
		adaptors = new HashMap<>();
		
		this.saveDefaultConfig();
		
		this.getLogger().info(this.getConfig().getString("data.address"));
		
		// Enable managers
		signManager = new SignManager(this);
		
		// Enable database
		DatabaseUtils.onEnable(this);
		PlayerUtils.onEnable(this);
		
		// Enable command handling
		rootCommand = new RootCommand();
		
		getCommand("tournamentmanager").setExecutor(rootCommand);
		getCommand("tournamentmanager").setTabCompleter(rootCommand);
		
		// Register Events
		getServer().getPluginManager().registerEvents(new LogoutEvent(this), this);
		getServer().getPluginManager().registerEvents(new SignListener(this), this);
		getServer().getPluginManager().registerEvents(new AdaptorListener(this), this);
		
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
		getSignManager().updateSigns();
	}
	
	public Adaptor getNextGame() {
		return nextGame;
	}
	
	public SignManager getSignManager() {
		return signManager;
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
		if (isTournamentEnabled()) {
			getLogger().info(() -> String.format("Giving player %s %d points as requested by %s", 
					player.getName(), points, adaptor.getAdaptorName()));
			
			getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
				@Override
				public void run() {
					try {
						DatabaseUtils.PlayerUtils.addToPlayerScore(getCurrentTournament(), player.getUniqueId().toString(), points);

					} catch (SQLException e) {
						getServer().broadcast(ChatColor.DARK_RED + "An SQL error occured. Please check logs.", "tournamentmanager.admin");
						e.printStackTrace();
					}					
				}
			});
			
			getServer().broadcastMessage("Giving player " + player.getDisplayName() + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + " " + points + " Points");
		} else {
			getServer().broadcast(ChatColor.RED + "No tournament is enable. No scores being given.", "tournamentmanager.admin");
		}
	}

	@Override
	public void gameEnded(Adaptor adaptor) {
		getLogger().info(() -> String.format("Adaptor '%s' reports game end", 
				adaptor.getAdaptorName()));		
	}
}
