package com.atlshearer.tournamentmanager.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.tournament.SimplePlayer;
import com.atlshearer.tournamentmanager.tournament.Team;
import com.atlshearer.tournamentmanager.tournament.Tournament;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import pro.husk.mysql.MySQL;

public class DatabaseUtils {
	
	private static TournamentManager plugin;
	private static MySQL database;
	private static String tablePrefix;
	private static boolean debugOutput;
	
	private DatabaseUtils() {}
	
	public static void onEnable(TournamentManager plugin) {
		DatabaseUtils.plugin = plugin;
		
		// TODO flag...
		DatabaseUtils.debugOutput = true;
		
		DatabaseUtils.database = new MySQL(
				DatabaseUtils.plugin.getConfig().getString("data.address"), 
				DatabaseUtils.plugin.getConfig().getString("data.port"), 
				DatabaseUtils.plugin.getConfig().getString("data.database"), 
				DatabaseUtils.plugin.getConfig().getString("data.username"), 
				DatabaseUtils.plugin.getConfig().getString("data.password"), 
				"useSSL=false");
		try {
			tablePrefix = DatabaseUtils.plugin.getConfig().getString("data.table_prefix");

			update("CREATE TABLE IF NOT EXISTS " + tablePrefix + "player ("+
							"uuid VARCHAR(36) NOT NULL," + 
							"username VARCHAR(16) NOT NULL," + 
							"PRIMARY KEY (uuid));");
			
			update("CREATE TABLE IF NOT EXISTS " + tablePrefix + "team (" +
							"id INT NOT NULL AUTO_INCREMENT," + 
							"name VARCHAR(16) NOT NULL," + 
							"PRIMARY KEY (id));");
			
			update("CREATE TABLE IF NOT EXISTS " + tablePrefix + "team_member (" +
							"player_uuid VARCHAR(36) NOT NULL REFERENCES player(uuid)," +
							"team_id INT NOT NULL REFERENCES team(id)," +
							"CONSTRAINT team_member_pkey PRIMARY KEY (player_uuid, team_id)," +
							"FOREIGN KEY(player_uuid) REFERENCES " + tablePrefix + "player(uuid)," +
							"FOREIGN KEY(team_id)     REFERENCES " + tablePrefix + "team(id));");
			
			update("CREATE TABLE IF NOT EXISTS " + tablePrefix + "tournament (" + 
							"id INT NOT NULL AUTO_INCREMENT," + 
							"name VARCHAR(16) NOT NULL," + 
							"PRIMARY KEY(id));");
			
			update("CREATE TABLE IF NOT EXISTS " + tablePrefix + "score (" + 
							"tournament_id INT NOT NULL," + 
							"player_uuid VARCHAR(36) NOT NULL," + 
							"score INT NOT NULL," + 
							"CONSTRAINT score_pkey PRIMARY KEY (player_uuid, tournament_id)," + 
							"FOREIGN KEY(player_uuid)   REFERENCES " + tablePrefix + "player(uuid)," + 
							"FOREIGN KEY(tournament_id) REFERENCES " + tablePrefix + "tournament(id));");
			
			update("CREATE TABLE IF NOT EXISTS " + tablePrefix + "tournament_team (" + 
							"tournament_id INT NOT NULL," + 
							"team_id INT NOT NULL," + 
							"CONSTRAINT tournament_team_pkey PRIMARY KEY (tournament_id, team_id)," +
							"FOREIGN KEY(tournament_id) REFERENCES " + tablePrefix + "tournament(id)," +
							"FOREIGN KEY(team_id)       REFERENCES " + tablePrefix + "team(id));");
			
			update(String.format(
					"CREATE OR REPLACE VIEW %1$steam_score AS " + 
					"SELECT %1$sscore.tournament_id, %1$steam.id AS team_id, %1$steam.name AS team_name, SUM(%1$sscore.score) AS score FROM %1$sscore " + 
					"JOIN %1$steam_member ON %1$steam_member.player_uuid = %1$sscore.player_uuid " + 
					"JOIN %1$steam ON %1$steam.id = %1$steam_member.team_id " + 
					"JOIN %1$stournament_team ON %1$stournament_team.team_id = %1$steam.id " + 
					"WHERE %1$stournament_team.tournament_id = %1$sscore.tournament_id " + 
					"GROUP BY %1$steam.id, %1$sscore.tournament_id", tablePrefix));
			
		} catch (SQLException e) {
			DatabaseUtils.plugin.getLogger().warning("Was not able to access database. See stack trace.");
			e.printStackTrace();
		}
	}

