package org.qubit.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class command_gmMode implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg3)
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			if(player.isOp())
			{
				if(label.equalsIgnoreCase("gmc"))
				{
					if(player.getGameMode() != GameMode.CREATIVE)
					{
						player.setGameMode(GameMode.CREATIVE);
						player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Gamemode changed to creative");
					}
					else
					{
						player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "you are already creative");
					}
					
				}
				else if(label.equalsIgnoreCase("gms"))
				{
					if(player.getGameMode() != GameMode.SURVIVAL)
					{
						player.setGameMode(GameMode.SURVIVAL);
						player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Gamemode changed to survival");
					}
					else
					{
						player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "you are already survival");
					}
				}
				else if(label.equalsIgnoreCase("gmsp"))
				{
					if(player.getGameMode() != GameMode.SPECTATOR)
					{
						player.setGameMode(GameMode.SPECTATOR);
						player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Gamemode changed to spectator");
					}
					else
					{
						player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "you are already spectator");
					}
				}
			}
		}		
		else
		{
			sender.sendMessage("Commando non eseguibile dalla console");
		}			
		return true;
	}
}
