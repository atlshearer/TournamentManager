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
	
	public void updateAllSigns() {
		updateJoinSigns();
		updateTeamSigns();
	}
	
	public void updateJoinSigns() {
		plugin.getLogger().info("Updating join signs.");
		
		HashSet<SignBase> invalidSigns = new HashSet<>();
		
		for (SignBase sign : signs) {
			if (!(sign.location.getBlock().getState() instanceof Sign)) {
				plugin.getServer().broadcast(ChatColor.RED + "Sign removed", "tournamentmanager.admin");
				invalidSigns.add(sign);
				continue;
			}
			
			if (!(sign instanceof SignJoinNext)) {
				continue;
			}
			
			sign.updateSign();
		}
		
		signs.removeAll(invalidSigns);
	}
	
	public void updateTeamSigns() {
		plugin.getLogger().info("Updating teams signs.");
		
		HashSet<SignBase> invalidSigns = new HashSet<>();
		
		for (SignBase sign : signs) {
			if (!(sign.location.getBlock().getState() instanceof Sign)) {
				plugin.getServer().broadcast(ChatColor.RED + "Sign removed", "tournamentmanager.admin");
				invalidSigns.add(sign);
				continue;
			}
			
			if (sign instanceof SignTeam) {
				sign.updateSign();
			}			
		}
		
		signs.removeAll(invalidSigns);
	}
	
	public void updateTeamSign(String teamName) {
		plugin.getLogger().info("Updating team signs.");
		
		HashSet<SignBase> invalidSigns = new HashSet<>();
		
		for (SignBase sign : signs) {
			if (!(sign.location.getBlock().getState() instanceof Sign)) {
				plugin.getServer().broadcast(ChatColor.RED + "Sign removed", "tournamentmanager.admin");
				invalidSigns.add(sign);
				continue;
			}
			
			if (sign instanceof SignTeam) {
				SignTeam teamSign = (SignTeam) sign;
				
				if (teamSign.isForTeam(teamName)) {
					sign.updateSign();					
				}
			}		
		}
		
		signs.removeAll(invalidSigns);
	}
	
	public void removeSign(SignBase sign) {
		signs.remove(sign);
	}
}