	public static void onDisable() {
		try {
			database.closeConnection();
		} catch (SQLException e) {
			DatabaseUtils.plugin.getLogger().warning("An error occured while trying to close the database connection.");
			e.printStackTrace();
		}
	}
	
	
	private static int update(String update) throws SQLException {
		if (DatabaseUtils.debugOutput) {
			DatabaseUtils.plugin.getLogger().info(update);
		}
		return DatabaseUtils.database.update(update);
	}
	
	private static ResultSet query(String query) throws SQLException {
		if (DatabaseUtils.debugOutput) {
			DatabaseUtils.plugin.getLogger().info(query);
		}
		return DatabaseUtils.database.query(query);
	}
	
	
	// Static class for all player associated database calls
	public static class PlayerUtils {
		
		private PlayerUtils() {}
		
		
		/**
		 * Adds player to database
		 * 
		 * @param player
		 * @throws SQLException
		 */
		public static void addPlayer(Player player) throws SQLException {
			String requestStr = String.format(
					"INSERT INTO %1$splayer (uuid, username) VALUE ('%2$s', '%3$s');", 
					tablePrefix,
					player.getUniqueId(),
					player.getName());
			
			update(requestStr);
		}
		
		/**
		 * Sets the score of player for given tournament
		 * 
		 * @param tournament
		 * @param uuid
		 * @param score
		 * @throws SQLException
		 */
		public static void setPlayerScore(Tournament tournament, String uuid, int score) throws SQLException {
			String requestStr;
			
			if (doesPlayerScoreExist(tournament, uuid)) {
				requestStr = String.format(
						"UPDATE %1$sscore " + 
								"SET %1$sscore.score = %4$d " + 
								"WHERE %1$sscore.tournament_id = %3$d AND %1$sscore.player_uuid = '%2$s'",
								tablePrefix,
								uuid,
								tournament.id,
								score);
			} else {
				requestStr = String.format(
						"INSERT INTO %1$sscore (player_uuid, tournament_id, score) " +
								"VALUES ('%2$s', %3$d, %4$d);",
								tablePrefix,
								uuid,
								tournament.id,
								score);
			}
			
			update(requestStr);
		}
		
		/**
		 * Adds the given score to the players total
		 * 
		 * @param tournament
		 * @param uuid
		 * @param score
		 * @throws SQLException
		 */
		public static void addToPlayerScore(Tournament tournament, String uuid, int score) throws SQLException {
			String requestStr = String.format(
					"UPDATE %1$sscore " + 
					"SET %1$sscore.score = %1$sscore.score + %4$d " + 
					"WHERE %1$sscore.tournament_id = %2$d " + 
					"AND %1$sscore.player_uuid = '%3$s'", 
					tablePrefix,
					tournament.id,
					uuid,
					score);
			
			update(requestStr);
		}
		
		/**
		 * Gets the score of the player in the given tournament
		 * 
		 * @param tournament
		 * @param uuid
		 * @return player score or 0 if no score found
		 * @throws SQLException
		 */
		public static int getPlayerScore(Tournament tournament, String uuid) throws SQLException {
			String requestStr = String.format(
					"SELECT %1$sscore.tournament_id, %1$sscore.player_uuid, %1$sscore.score FROM %1$sscore " +
					"WHERE %1$sscore.tournament_id = %2$d " +
					"AND %1$sscore.player_uuid = '%3$s';", 
					tablePrefix,
					tournament.id,
					uuid);		
			
			ResultSet results = query(requestStr);
			
			int score = 0;
			
			if (results.next()) {
				score = results.getInt("score");
			}
			
			return score;
		}
		
