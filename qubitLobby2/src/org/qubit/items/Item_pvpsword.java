package org.qubit.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Item_pvpsword implements Listener
{
	ItemStack sword = createPvpSword();
	
	private Map<UUID, Boolean> playerPvPStatus = new HashMap<>();
	
	public static ItemStack createPvpSword()
	{
		 ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
		 ItemMeta swordMeta = sword.getItemMeta();
		 swordMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "PVP Sword");
		 List<String> lore = new ArrayList<>();
		 lore.add("Go fight");
		 lore.add("with other player");
		 swordMeta.setLore(lore);
		 swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
		 swordMeta.setUnbreakable(true);
		 sword.setItemMeta(swordMeta);
		 return sword;
	}
	
	private boolean getPlayerPvPStatus(Player player) 
	{
	    return playerPvPStatus.getOrDefault(player.getUniqueId(), false);
	}
	
	@EventHandler
    public void onEntityDamage(EntityDamageEvent event) 
	{
		if (event.getEntity() instanceof Player) 
		{
			Player damagedPlayer = (Player) event.getEntity();
			if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) 
			{
	            event.setCancelled(true); // Termina il metodo qui
	        }
			
			// Controllo se l'attaccante è un giocatore
	        if (event instanceof EntityDamageByEntityEvent) 
	        {
	            EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event;
	            if (entityEvent.getDamager() instanceof Player) 
	            {
	                Player attackingPlayer = (Player) entityEvent.getDamager();
	             // Ottieni lo stato PvP dei giocatori (dovrai implementare il metodo getPlayerPvPStatus)
	                boolean damagedPlayerPvPStatus = getPlayerPvPStatus(damagedPlayer);
	                boolean attackingPlayerPvPStatus = getPlayerPvPStatus(attackingPlayer);

	                // Controlla lo stato PvP di entrambi i giocatori
	                if (damagedPlayerPvPStatus && attackingPlayerPvPStatus || damagedPlayerPvPStatus && !attackingPlayerPvPStatus || !damagedPlayerPvPStatus && attackingPlayerPvPStatus) 
	                {
	                    event.setCancelled(true);
	                    damagedPlayer.updateInventory();
	                    attackingPlayer.updateInventory();
	                } 
	                
	                else 
	                {
	                    event.setCancelled(false);
	                    damagedPlayer.updateInventory();
	                    attackingPlayer.updateInventory();
	                }
	            }
	            else
	            {
	            	event.setCancelled(true);
	            }
	        }

		}
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		if (event.getEntity() instanceof Player) 
		{
			Player player = event.getEntity();
			playerPvPStatus.put(player.getUniqueId(), true);
			player.getInventory().setHeldItemSlot(4);
			player.getInventory().setArmorContents(null);
			player.updateInventory();
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player p = event.getPlayer();
		playerPvPStatus.put(p.getUniqueId(), true);
		p.getInventory().setArmorContents(null);
		p.updateInventory();
	}
	
	@EventHandler
	public void onItemHeld(PlayerItemHeldEvent event)
	{
		Player p = event.getPlayer();
		ItemStack itemInNewSlot = p.getInventory().getItem(event.getNewSlot());
		ItemStack itemInPreviousSlot = p.getInventory().getItem(event.getPreviousSlot());
		
		// Logica per l'ingresso nel PvP
		if(itemInNewSlot != null && itemInNewSlot.isSimilar(sword)) 
		{	
			//damage true
			playerPvPStatus.put(p.getUniqueId(), false);
			p.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
			p.getInventory().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
			p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
			p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Sei entrato in pvp!");	    }
		else if(itemInPreviousSlot != null && itemInPreviousSlot.isSimilar(sword))
		{
			playerPvPStatus.put(p.getUniqueId(), true);
			p.setHealth(20);
			p.getInventory().setArmorContents(null);
			p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Sei uscito dal pvp!");
		}
	}
}
