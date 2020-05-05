package com.atlshearer.tournamentmanager.commands.tournament;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.Command;

public class TournamentCreate extends Command {

	public TournamentCreate(Command parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		if (args.length != 0) {
			sender.sendMessage(ChatColor.GREEN + "Usage - /tm tournament <tournamen_name> create");
		} else {
			sender.sendMessage("Creating tournament with name: " + pargs.get(0));
		}
	}

	@Override
	public String getName() {
		return "create";
	}

	@Override
	protected String getBasePermission() {
		return "create";
	}

}
