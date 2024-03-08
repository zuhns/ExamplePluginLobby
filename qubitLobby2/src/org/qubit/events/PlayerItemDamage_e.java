package org.qubit.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerItemDamage_e implements Listener
{
	private JavaPlugin plugin;

    public PlayerItemDamage_e(JavaPlugin plugin) 
    {
        this.plugin = plugin;
    }
	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent event) 
	{
	    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> 
	    {
	    	event.setCancelled(true);
	    	ItemStack item = event.getItem();
	    	Player player = event.getPlayer();
	        if (item != null && item.getType() != Material.AIR) 
	        {
	            player.updateInventory();
	        }
	    }, 1L);
	}
}