		/**
		 * Checks if the player has a score in the given tournament
		 * 
		 * @param tournament
		 * @param uuid
		 * @return true iff a score record exists
		 * @throws SQLException
		 */
		public static boolean doesPlayerScoreExist(Tournament tournament, String uuid) throws SQLException {
			String requestStr = String.format(
					"SELECT EXISTS(" + 
					"SELECT 1 FROM %1$sscore " + 
					"WHERE %1$sscore.tournament_id = %2$d " + 
					"AND %1$sscore.player_uuid = '%3$s');", 
					tablePrefix,
					tournament.id,
					uuid);		
			
			ResultSet results = query(requestStr);
			
			boolean exists = false;
			
			if (results.next()) {
				exists = results.getInt(1) == 1;
			}
			
			return exists;
		}
		
		/**
		 * Gets the data stored for a player from database
		 * 
		 * @param name
		 * @return SimplePlayer with data about player or null
		 * @throws SQLException
		 */
		public static SimplePlayer getPlayerByName(String name) throws SQLException {
			String requestStr = String.format(
					"SELECT %1$splayer.uuid, %1$splayer.username FROM %1$splayer " +
					"WHERE %1$splayer.username = '%2$s';", 
					tablePrefix,
					name);
			
			SimplePlayer player = null;
			
			ResultSet results = query(requestStr);
			
			if (results.next()) {
				player = new SimplePlayer(results.getString("uuid"), results.getString("username"));
			}
			
			results.getStatement().close();
			
			return player;
		}
		
		/**
		 * Checks if the given player is in any team
		 * 
		 * @param uuid
		 * @return Team - A team that the player is in or null if they are in no team
		 * @throws SQLException
		 */
		public static Team isPlayerInAnyTeam(String uuid) throws SQLException {
			String requestStr = String.format(
					"SELECT %1$steam.id, %1$steam.name FROM %1$steam " + 
					"JOIN %1$steam_member ON %1$steam_member.team_id = %1$steam.id " + 
					"WHERE %1$steam_member.player_uuid = '%2$s'", 
					tablePrefix,
					uuid);
			
			Team team = null;
			
			ResultSet results = query(requestStr);
			
			if (results.next()) {
				team = new Team(results.getInt("id"), results.getString("name"));
			}
			
			results.getStatement().close();
			
			return team;
		}
		
		/**
		 * Checks if the given player is in the given team
		 * 
		 * @param uuid String
		 * @param team Team
		 * @return Team - A team that the player is in or null if they are in no team
		 * @throws SQLException
		 */
		public static Team isPlayerInTeam(String uuid, String teamName) throws SQLException {	
			String requestStr = String.format(
					"SELECT %1$steam.id, %1$steam.name FROM %1$steam " + 
					"JOIN %1$steam_member ON %1$steam_member.team_id = %1$steam.id " + 
					"WHERE %1$steam_member.player_uuid = '%2$s' " +
					"AND %1$steam.name = '%3$s';", 
					tablePrefix,
					uuid,
					teamName);
			
			Team team = null;
			
			ResultSet results = query(requestStr);
			
			if (results.next()) {
				team = new Team(results.getInt("id"), results.getString("name"));
			}
			
			results.getStatement().close();
			
			return team;
		}
		
		private static CacheLoader<String, List<SimplePlayer>> playersLoader = new CacheLoader<String, List<SimplePlayer>>() {
			@Override
			public List<SimplePlayer> load(String requestStr) {		
				ArrayList<SimplePlayer> players = new ArrayList<>();
				
				try {
					ResultSet results = query(requestStr);
					
					while (results.next()) {
						players.add(new SimplePlayer(results.getString("uuid"), results.getString("username")));
					}
					
					results.getStatement().close();
				} catch (SQLException e) {
					plugin.getServer().broadcast(ChatColor.DARK_RED + "An SQL error occured. Please check logs.", "tournamentmanager.admin");
					e.printStackTrace();
				}
				
				return players;
			}
		};
		
		private static LoadingCache<String, List<SimplePlayer>> playersCache = CacheBuilder.newBuilder()
				.expireAfterWrite(5, TimeUnit.SECONDS)
				.build(playersLoader);
		
