package com.atlshearer.tournamentmanager.signs;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.atlshearer.tournamentmanager.TournamentManager;

public class SignListener implements Listener {
	
	private final TournamentManager plugin;
	
	public SignListener(TournamentManager plugin) {
		this.plugin = plugin;
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSignCreate(SignChangeEvent e) {
		Player player = e.getPlayer();
		if (ChatColor.stripColor(e.getLine(0)).equalsIgnoreCase("[TM]")) {  
			if (!player.hasPermission("tournamentmanager.admin")) {
				player.sendMessage(ChatColor.RED + "You do not have required permission.");
				e.setCancelled(true);
				e.getBlock().breakNaturally();
				return;
			}	
			
			String line = e.getLine(1).toLowerCase();

			if (line.equalsIgnoreCase("[joinnext]")) {
				player.sendMessage(ChatColor.GREEN + "'Created' joinnext sign.");

				e.setLine(0, 
						ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.prefix")));

				plugin.getSignManager().addSign(new SignJoinNext(plugin, e.getBlock().getLocation()));
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
						new Runnable() {
							@Override
							public void run() {
								plugin.getSignManager().updateSigns();
							}
						}
					);
			}
		}
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSignClick(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		
		if (!(e.getClickedBlock().getState() instanceof Sign)) {
			return;
		}
		
		
		Sign sign = (Sign) e.getClickedBlock().getState();
		
		String prefixline = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.prefix"));
		
		if (sign.getLine(0).equalsIgnoreCase(prefixline)) {
			if (sign.getLine(1).equalsIgnoreCase("[joinnext]")) {
				e.getPlayer().sendMessage(ChatColor.GREEN + "Join next sign click.");
				plugin.getServer().dispatchCommand(e.getPlayer(), "tournamentmanager joinnext");
			}
		}
	}
}
