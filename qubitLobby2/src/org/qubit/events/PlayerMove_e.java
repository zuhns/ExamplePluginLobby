package org.qubit.events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerMove_e implements Listener
{
	private final JavaPlugin plugin;
	private final int minHeight; // L'altezza minima prima di teletrasportare il giocatore
	private static Location safeLocation; // La posizione sicura dove teletrasportare il giocatore

    public PlayerMove_e(JavaPlugin plugin, int minHeight) 
    {
        this.plugin = plugin;
        this.minHeight = minHeight;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) 
    {
        Player player = event.getPlayer();
        if (player.getLocation().getY() < minHeight) 
        {
        	try 
            {
            	World world = plugin.getServer().getWorld(plugin.getConfig().getString("spawn.world"));
                double x = plugin.getConfig().getDouble("spawn.x");
                double y = plugin.getConfig().getDouble("spawn.y");
                double z = plugin.getConfig().getDouble("spawn.z");
                float yaw = (float) plugin.getConfig().getDouble("spawn.yaw");
                float pitch = (float) plugin.getConfig().getDouble("spawn.pitch");

                safeLocation = new Location(world, x, y, z, yaw, pitch);
                player.teleport(safeLocation);
            } 
            catch (Exception e) 
            {
                player.sendMessage(ChatColor.RED + "Spawn non disponibile");
                e.printStackTrace(); // This line logs the exception to the console
                return;
            }
        }
    }
}