		/**
		 * Gets all players in database
		 * 
		 * @param name
		 * @return SimplePlayer with data about player or null
		 * @throws SQLException
		 */
		public static List<SimplePlayer> getPlayers() {
			String requestStr = String.format(
					"SELECT %1$splayer.uuid, %1$splayer.username FROM %1$splayer", 
					tablePrefix);
			
			return playersCache.getUnchecked(requestStr);
		}
		
		/**
		 * Gets all players in database with score
		 * 
		 * @param name
		 * @return ArrayList<SimplePlayer> of players ordered by score DESC
		 * @throws SQLException
		 */
		public static List<SimplePlayer> getPlayers(Tournament tournament) throws SQLException {
			String requestStr = String.format(
					"SELECT %1$splayer.uuid, %1$splayer.username, %1$sscore.score FROM %1$splayer " + 
					"JOIN %1$sscore ON %1$sscore.player_uuid = %1$splayer.uuid " +
					"WHERE %1$sscore.tournament_id = %2$d " +
					"ORDER BY %1$sscore.score DESC", 
					tablePrefix,
					tournament.id);		
			
			ArrayList<SimplePlayer> players = new ArrayList<>();
			
			ResultSet results = query(requestStr);
			
			while (results.next()) {
				players.add(new SimplePlayer(results.getString("uuid"), results.getString("username"), results.getInt("score")));
			}
			
			results.getStatement().close();
			
			return players;
		}
		
		public static List<SimplePlayer> getPlayersInTeam(int teamID) throws SQLException {
			String requestStr = String.format(
					"SELECT %1$splayer.username, %1$splayer.uuid FROM %1$splayer " + 
					"JOIN %1$steam_member ON %1$steam_member.player_uuid = %1$splayer.uuid " + 
					"WHERE %1$steam_member.team_id = %2$d",  
					tablePrefix,
					teamID);		
			
			ResultSet results = query(requestStr);
			
			ArrayList<SimplePlayer> players = new ArrayList<>();
			
			while (results.next()) {
				players.add(new SimplePlayer(results.getString("uuid"), results.getString("username")));
			}
			
			return players;
		}
		
		public static List<SimplePlayer> getPlayersInTeam(int teamID, Tournament tournament) throws SQLException {		
			String requestStr = String.format(
					"SELECT %1$splayer.uuid, %1$splayer.username, %1$sscore.score FROM %1$splayer " + 
					"JOIN %1$sscore ON %1$sscore.player_uuid = %1$splayer.uuid " + 
					"JOIN %1$steam_member ON %1$steam_member.player_uuid = %1$splayer.uuid " + 
					"WHERE %1$sscore.tournament_id = %2$d " + 
					"AND %1$steam_member.team_id = %3$d",  
					tablePrefix,
					tournament.id,
					teamID);
			
			ResultSet results = query(requestStr);
			
			ArrayList<SimplePlayer> players = new ArrayList<>();
			
			while (results.next()) {
				players.add(new SimplePlayer(results.getString("uuid"), results.getString("username"), results.getInt("score")));
			}
			
			return players;
		}
	}
	
	public static class TeamUtils {

		private TeamUtils() {}
		
		public static void createTeam(String name) throws SQLException {
			String requestStr = String.format(
					"INSERT INTO %1$steam (name) VALUE ('%2$s');", 
					tablePrefix,
					name);

			update(requestStr);
		}

		public static void addPlayerToTeam(SimplePlayer player, Team team) throws SQLException {
			String requestStr = String.format(
					"INSERT INTO %1$steam_member (player_uuid, team_id) VALUE ('%2$s', %3$s);", 
					tablePrefix,
					player.uuid,
					team.id);

			update(requestStr);
		}

		public static void removePlayerFromTeam(SimplePlayer player, Team team) throws SQLException {
			String requestStr = String.format(
					"DELETE FROM %1$steam_member " + 
							"WHERE %1$steam_member.player_uuid = '%2$s' " + 
							"AND %1$steam_member.team_id = %3$d;", 
							tablePrefix,
							player.uuid,
							team.id);

			update(requestStr);
		}
		
