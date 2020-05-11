package com.atlshearer.tournamentmanager.signs;

import org.bukkit.Location;

import com.atlshearer.tournamentmanager.TournamentManager;

public abstract class SignBase {

	protected final TournamentManager plugin;
	public final Location location;
	
	public SignBase(TournamentManager plugin, Location location) {
		this.plugin = plugin;
		this.location = location;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SignBase) {
			SignBase other = (SignBase) obj;
			return other.location == this.location;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return location.hashCode();
	}
	
	public abstract void updateSign();
}
