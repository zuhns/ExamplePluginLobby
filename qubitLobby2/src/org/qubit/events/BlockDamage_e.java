package org.qubit.events;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;


public class BlockDamage_e implements Listener
{
	@EventHandler
	public void onPlayerBlockDamage(BlockDamageEvent event)
	{
		Player p = event.getPlayer();

        if (p.getGameMode().equals(GameMode.CREATIVE))
        {
        	event.setCancelled(false);	
        }
        else
        {
        	event.setCancelled(true);	
        }	
	}
}
