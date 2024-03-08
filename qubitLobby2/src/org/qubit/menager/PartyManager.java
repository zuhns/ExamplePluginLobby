package org.qubit.menager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class PartyManager 
{
    private HashMap<UUID, Party> partyMap = new HashMap<>();
    private HashSet<UUID> partyChatEnabled = new HashSet<>();
    
    private HashMap<UUID, UUID> lastPartyInvite = new HashMap<>();

    // Metodo per creare un nuovo party
    public void createParty(Player leader) 
    {
        Party party = new Party(leader);
        partyMap.put(leader.getUniqueId(), party);
    }

    // Metodo per aggiungere un membro a un party
    public void addMember(Player leader, Player member) 
    {
        Party party = partyMap.get(leader.getUniqueId());
        if (party != null) 
        {
            party.addMember(member);
            partyMap.put(member.getUniqueId(), party);
        }
    }
    
    public void partyInviter(Player inviter, Player invitee)
    {
    	lastPartyInvite.put(invitee.getUniqueId(), inviter.getUniqueId());
    }
    
    public boolean acceptParty(Player member)
    {
    	UUID inviterId = lastPartyInvite.remove(member.getUniqueId());

        if (inviterId != null) 
        {
            Party party = partyMap.get(inviterId);
            if (party != null) 
            {
                for (Player p : party.getMembers()) 
            	{
                    p.sendMessage(ChatColor.GOLD + member.getName() + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " è entrato nel party!");
                }
                party.addMember(member);
                partyMap.put(member.getUniqueId(), party);
                return true; // Invito accettato con successo
            }
        }

        return false; // Non è stato possibile accettare l'invito
    }
    
    // Metodo per verificare se un giocatore ha inviti pendenti
    public boolean hasPendingInvites(Player player) 
    {
        return lastPartyInvite.containsKey(player.getUniqueId());
    }
    
    public void removeMember(Player member)
    {
    	Party party = partyMap.get(member.getUniqueId());
        if (party != null)
        {
        	party.removeMember(member);
        	partyMap.remove(member.getUniqueId());
        }
        
        if (member.equals(party.getLeader())) 
        {
            if (!party.getMembers().isEmpty()) //ci sono altri player nel party
            {
                // Assegna un nuovo leader dal set dei membri rimanenti
                Player newLeader = party.getMembers().iterator().next();
                party.setLeader(newLeader);
                partyMap.put(newLeader.getUniqueId(), party);
                // Notifica i membri del party del cambio di leadership
                for (Player p : party.getMembers()) 
                {
                    p.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.GOLD + newLeader.getName() + ChatColor.GREEN + "" + ChatColor.BOLD + " è ora il leader del party.");
                }
            } 
            else 
            {
                // Sciogli il party se non ci sono altri membri
                partyMap.remove(party.getLeader().getUniqueId());
            }
        }
    }
    
    //sciogli il party
    public void disbandParty(Player member)
    {
    	Party party = partyMap.get(member.getUniqueId());
    	// Notifica tutti i membri che il party è stato sciolto
    	for (Player p : party.getMembers()) 
    	{
    		partyMap.remove(party.getLeader().getUniqueId());
            p.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.DARK_AQUA + "" +ChatColor.BOLD + "Il party è stato sciolto.");
            //sciogli party
            partyMap.remove(p.getUniqueId());
        }
    }
    
    public void togglePartyChat(Player player) 
    {
        if (partyChatEnabled.contains(player.getUniqueId())) 
        {
            partyChatEnabled.remove(player.getUniqueId());
        } 
        else 
        {
            partyChatEnabled.add(player.getUniqueId());
        }
    }

    public boolean isPartyChatEnabled(Player player) 
    {
        return partyChatEnabled.contains(player.getUniqueId());
    }

    // Metodo per ottenere il party di un giocatore
    public Party getParty(UUID playerId) 
    {
        return partyMap.get(playerId);
    }
}