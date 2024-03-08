package org.qubit.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Item_parkourladder implements Listener
{
	private static Location parkourLocation;
	
	private final JavaPlugin plugin;

	public Item_parkourladder(JavaPlugin plugin) 
	{
		this.plugin = plugin;
	}
	
    public static ItemStack createParkour()
	{
		 ItemStack ladder = new ItemStack(Material.LADDER, 1);
		 ItemMeta ladderMeta = ladder.getItemMeta();
		 ladderMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Parkour");
		 List<String> lore = new ArrayList<>();
		 lore.add("Go to Parkour");
		 ladderMeta.setLore(lore);
		 ladder.setItemMeta(ladderMeta);
		 return ladder;
	}
    
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) 
    {
		Player p = event.getPlayer();
		ItemStack itemInHand = p.getInventory().getItemInMainHand(); 
		
		if (itemInHand != null && itemInHand.isSimilar(createParkour())) 
		{
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) 
			{
	            try 
	            {
	            	World world = plugin.getServer().getWorld(plugin.getConfig().getString("parkour.world"));
	                double x = plugin.getConfig().getDouble("parkour.x");
	                double y = plugin.getConfig().getDouble("parkour.y");
	                double z = plugin.getConfig().getDouble("parkour.z");
	                float yaw = (float) plugin.getConfig().getDouble("parkour.yaw");
	                float pitch = (float) plugin.getConfig().getDouble("parkour.pitch");
	
	                parkourLocation = new Location(world, x, y, z, yaw, pitch);
	                p.teleport(parkourLocation);
	                return;
	            } 
	            catch (Exception e) 
	            {
	                p.sendMessage(ChatColor.RED + "Parkour non disponibile");
	                e.printStackTrace(); // This line logs the exception to the console
	                return;
	            }
			}
        }
    }
}
