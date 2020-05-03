package com.atlshearer.tournamentmanager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.atlshearer.tournamentmanager.tournament.Team;
import com.atlshearer.tournamentmanager.tournament.Tournament;

import pro.husk.mysql.MySQL;

public class DatabaseUtils {
	
	private static TournamentManager plugin;
	public static MySQL database;
	
	public static void onEnable(TournamentManager plugin) {
		DatabaseUtils.plugin = plugin;
		
		DatabaseUtils.database = new MySQL(
				DatabaseUtils.plugin.getConfig().getString("data.address"), 
				DatabaseUtils.plugin.getConfig().getString("data.port"), 
				DatabaseUtils.plugin.getConfig().getString("data.database"), 
				DatabaseUtils.plugin.getConfig().getString("data.username"), 
				DatabaseUtils.plugin.getConfig().getString("data.password"), 
				"useSSL=false");
		try {
			String prefix = DatabaseUtils.plugin.getConfig().getString("data.table_prefix");
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
			DatabaseUtils.plugin.getLogger().warning("Was not able to access database. See stack trace.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets all teams stored in database
	 * 
	 * @param database
	 * @return ArrayList<Team> of all teams
	 * @throws SQLException 
	 */
	public static ArrayList<Team> getTeams() throws SQLException {
		String prefix = DatabaseUtils.plugin.getConfig().getString("data.table_prefix");
			
		String requestStr = String.format(
				"SELECT %1$steam.id, %1$steam.name FROM %1$steam;", 
				prefix);
		
		ArrayList<Team> teams = new ArrayList<Team>();
		
		DatabaseUtils.plugin.database.query(requestStr, results -> {
			if (results != null) {
				while(results.next()) {
					teams.add(new Team(results.getInt("id"), results.getString("name")));
				}
			}
		});
		
		return teams;
	}
	
	/**
	 * Gets teams in a specific tournament
	 * 
	 * @param tournamentID
	 * @return ArrayList<Team> of teams in tournament
	 * @throws SQLException
	 */
	public static ArrayList<Team> getTeamsInTournament(int tournamentID) throws SQLException {
		String prefix = DatabaseUtils.plugin.getConfig().getString("data.table_prefix");
		
		String requestStr = String.format(
				"SELECT %1$steam.id, %1$steam.name FROM %1$steam " +
				"JOIN %1$stournament_team ON %1$stournament_team.team_id = %1$steam.id " +
				"WHERE %1$stournament_team.tournament_id = %2$d", 
				prefix,
				tournamentID);
		
		ArrayList<Team> teams = new ArrayList<Team>();
		
		DatabaseUtils.plugin.database.query(requestStr, results -> {
			if (results != null) {
				while(results.next()) {
					teams.add(new Team(results.getInt("id"), results.getString("name")));
				}
			}
		});
		
		return teams;
	}
	
	public static Team getTeamByID(int teamID) throws SQLException {
		String prefix = DatabaseUtils.plugin.getConfig().getString("data.table_prefix");
		
		String requestStr = String.format(
				"SELECT %1$steam.id, %1$steam.name FROM %1$steam " +
				"WHERE %1$steam.id = %2$d", 
				prefix,
				teamID);
		
		Team team = null;
		
		ResultSet results = DatabaseUtils.plugin.database.query(requestStr);
		if (results != null) {
			team = new Team(results.getInt("id"), results.getString("name"));
		}
		
		results.close();
		
		return team;
	}
	
	public static Tournament getTournamentByID(int tournamentID) throws SQLException {
		String prefix = DatabaseUtils.plugin.getConfig().getString("data.table_prefix");
		
		String requestStr = String.format(
				"SELECT %1$stournament.id, %1$stournament.name FROM %1$stournament " +
				"WHERE %1$stournament.id = %2$d", 
				prefix,
				tournamentID);
		
		Tournament tournament = null;
		
		ResultSet results = DatabaseUtils.plugin.database.query(requestStr);
		
		if (results.next()) {
			tournament = new Tournament(results.getInt("id"), results.getString("name"));
		}
		
		results.getStatement().close();
		
		return tournament;
	}
}
