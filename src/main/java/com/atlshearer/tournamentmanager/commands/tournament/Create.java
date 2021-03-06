package com.atlshearer.tournamentmanager.commands.tournament;

import java.sql.SQLException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;

public class Create extends Command {

	public Create(Command parent) {
		super(parent);
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		if (args.length != 0) {
			sender.sendMessage(ChatColor.RED + getHelp());
			return;
		}
		
		if (pargs.get(0).length() > 16) {
			sender.sendMessage(ChatColor.RED + "Name cannot be longer than 16 characters.");
			return;
		}

		try {
			if (DatabaseUtils.TournamentUtils.getTournamentByName(pargs.get(0)) != null) {
				sender.sendMessage(ChatColor.RED + String.format("Tournament with the name '%s' already exists.", pargs.get(0)));
				return;
			}
			
			DatabaseUtils.TournamentUtils.createTournament(pargs.get(0));
			sender.sendMessage(ChatColor.GREEN + "Succesfully added tournament to db.");
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}	
	}

	@Override
	public String getName() {
		return "create";
	}
	
	@Override
	public String getHelp() {
		return "Usage - /tm tournament <tournamen_name> create";
	}

	@Override
	protected String getBasePermission() {
		return "create";
	}

}
