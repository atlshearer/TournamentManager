package com.atlshearer.tournamentmanager.tournament;

public class Team {
	
	public final int id;
	public final String name;
	public final int score;
	
	public Team(int teamId, String teamName) {
		this.id = teamId;
		this.name = teamName;
		this.score = -1;
	}
	
	public Team(int teamId, String teamName, int teamScore) {
		this.id = teamId;
		this.name = teamName;
		this.score = teamScore;
	}
	
}
