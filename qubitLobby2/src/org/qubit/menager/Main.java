package org.qubit.menager;

import org.bukkit.plugin.java.JavaPlugin;
import org.qubit.events.*;
import org.qubit.items.*;

import org.qubit.commands.*;

public class Main extends JavaPlugin
{	
	public void onEnable()
	{
		//config file
		getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		this.getConfig().set("MinCoord", 0);
		this.saveConfig();
		
		//events for lobby
		getServer().getPluginManager().registerEvents(new BlockDamage_e(), this);
		getServer().getPluginManager().registerEvents(new BlockPlace_e(), this);
		getServer().getPluginManager().registerEvents(new FoodLvlChange_e(), this);
		getServer().getPluginManager().registerEvents(new InventoryClick_e(), this);
		getServer().getPluginManager().registerEvents(new PlayerJoin_e(), this);
		getServer().getPluginManager().registerEvents(new PlayerDead_e(this), this);
		getServer().getPluginManager().registerEvents(new PlayerDropItem_e(), this);
		getServer().getPluginManager().registerEvents(new PlayerItemDamage_e(this), this);
		getServer().getPluginManager().registerEvents(new WeatherChange_e(), this);
		getServer().getPluginManager().registerEvents(new PlayerMove_e(this, getConfig().getInt("MinCoord")), this);
		
		//events for item
		getServer().getPluginManager().registerEvents(new Item_pvpsword(), this);
		getServer().getPluginManager().registerEvents(new Item_parkourladder(this), this);
		getServer().getPluginManager().registerEvents(new Item_navigator(), this);
		
		//Assegnazione nuova istanza
		PartyManager partyManager = new PartyManager();
		
		getServer().getPluginManager().registerEvents(new PlayerChat_e(partyManager), this);
		getServer().getPluginManager().registerEvents(new PlayerQuit_e(partyManager), this);
		this.getCommand("party").setExecutor(new command_partyCMD(partyManager));
		
		//gamemode 0 - 1 - 2
	    this.getCommand("gmc").setExecutor(new command_gmMode());
	    this.getCommand("gms").setExecutor(new command_gmMode());
	    this.getCommand("gmsp").setExecutor(new command_gmMode());
	    this.getCommand("setparkour").setExecutor(new command_setparkour(this));
	    this.getCommand("setspawn").setExecutor(new command_setspawn(this));
	    this.getCommand("spawn").setExecutor(new command_spawn(this));
	    this.getCommand("spawn").setExecutor(new command_spawn(this));
	    
	}
	
	public void onDisable()
	{
		System.out.println("Plugin QubitLobby disabilitato!");
	}
}