		public static Team getTeamByID(int teamID) throws SQLException {
			String requestStr = String.format(
					"SELECT %1$steam.id, %1$steam.name FROM %1$steam " +
							"WHERE %1$steam.id = %2$d", 
							tablePrefix,
							teamID);

			Team team = null;

			ResultSet results = query(requestStr);
			if (results.next()) {
				team = new Team(results.getInt("id"), results.getString("name"));
			}

			results.close();

			return team;
		}

		/**
		 * Gets the team of the given name from database
		 * 
		 * @param teamName
		 * @return Team if a team was found or null
		 * @throws SQLException
		 */
		public static Team getTeamByName(String teamName) throws SQLException {
			String requestStr = String.format(
					"SELECT %1$steam.id, %1$steam.name FROM %1$steam " +
							"WHERE %1$steam.name = '%2$s'", 
							tablePrefix,
							teamName);

			Team team = null;

			ResultSet results = query(requestStr);
			if (results.next()) {
				team = new Team(results.getInt("id"), results.getString("name"));
			}

			results.close();

			return team;
		}

		/**
		 * Gets the team of the given name from database
		 * 
		 * @param teamName
		 * @param tournament
		 * @return Team if a team was found or null
		 * @throws SQLException
		 */
		public static Team getTeamByName(String teamName, Tournament tournament) throws SQLException {
			String requestStr = String.format(
					"SELECT %1$steam_score.team_id, %1$steam_score.team_name, %1$steam_score.score FROM %1$steam_score " +
							"WHERE %1$steam_score.team_name = '%2$s' " +
							"AND %1$steam_score.tournament_id = %3$d;", 
							tablePrefix,
							teamName,
							tournament.id);

			Team team = null;

			ResultSet results = query(requestStr);
			if (results.next()) {
				team = new Team(results.getInt("team_id"), results.getString("team_name"), results.getInt("score"));
			}

			results.close();

			return team;
		}
		
		// List<Team> loading	
		private static CacheLoader<String, List<Team>> teamsLoader = new CacheLoader<String, List<Team>>() {
			@Override
			public List<Team> load(String requestStr) {		
				ArrayList<Team> teams = new ArrayList<>();

				ResultSet results;
				try {
					results = query(requestStr);

					while(results.next()) {
						teams.add(new Team(results.getInt("id"), results.getString("name")));
					}
				} catch (SQLException e) {
					plugin.getServer().broadcast(ChatColor.DARK_RED + "An SQL error occured. Please check logs.", "tournamentmanager.admin");
					e.printStackTrace();
				}

				return teams;
			}
		};

		private static LoadingCache<String, List<Team>> teamsCahce = CacheBuilder.newBuilder()
				.expireAfterWrite(5, TimeUnit.SECONDS)
				.build(teamsLoader);

		/**
		 * Gets all teams stored in database excluding score
		 * 
		 * @param database
		 * @return ArrayList<Team> of all teams
		 * @throws SQLException 
		 */
		public static List<Team> getTeams() {
			String requestStr = String.format(
					"SELECT %1$steam.id, %1$steam.name FROM %1$steam;", 
					tablePrefix);

			return teamsCahce.getUnchecked(requestStr);
		}

		/**
		 * Gets teams in a specific tournament excluding score
		 * 
		 * @param tournamentID
		 * @return ArrayList<Team> of teams in tournament
		 * @throws SQLException
		 */
		public static List<Team> getTeams(Tournament tournament){		
			String requestStr = String.format(
					"SELECT %1$steam.id, %1$steam.name FROM %1$steam " +
							"JOIN %1$stournament_team ON %1$stournament_team.team_id = %1$steam.id " +
							"WHERE %1$stournament_team.tournament_id = %2$d", 
							tablePrefix,
							tournament.id);

			return teamsCahce.getUnchecked(requestStr);
		}

		/**
		 * Gets teams in a specific tournament including score
		 * 
		 * @param tournament
		 * @return
		 * @throws SQLException
		 */
		public static List<Team> getTeamScores(Tournament tournament) throws SQLException {
			String requestStr = String.format(
					"SELECT team_id, team_name, score FROM %1$steam_score " +
							"WHERE %1$steam_score.tournament_id = %2$d " +
							"ORDER BY score DESC;", 
							tablePrefix,
							tournament.id);		

			ResultSet results = query(requestStr);

			ArrayList<Team> teams = new ArrayList<>();

			while (results.next()) {
				teams.add(new Team(results.getInt("team_id"), results.getString("team_name"), results.getInt("score")));
			}

			return teams; 
		}
	}
	
	
	public static class TournamentUtils {

