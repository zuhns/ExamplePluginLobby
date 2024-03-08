package org.qubit.events;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem_e implements Listener
{
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event)
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
