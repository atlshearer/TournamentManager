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
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		final SignBase other = (SignBase) obj;
		
		if (location.getBlockX() != other.location.getBlockX()) {
			return false;
		}
		
		if (location.getBlockY() != other.location.getBlockY()) {
			return false;
		}
		
		if (location.getBlockZ() != other.location.getBlockZ()) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return location.getBlockX() ^ location.getBlockY() ^ location.getBlockZ();
	}
	
	public abstract void updateSign();
}
