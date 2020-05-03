package com.atlshearer.tournamentmanager;

import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import com.atlshearer.tournamentmanager.listeners.LogoutEvent;
import com.atlshearer.tournamentmanager.tournament.Tournament;

import pro.husk.mysql.MySQL;

public class TournamentManager extends JavaPlugin {
	public CommandHandler commandHandler;
	public Tournament tournament;
	public MySQL database;	
	
	@Override
	public void onEnable() {		
		this.saveDefaultConfig();
		
		this.getLogger().info(this.getConfig().getString("data.address"));
		
		// Enable database
		database = new MySQL(
				this.getConfig().getString("data.address"), 
				this.getConfig().getString("data.port"), 
				this.getConfig().getString("data.database"), 
				this.getConfig().getString("data.username"), 
				this.getConfig().getString("data.password"), 
				"useSSL=false");
		
		try {
			String prefix = this.getConfig().getString("data.table_prefix");
			database.update("CREATE TABLE IF NOT EXISTS " + prefix + "player ("+
							"uuid VARCHAR(36) NOT NULL," + 
							"username VARCHAR(16) NOT NULL," + 
							"PRIMARY KEY (uuid));");
			
			database.update("CREATE TABLE IF NOT EXISTS " + prefix + "team (" +
							"id INT NOT NULL AUTO_INCREMENT," + 
							"name VARCHAR(16) NOT NULL," + 
							"PRIMARY KEY (id));");
			
			database.update("CREATE TABLE IF NOT EXISTS " + prefix + "team_member (" +
							"player_uuid VARCHAR(36) NOT NULL REFERENCES player(uuid)," +
							"team_id INT NOT NULL REFERENCES team(id)," +
							"CONSTRAINT team_member_pkey PRIMARY KEY (player_uuid, team_id)," +
							"FOREIGN KEY(player_uuid) REFERENCES " + prefix + "player(uuid)," +
							"FOREIGN KEY(team_id)     REFERENCES " + prefix + "team(id));");
			
			database.update("CREATE TABLE IF NOT EXISTS " + prefix + "tournament (" + 
							"id INT NOT NULL AUTO_INCREMENT," + 
							"name VARCHAR(16) NOT NULL," + 
							"PRIMARY KEY(id));");
			
			database.update("CREATE TABLE IF NOT EXISTS " + prefix + "score (" + 
							"tournament_id INT NOT NULL," + 
							"player_uuid VARCHAR(36) NOT NULL," + 
							"score INT NOT NULL," + 
							"CONSTRAINT score_pkey PRIMARY KEY (player_uuid, tournament_id)," + 
							"FOREIGN KEY(player_uuid)   REFERENCES " + prefix + "player(uuid)," + 
							"FOREIGN KEY(tournament_id) REFERENCES " + prefix + "tournament(id));");
			
			database.update("CREATE TABLE IF NOT EXISTS " + prefix + "tournament_team (" + 
							"tournament_id INT NOT NULL," + 
							"team_id INT NOT NULL," + 
							"CONSTRAINT tournament_team_pkey PRIMARY KEY (tournament_id, team_id)," +
							"FOREIGN KEY(tournament_id) REFERENCES " + prefix + "tournament(id)," +
							"FOREIGN KEY(team_id)       REFERENCES " + prefix + "team(id));");
			
		} catch (SQLException e) {
			this.getLogger().warning("Was not able to access database. See stack trace.");
			e.printStackTrace();
		}
		
		// Enable command handling
		commandHandler = new CommandHandler(this);
		
		getCommand("tournamentmanager").setExecutor(commandHandler);
		getCommand("tournamentmanager").setTabCompleter(commandHandler);
		
		getServer().getPluginManager().registerEvents(new LogoutEvent(this), this);
		
		tournament = new Tournament(this, 8);
	}
	
	@Override
	public void onDisable() {
		try {
			database.closeConnection();
		} catch (SQLException e) {
			this.getLogger().warning("An error occured while trying to close the database connection.");
			e.printStackTrace();
		}
	}
	
	public void createTournament(String name) {
		this.getLogger().warning("Create tournament not impl.");
		this.getLogger().warning("Name req: " + name);
	}
}
