package org.qubit.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class command_setparkour implements CommandExecutor
{
	private static Location parkourLocation;
	
	private JavaPlugin plugin;

    public command_setparkour(JavaPlugin plugin) 
    {
        this.plugin = plugin;
    }

    public Location getParkourLocation() 
    {
        return parkourLocation;
    }
    
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg3)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			if(p.isOp())
			{
				parkourLocation = p.getLocation();
				plugin.getConfig().set("parkour.world", parkourLocation.getWorld().getName());
				plugin.getConfig().set("parkour.x", parkourLocation.getX());
				plugin.getConfig().set("parkour.y", parkourLocation.getY());
				plugin.getConfig().set("parkour.z", parkourLocation.getZ());
				plugin.getConfig().set("parkour.yaw", 180);
				plugin.getConfig().set("parkour.pitch", 0);
				plugin.saveConfig();
				
				p.sendMessage(ChatColor.AQUA + "Posizione del parkour impostata!");
				
			}
			else
			{
				p.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> " + ChatColor.RED + "Non hai il permesso di farlo");
			}
		}
		return true;
	}
}
