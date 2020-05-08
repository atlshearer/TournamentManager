package com.atlshearer.tournamentmanager.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.api.Adaptor;
import com.atlshearer.tournamentmanager.api.Adaptor.STATE;

public class JoinNext extends Command {
	
	public JoinNext(Command parent) {
		super(parent);
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		if (args.length != 0) {
			sender.sendMessage(ChatColor.RED + getHelp());
			return;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command can only be run by players.");
			return;
		}
		
		Player player = (Player) sender;
		
		Adaptor adaptor = plugin.getNextGame();
		if (adaptor == null || adaptor.getState() != STATE.ENABLED) {
			sender.sendMessage(ChatColor.RED + "Next game is not currently enabled.");
			return;
		}
		
		sender.sendMessage(ChatColor.GREEN + "Joining game.");
		adaptor.playerJoin(player);
	}

	@Override
	public String getName() {
		return "joinnext";
	}

	@Override
	public String getHelp() {
		return "Usage - /tm joinnext";
	}
	
	@Override
	protected String getBasePermission() {
		return "joinnext";
	}

}
