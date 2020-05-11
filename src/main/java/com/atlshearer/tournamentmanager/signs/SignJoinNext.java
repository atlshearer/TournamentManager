package com.atlshearer.tournamentmanager.signs;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;

import com.atlshearer.tournamentmanager.TournamentManager;
import com.atlshearer.tournamentmanager.api.Adaptor.STATE;

public class SignJoinNext extends SignBase {

	public SignJoinNext(TournamentManager plugin, Location location) {
		super(plugin, location);
	}

	@Override
	public void updateSign() {
		if (location.getBlock().getState() instanceof Sign) {
			Sign signBlock = (Sign) location.getBlock().getState();
			if (plugin.getNextGame() != null && plugin.getNextGame().getState() == STATE.ENABLED) {
				signBlock.setLine(2, 
						ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.ready")));
			} else {
				signBlock.setLine(2, 
						ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.waiting")));
			}
			signBlock.update();
		}
		// TODO throw something?
	}

}
