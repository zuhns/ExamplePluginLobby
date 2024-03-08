package org.qubit.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Item_navigator implements Listener
{
	public static ItemStack createNavigator()
	{
		 ItemStack navigator = new ItemStack(Material.COMPASS, 1);
		 ItemMeta navigatorMeta = navigator.getItemMeta();
		 navigatorMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Navigator");
		 List<String> lore = new ArrayList<>();
		 lore.add("Select Play Mode");
		 navigatorMeta.setLore(lore);
		 navigator.setItemMeta(navigatorMeta);
		 return navigator;
	}
	
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) 
    {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) 
		{
			Player p = event.getPlayer();
			ItemStack itemInHand = p.getInventory().getItemInMainHand(); 
			
			if (itemInHand != null && itemInHand.isSimilar(createNavigator())) 
			{
                Inventory navigatorgui = Bukkit.createInventory(p, 45, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Navigator");
                
                //Item bed selector
                ItemStack solo = new ItemStack(Material.RED_BED);
                ItemStack duo = new ItemStack(Material.RED_BED);
                ItemStack trio = new ItemStack(Material.RED_BED);
                ItemStack squad = new ItemStack(Material.RED_BED);
                
                
                ItemMeta solo_meta = solo.getItemMeta();
                solo_meta.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Solo" + ChatColor.WHITE + "" + ChatColor.BOLD + " Bedwars");
                ArrayList<String>solo_lore = new ArrayList<>();
                solo_lore.add(ChatColor.GOLD + "" + ChatColor.BOLD+ "Press to play Solo Bedwars");
                solo_lore.add(ChatColor.DARK_RED + "recommended version: 1.8.9");
                solo_meta.setLore(solo_lore);
                solo.setItemMeta(solo_meta);
                
                ItemMeta duo_meta = solo.getItemMeta();
                duo_meta.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Duo" + ChatColor.WHITE + "" + ChatColor.BOLD + " Bedwars");
                ArrayList<String>duo_lore = new ArrayList<>();
                duo_lore.add(ChatColor.GOLD + "" + ChatColor.BOLD+ "Press to play Duo Bedwars");
                duo_lore.add(ChatColor.DARK_RED + "recommended version: 1.8.9");
                duo_meta.setLore(duo_lore);
                duo.setItemMeta(duo_meta);
                
                ItemMeta trio_meta = solo.getItemMeta();
                trio_meta.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Trio" + ChatColor.WHITE + "" + ChatColor.BOLD + " Bedwars");
                ArrayList<String>trio_lore = new ArrayList<>();
                trio_lore.add(ChatColor.GOLD + "" + ChatColor.BOLD+ "Press to play Trio Bedwars");
                trio_lore.add(ChatColor.DARK_RED + "recommended version: 1.8.9");
                trio_meta.setLore(trio_lore);
                trio.setItemMeta(trio_meta);
                
                
                ItemMeta squad_meta = solo.getItemMeta();
                squad_meta.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Squad" + ChatColor.WHITE + "" + ChatColor.BOLD + " Bedwars");
                ArrayList<String>squad_lore = new ArrayList<>();
                squad_lore.add(ChatColor.GOLD + "" + ChatColor.BOLD+ "Press to play Squad Bedwars");
                squad_lore.add(ChatColor.DARK_RED + "recommended version: 1.8.9");
                squad_meta.setLore(squad_lore);
                squad.setItemMeta(squad_meta);
                
                //posizione inventario
                navigatorgui.setItem(11, solo);
                navigatorgui.setItem(12, duo);
                navigatorgui.setItem(14, trio);
                navigatorgui.setItem(15, squad);

                p.openInventory(navigatorgui);
            }
		}
    }
	
}
