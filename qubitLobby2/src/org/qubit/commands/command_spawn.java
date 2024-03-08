package org.qubit.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class command_spawn implements CommandExecutor
{
	private Location spawnLocation;
	
	private JavaPlugin plugin;

    public command_spawn(JavaPlugin plugin) 
    {
        this.plugin = plugin;
    }
    
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg3)
	{
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			try 
            {
            	World world = plugin.getServer().getWorld(plugin.getConfig().getString("spawn.world"));
                double x = plugin.getConfig().getDouble("spawn.x");
                double y = plugin.getConfig().getDouble("spawn.y");
                double z = plugin.getConfig().getDouble("spawn.z");
                float yaw = (float) plugin.getConfig().getDouble("spawn.yaw");
                float pitch = (float) plugin.getConfig().getDouble("spawn.pitch");

                spawnLocation = new Location(world, x, y, z, yaw, pitch);
                p.teleport(spawnLocation);
            } 
            catch (Exception e) 
            {
                p.sendMessage(ChatColor.RED + "Spawn non disponibile");
                e.printStackTrace(); // This line logs the exception to the console
            }
		}
		else
		{
			sender.sendMessage("Non puoi eseguire questo commando");
		}
		return true;
	}
}