		private TournamentUtils() {}


		public static void createTournament(String name) throws SQLException {
			String requestStr = String.format(
					"INSERT INTO %1$stournament (name) VALUE ('%2$s');", 
					tablePrefix,
					name);

			update(requestStr);
		}

		
		public static void addTeamToTournament(int tournamentID, int teamID) throws SQLException {
			String requestStr = String.format(
					"INSERT INTO %1$stournament_team (team_id, tournament_id) VALUE (%2$d, %3$d);", 
					tablePrefix,
					teamID,
					tournamentID);

			update(requestStr);

			// Find all players in team
		}

		
		public static void addTeamToTournament(Tournament tournament, Team team) throws SQLException {
			addTeamToTournament(tournament.id, team.id);
		}
		
		
		public static boolean isTeamInTournament(Tournament tournament, Team team) throws SQLException {
			String requestStr = String.format(
					"SELECT * FROM %1$stournament_team " + 
							"WHERE tournament_id = %2$d " + 
							"AND team_id = %3$d", 
							tablePrefix,
							tournament.id,
							team.id);

			ResultSet results = query(requestStr);

			Boolean inTournament = results.next();

			results.getStatement().close();

			return inTournament;
		}
		
		
		/**
		 * Searches the database for a tournament with the given name
		 * 
		 * @param tournamentName
		 * @return Tournament if a tournament is found or null
		 * @throws SQLException
		 */
		public static Tournament getTournamentByName(String tournamentName) throws SQLException {
			String requestStr = String.format(
					"SELECT %1$stournament.id, %1$stournament.name FROM %1$stournament " +
							"WHERE %1$stournament.name = '%2$s'", 
							tablePrefix,
							tournamentName);

			Tournament tournament = null;

			ResultSet results = query(requestStr);

			if (results.next()) {
				tournament = new Tournament(results.getInt("id"), results.getString("name"));
			}

			results.getStatement().close();

			return tournament;
		}

		/**
		 * Gets the specified tournament
		 * 
		 * @param tournamentID
		 * @return Returns specified tournament if found or null
		 * @throws SQLException
		 */
		public static Tournament getTournamentByID(int tournamentID) throws SQLException {
			String requestStr = String.format(
					"SELECT %1$stournament.id, %1$stournament.name FROM %1$stournament " +
							"WHERE %1$stournament.id = %2$d", 
							tablePrefix,
							tournamentID);

			Tournament tournament = null;

			ResultSet results = query(requestStr);

			if (results.next()) {
				tournament = new Tournament(results.getInt("id"), results.getString("name"));
			}

			results.getStatement().close();

			return tournament;
		}
		
		
		private static CacheLoader<String, List<Tournament>> tournamentsLoader = new CacheLoader<String, List<Tournament>>() {
			@Override
			public List<Tournament> load(String requestStr) {		
				ArrayList<Tournament> tournaments = new ArrayList<>();

				ResultSet results;
				try {
					results = query(requestStr);

					while (results.next()) {
						tournaments.add(new Tournament(results.getInt("id"), results.getString("name")));
					}

					results.getStatement().close();
				} catch (SQLException e) {
					plugin.getServer().broadcast(ChatColor.DARK_RED + "An SQL error occured. Please check logs.", "tournamentmanager.admin");
					e.printStackTrace();
				}

				return tournaments;
			}
		};

		private static LoadingCache<String, List<Tournament>> tournamentsCache = CacheBuilder.newBuilder()
				.expireAfterWrite(5, TimeUnit.SECONDS)
				.build(tournamentsLoader);

		/**
		 * Retrieves all the tournaments from the database
		 * 
		 * @return 
		 * @throws SQLException
		 */
		public static List<Tournament> getTournaments() {
			String requestStr = String.format(
					"SELECT %1$stournament.id, %1$stournament.name FROM %1$stournament",
					tablePrefix);

			return tournamentsCache.getUnchecked(requestStr);
		}

		
	}	
}