package org.qubit.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChange_e implements Listener
{
	@EventHandler
	public void onWeatherChanger(WeatherChangeEvent event)
	{
		event.setCancelled(true);
	}
}
