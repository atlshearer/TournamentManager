package com.atlshearer.tournamentmanager.signs;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
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

			e.setLine(0, 
					ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.prefix")));
			
			if (line.equalsIgnoreCase("[joinnext]")) {
				plugin.getSignManager().addSign(new SignJoinNext(plugin, e.getBlock().getLocation()));

				
				player.sendMessage(ChatColor.GREEN + "Created joinnext sign.");
			} else if (line.equalsIgnoreCase("[team]")) {
				plugin.getSignManager().addSign(new SignTeam(plugin, e.getBlock().getLocation()));
				
				player.sendMessage(ChatColor.GREEN + "Created team sign.");
			}
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
					new Runnable() {
						@Override
						public void run() {
							plugin.getSignManager().updateAllSigns();
						}
					}
				);
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
				plugin.getServer().dispatchCommand(e.getPlayer(), "tournamentmanager joinnext");
			} else if (sign.getLine(1).equalsIgnoreCase("[team]")) {
				plugin.getServer().dispatchCommand(e.getPlayer(), "tournamentmanager team " + sign.getLine(2) + " join");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSignDestroy(BlockBreakEvent e) {
		if (!(e.getBlock().getState() instanceof Sign)) {
			return;
		}
		
		Player player = e.getPlayer();
		Sign sign = (Sign) e.getBlock().getState();
		
		if (sign.getLine(0).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.prefix")))) {
			if (!player.hasPermission("tournamentmanager.admin")) {
				player.sendMessage(ChatColor.RED + "You do not have required permission.");
				e.setCancelled(true);
				return;
			}
			
			String line = sign.getLine(1).toLowerCase();
			
			if (line.equalsIgnoreCase("[joinnext]")) {
				player.sendMessage(ChatColor.RED + "Sign removed");
				plugin.getSignManager().removeSign(new SignJoinNext(plugin, e.getBlock().getLocation()));
			} else if (line.equalsIgnoreCase("[team]")) {
				player.sendMessage(ChatColor.RED + "Sign removed");
				plugin.getSignManager().removeSign(new SignTeam(plugin, e.getBlock().getLocation()));
			}
		}
	}
}
