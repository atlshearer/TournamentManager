package com.atlshearer.tournamentmanager.commands.adaptor;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.api.Adaptor;
import com.atlshearer.tournamentmanager.commands.Command;

public class SetAsNext extends Command {

	public SetAsNext(Command parent) {
		super(parent);
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		if (args.length != 0) {
			sender.sendMessage(ChatColor.RED + getHelp());
			return;
		}
		
		Adaptor adaptor = plugin.getAdaptors().get (pargs.get(0)); 
		if (adaptor == null) {
			sender.sendMessage(ChatColor.RED + "Failed to find adaptor for the given name.");
			return;
		}
		
		plugin.setNextGame(adaptor);
		sender.sendMessage(ChatColor.GREEN + "Next game updated.");
	}

	@Override
	public String getName() {
		return "setasnext";
	}

	@Override
	public String getHelp() {
		return "Usage - /tm adaptor <adaptor_name> setasnext";
	}
	
	@Override
	protected String getBasePermission() {
		return "setasnext";
	}

}
