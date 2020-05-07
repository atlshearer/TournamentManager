package com.atlshearer.tournamentmanager.commands.tournament;

import java.sql.SQLException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.tournament.Tournament;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;

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
		
		try {
			Tournament tournament = DatabaseUtils.getTournamentByName(pargs.get(0));
			
			if (tournament == null) {
				sender.sendMessage(ChatColor.RED + String.format("No tournament by the name %s was found", pargs.get(0)));
				return;
			}
			
			plugin.setCurrentTournament(tournament);
			sender.sendMessage(ChatColor.GREEN + "Current tournament has been set to " + pargs.get(0));
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}

	}

	@Override
	public String getName() {
		return "enable";
	}

	@Override
	public String getHelp() {
		return "Usage - /tm tournament <tournament_name> enable";
	}
	
	@Override
	protected String getBasePermission() {
		return "enable";
	}

}
