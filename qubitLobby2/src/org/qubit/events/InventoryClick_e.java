package org.qubit.events;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick_e implements Listener
{
	@EventHandler
	public void onClickInventory(InventoryClickEvent event)
	{
		Player p = (Player) event.getWhoClicked();
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
