package com.atlshearer.tournamentmanager.commands.adaptor;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.api.Adaptor;
import com.atlshearer.tournamentmanager.commands.Command;

public class Enable extends Command {

	public Enable(Command parent) {
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
		
		adaptor.enable();
		sender.sendMessage(ChatColor.GREEN + "Enable request sent.");
	}

	@Override
	public String getName() {
		return "enable";
	}

	@Override
	public String getHelp() {
		return "Usage - /tm adaptor <adpator_name> enable";
	}
	
	@Override
	protected String getBasePermission() {
		return "enable";
	}

}
