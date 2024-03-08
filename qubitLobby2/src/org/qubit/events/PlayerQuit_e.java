package org.qubit.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.qubit.menager.*;

public class PlayerQuit_e implements Listener 
{
	private PartyManager partyManager;

    public PlayerQuit_e(PartyManager partyManager) 
    {
        this.partyManager = partyManager;
    }
    
    @EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		System.out.println("PlayerQuit event triggered");
		Player player = event.getPlayer();
		Party party = partyManager.getParty(player.getUniqueId());
		if(party != null)
		{
			if (partyManager.isPartyChatEnabled(player))
			{
				partyManager.togglePartyChat(player);
			}
			for(Player member : party.getMembers())
			{
				member.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " è uscito dal gioco e dal party");
			}	
			partyManager.removeMember(player);
	    } 
		
		event.setQuitMessage("");
	}
}
