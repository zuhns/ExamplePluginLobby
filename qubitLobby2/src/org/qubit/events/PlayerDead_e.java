package org.qubit.events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDead_e implements Listener
{
	private Location spawnLocation;
	
	private JavaPlugin plugin;

    public PlayerDead_e(JavaPlugin plugin) 
    {
        this.plugin = plugin;
    }
    
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		
		//keepInventory
		event.setKeepInventory(true);
		//esperienza
		event.setKeepLevel(true);
		event.setDroppedExp(0);
		
		//respawn immediato
		new BukkitRunnable() 
		{
            @Override
            public void run() 
            {
            	event.setKeepInventory(true);
                player.spigot().respawn(); // Questo è il metodo di Spigot per il respawn automatico
                // Puoi aggiungere altro codice qui se vuoi impostare la locazione del respawn, ecc.
                
                // Applicazione dell'effetto al giocatore
        		PotionEffect speedEffect = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1);
        		player.addPotionEffect(speedEffect);
        		
        		//torna allo spawn alla morte del player
                try 
                {
                	World world = plugin.getServer().getWorld(plugin.getConfig().getString("spawn.world"));
                	double x = plugin.getConfig().getDouble("spawn.x");
                	double y = plugin.getConfig().getDouble("spawn.y");
                	double z = plugin.getConfig().getDouble("spawn.z");
                	float yaw = (float) plugin.getConfig().getDouble("spawn.yaw");
                	float pitch = (float) plugin.getConfig().getDouble("spawn.pitch");
                	
                	spawnLocation = new Location(world, x, y, z, yaw, pitch);
                	player.teleport(spawnLocation);
                } 
                catch (Exception e) 
                {
                	player.sendMessage(ChatColor.RED + "Spawn non disponibile");
                	e.printStackTrace(); // This line logs the exception to the console
                }
                // Imposta lo slot iniziale al centro della hotbar (indice 4)
        		player.getInventory().setHeldItemSlot(4);
        		player.updateInventory();
            }
        }.runTaskLater(plugin, 1L);
        
	}
}
