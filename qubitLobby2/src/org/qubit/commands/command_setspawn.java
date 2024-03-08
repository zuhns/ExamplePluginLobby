package org.qubit.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class command_setspawn implements CommandExecutor 
{
	private static Location spawnLocation;
	
	private JavaPlugin plugin;

    public command_setspawn(JavaPlugin plugin) 
    {
        this.plugin = plugin;
    }
    
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg3)
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			if(player.isOp())
			{
				spawnLocation = player.getLocation();
				plugin.getConfig().set("spawn.world", spawnLocation.getWorld().getName());
				plugin.getConfig().set("spawn.x", spawnLocation.getX());
				plugin.getConfig().set("spawn.y", spawnLocation.getY());
				plugin.getConfig().set("spawn.z", spawnLocation.getZ());
				plugin.getConfig().set("spawn.yaw", 180);
				plugin.getConfig().set("spawn.pitch", 0);
				plugin.saveConfig();
				
				player.sendMessage(ChatColor.AQUA + "Posizione dello spawn impostata!");
			}
		}
		else
		{
			sender.sendMessage("Non puoi eseguire questo commando");
		}
		return true;
	}
}
