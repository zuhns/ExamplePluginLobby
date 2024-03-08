package org.qubit.events;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.qubit.menager.*;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;



public class PlayerChat_e implements Listener
{
	// HashMap per tracciare i cooldown dei giocatori
    private HashMap<UUID, Long> cooldowns = new HashMap<>();
    
    private PartyManager partyManager;
    
    public PlayerChat_e(PartyManager partyManager) 
    {
        this.partyManager = partyManager;
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) 
    {
    	String message = event.getMessage();
        Player player = event.getPlayer();
        Party party = partyManager.getParty(player.getUniqueId());
        
        if(party != null && partyManager.isPartyChatEnabled(player))
        {
        	event.setCancelled(true);
        	for(Player member : party.getMembers())
        	{
        		System.out.println("Messaggio in party chat: " + message);
        		member.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Party " + ChatColor.BLUE + "" + ChatColor.BOLD + player.getDisplayName() + ChatColor.WHITE + " >> " + ChatColor.AQUA + message);	
        	}
        }
        else
        {
        	UUID playerUUID = player.getUniqueId();
        	
        	for (Player p : Bukkit.getOnlinePlayers()) 
        	{
        		if (message.contains("@" + p.getName())) 
        		{
        			message = message.replaceAll("@" + p.getName(), ChatColor.GOLD + "@" + p.getName() + ChatColor.RESET);
        		}
        	}
        	if(!player.isOp())
        	{
        		// Controllo se il giocatore è nel cooldown
        		if (cooldowns.containsKey(playerUUID)) 
        		{
        			long timeElapsed = System.currentTimeMillis() - cooldowns.get(playerUUID);
        			if (timeElapsed < 5000) 
        			{ // Cooldown di 5 secondi
        				event.setCancelled(true);
        				player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Devi aspettare " + (5 - timeElapsed / 1000) + " secondi prima di poter inviare un altro messaggio.");
        				return;
        			}
        		}
        	}
        	
        	// Se non è nel cooldown, aggiorna l'ultimo messaggio inviato
        	cooldowns.put(playerUUID, System.currentTimeMillis());
        	
        	// Ottieni messaggio e stampa alla console
        	System.out.println("Messaggio in chat: " + message);
        	
        	LuckPerms api = LuckPermsProvider.get();
    	    UserManager userManager = api.getUserManager();
    	    User user = userManager.getUser(player.getUniqueId());

    	    if (user != null) 
    	    {
    	        CachedMetaData metaData = user.getCachedData().getMetaData();
    	        String prefix = metaData.getPrefix();

    	        if (prefix != null) {
    	            prefix = ChatColor.translateAlternateColorCodes('&', prefix);
    	        } else {
    	            prefix = "";
    	        }
    	        // Imposta il messaggio finale
    	        event.setFormat(prefix + ChatColor.WHITE + "" + ChatColor.BOLD + player.getDisplayName() + ChatColor.WHITE + " >> " + ChatColor.GRAY + message);
    	    }
        }
    }
}
