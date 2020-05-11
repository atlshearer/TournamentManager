package com.atlshearer.tournamentmanager.signs;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;

import com.atlshearer.tournamentmanager.TournamentManager;

public class SignManager {

	private final TournamentManager plugin;
	private Set<SignBase> signs;
	
	public SignManager(TournamentManager plugin) {
		this.plugin = plugin;
		this.signs = new HashSet<>();
	}
	
	public boolean addSign(SignBase sign) {
		return signs.add(sign);
	}
	
	public void updateSigns() {
		plugin.getLogger().info("Updating signs.");
		
		HashSet<SignBase> invalidSigns = new HashSet<>();
		
		for (SignBase sign : signs) {
			if (!(sign.location.getBlock().getState() instanceof Sign)) {
				plugin.getServer().broadcast(ChatColor.RED + "Sign removed", "tournamentmanager.admin");
				invalidSigns.add(sign);
				continue;
			}
			
			sign.updateSign();
		}
		
		signs.removeAll(invalidSigns);
	}
}
