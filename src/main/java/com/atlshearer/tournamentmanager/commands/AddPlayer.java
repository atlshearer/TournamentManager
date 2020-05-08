package com.atlshearer.tournamentmanager.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.atlshearer.tournamentmanager.utils.DatabaseUtils;
import com.atlshearer.tournamentmanager.utils.PlayerUtils;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class AddPlayer extends Command {
	
	public AddPlayer(Command parent) {
		super(parent);
	}
	
	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
			List<String> pargs) {
		if (args.length != 1) {
			sender.sendMessage(ChatColor.RED + getHelp());
			return;
		}
		
		Player target = Bukkit.getPlayer(args[0]);
		
		if (target == null) {
			sender.sendMessage(ChatColor.RED + "Player not found.");
			return;
		}
		
		try {
			DatabaseUtils.PlayerUtils.addPlayer(target);
			
			sender.sendMessage(ChatColor.GREEN + target.getName() + " added to database successfully.");
		} catch (MySQLIntegrityConstraintViolationException e) {
			sender.sendMessage(ChatColor.RED + "Player appears to already be in Database. Please check logs.");
			e.printStackTrace();
		}  catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED + "An SQL error occured. Please check logs.");
			e.printStackTrace();
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias,
			String[] args) {
		ArrayList<String> suggestions = new ArrayList<>();
		TreeSet<String> playerNames = new TreeSet<>();
		
		if (args == null || args.length == 0) {
			return new ArrayList<>();
		}
		
		playerNames = PlayerUtils.getPlayersNamesNotInDB();
		
		for (String name : playerNames) {
			if (name.toLowerCase().startsWith(args[0].toLowerCase())) {
				suggestions.add(name);
			}
		}
		
		return suggestions;
	}
	
	@Override
	public String getHelp() {
		return "Usage - /tm addplayer [player_name]";
	}

	@Override
	public String getName() {
		return "addplayer";
	}
	
	@Override
	public String getBasePermission() {
		return "addplayer";
	}
}
