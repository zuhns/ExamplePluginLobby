package org.qubit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLvlChange_e implements Listener
{
	@EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) 
	{
        if (event.getEntity() instanceof Player) 
        {
            event.setFoodLevel(20); // Imposta il livello di fame al massimo
        }
    }
}
