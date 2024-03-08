package org.qubit.events;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace_e implements Listener
{
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Player p = event.getPlayer();
		if(!p.getGameMode().equals(GameMode.CREATIVE))
		{
			event.setCancelled(true);
		}
	}
}
