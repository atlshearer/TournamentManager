package com.atlshearer.tournamentmanager.commands.tournament;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.atlshearer.tournamentmanager.commands.Command;
import com.atlshearer.tournamentmanager.tournament.Tournament;
import com.atlshearer.tournamentmanager.utils.DatabaseUtils;

public class ListTournament extends Command {

	public ListTournament(Command parent) {
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
			List<Tournament> tournaments = DatabaseUtils.getTournaments();
			
			if (tournaments.isEmpty()) {
				sender.sendMessage(ChatColor.RED + "Warning:" + ChatColor.RESET + " No tournaments found....");
				return;
			} 
			
			sender.sendMessage(ChatColor.BOLD + "Tournament Name" + ChatColor.ITALIC + " (Tournament ID)");
			for (Tournament tournament : tournaments) {
				sender.sendMessage(String.format("%s (%d)", tournament.name, tournament.id));
			}
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "listtournaments";
	}

	@Override
	public String getHelp() {
		return "Usage - /tm listteams";
	}
	
	@Override
	protected String getBasePermission() {
		return "tournament.list";
	}

}
