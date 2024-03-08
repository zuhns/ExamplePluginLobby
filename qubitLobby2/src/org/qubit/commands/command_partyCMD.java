package org.qubit.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.qubit.menager.*;

public class command_partyCMD implements CommandExecutor 
{
    private PartyManager partyManager;

    public command_partyCMD(PartyManager partyManager) 
    {
        this.partyManager = partyManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
    {
        if (!(sender instanceof Player)) 
        {
            sender.sendMessage(ChatColor.RED + "Solo i giocatori possono usare questo comando.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) 
        {
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "/party " + ChatColor.GRAY + "" + ChatColor.UNDERLINE + "Lista commandi:\n" + " ");
            player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "/party info: " + ChatColor.GRAY + "Visualizza i membri del party\n" + ChatColor.AQUA + "" + ChatColor.BOLD + "/party invite <player>: " + ChatColor.GRAY + "Invita i membri del clan");
            player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "/party create: " + ChatColor.GRAY + "Crea un party\n" + ChatColor.AQUA + "" + ChatColor.BOLD + "/party disband: " + ChatColor.GRAY + "Sciogli un party\n" + ChatColor.AQUA + "" + ChatColor.BOLD + "/party remove or kick <player>: " + ChatColor.GRAY + "Rimuovi un membro dal party");
            player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "/party chat: " + ChatColor.GRAY + "Attiva/disattiva la chat party\n" + ChatColor.AQUA + "" + ChatColor.BOLD + "/party leave: " + ChatColor.GRAY + "Esci da un party\n" + ChatColor.AQUA + "" + ChatColor.BOLD + "/party accept: " + ChatColor.GRAY + "Accetti l'invito in un party\n");
            return true;
        }

        switch (args[0].toLowerCase()) 
        {
            case "create":
                createParty(player);
                break;
            case "invite":
                inviteToParty(player, args);
                break;
            case "remove":
                removeFromParty(player, args);
                break;
            case "kick":
                removeFromParty(player, args);
                break;
            case "disband":
            	disbandParty(player);
            	break;
            case "info":
            	getInfoParty(player);
            	break;
            case "chat":
            	partyChat(player);
            	break;
            case "leave":
            	quitParty(player);
            	break;
            case "accept":
            	acceptPartyInvite(player);
            	break;
            	
            // Aggiungi qui altri casi (info, chat, promuve, disband)
            default:
                player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.RED + "" + ChatColor.BOLD + "Argomento non valido.");
        }
        return true;
    }
    
    private void createParty(Player player) 
    {
    	Party currentParty = partyManager.getParty(player.getUniqueId());
        if (currentParty != null) 
        {
            // Controlla se il giocatore è già in un party
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.RED + "" + ChatColor.BOLD + "Non puoi creare un nuovo party finché sei membro di un altro.");
            return;
        }
        // Crea il party e aggiungi il giocatore come leader
        partyManager.createParty(player);
        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Party creato con successo!");
    }
    
    private void inviteToParty(Player player, String[] args) 
    {
    	if (args.length < 2) //nome non inserito
    	{
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> " + ChatColor.RED + "" + ChatColor.BOLD + "Devi specificare un giocatore da invitare.");
            return;
        }
    	Player target = Bukkit.getPlayerExact(args[1]);
		
		if (target == null) //giocatore non trovato
		{
			player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.RED + "" + ChatColor.BOLD + "Nessun player trovato!");
			return;
		}
		if (target == player) //autoinvito
		{
			player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.RED + "" + ChatColor.BOLD + "Non puoi invitare te stesso.");
			return;
		}
		
		Party party = partyManager.getParty(player.getUniqueId());
		Party targetparty = partyManager.getParty(target.getUniqueId());
		
		if(targetparty != null)
		{
			player.sendMessage(ChatColor.RED + "" +  ChatColor.BOLD + "Il player " + ChatColor.GOLD + target.getName() + ChatColor.RED + "" +  ChatColor.BOLD + " si trova già in un party");
			return;
		}
		
		if (party == null) //non esiste nessun party 
		{
			partyManager.createParty(player);
			party = partyManager.getParty(player.getUniqueId());
		}
		
		
		player.sendMessage(ChatColor.GREEN + "" +  ChatColor.BOLD + "Hai invitato " + ChatColor.GOLD + target.getName() + ChatColor.GREEN + "" +  ChatColor.BOLD + " nel party!");
		target.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.DARK_AQUA + "" +  ChatColor.BOLD + " ti ha invitato al party");
		partyManager.partyInviter(player, target);
    }
    
    public void acceptPartyInvite(Player player)
	{
    	// Controlla se il giocatore ha inviti pendenti
        if (!partyManager.hasPendingInvites(player)) 
        {
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.RED + "" + ChatColor.BOLD + "Non hai inviti per unirti a un party.");
            return;
        }

        // Prova ad accettare l'invito
        if (partyManager.acceptParty(player)) 
        {
            player.sendMessage(ChatColor.GREEN +  "" + ChatColor.BOLD + "Hai accettato con successo l'invito al party!");
        } 
        else 
        {
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Non è stato possibile accettare l'invito al party.");
        }
    }
    
    private void removeFromParty(Player player, String[] args) 
    {
        // Implementazione del sottocomando remove
        if (args.length < 2) 
        {
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.RED + "" + ChatColor.BOLD + "Devi specificare un giocatore da rimuovere.");
            return;
        }
        
        
        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) //controlla se c'è il giocatore
        {
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.RED + "" + ChatColor.BOLD + "Giocatore non trovato.");
            return;
        }
        Party party = partyManager.getParty(player.getUniqueId());
        if (party == null) //controlla se esiste il party
        {
        	player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.RED + "" + ChatColor.BOLD + "Non esiste nessun party!");
        	return;
        }
        
        if(!party.getLeader().equals(player))//controlla se è il leader del party
        {
        	player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.RED + "" + ChatColor.BOLD + "Non hai il permesso di espellere dal party");
            return;
        }
        if (!party.getMembers().contains(target)) //controlla se il giocatore è nel party
        {
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.RED + "" + ChatColor.BOLD + "Il giocatore non è nel tuo party.");
            return;
        }
        partyManager.removeMember(target);
        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Giocatore rimosso dal party.");
        for (Player member : party.getMembers()) 
        {
    		member.sendMessage(ChatColor.GOLD + target.getName() + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " è stato espulso dal party");    		
        }
        target.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Sei stato espulso dal party!");
    }
    
    private void disbandParty(Player player)
    {
    	Party party = partyManager.getParty(player.getUniqueId());
    	if(party == null)//non esiste nessun party 
    	{
    		player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.RED + "" + ChatColor.BOLD + "Non esiste nessun party!");
    		return;
    	}
    	
    	Player leader = party.getLeader();
    	if(!leader.equals(player))//se il sender non è il leader
    	{
    		player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.RED + "" + ChatColor.BOLD + "Non sei il leader del party!");
    		return;
    	}
    	
    	//#PartyManager controllo funzione
    	partyManager.disbandParty(player);
    }
    
    private void getInfoParty(Player player)
    {
    	Party party = partyManager.getParty(player.getUniqueId());
    	if(party == null)//non esiste nessun party 
    	{
    		player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.RED + "" + ChatColor.BOLD + "Non esiste nessun party!");
    		return;
    	}
    	
    	StringBuilder membersList = new StringBuilder();
        for (Player member : party.getMembers()) 
        {
            if (membersList.length() > 0) {
                membersList.append(", ");
            }
            membersList.append(member.getName());
        }
        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Informazioni del Party:\n" + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Leader: " + ChatColor.GOLD + party.getLeader().getName() + "\n" + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Members: " + ChatColor.GOLD + membersList.toString());
    }
    
    private void partyChat(Player player)
    {
    	Party party = partyManager.getParty(player.getUniqueId());
    	if(party == null)//non esiste nessun party 
    	{
    		player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.RED + "" + ChatColor.BOLD + "Non esiste nessun party!");
    		return;
    	}
    	
    	partyManager.togglePartyChat(player);
        if (partyManager.isPartyChatEnabled(player)) 
        {
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Chat di party attivata.");
        } 
        else 
        {
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Chat di party disattivata.");
        }
    }
    
    private void quitParty(Player player)
    {
    	Party party = partyManager.getParty(player.getUniqueId());
    	if(party == null)//non esiste nessun party 
    	{
    		player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.RED + "" + ChatColor.BOLD + "Non esiste nessun party!");
    		return;
    	}
    	
    	Player target = player;
    	if(target.equals(party.getLeader()))
		{
    		disbandParty(target);
    		return;
		}
    	
    	partyManager.removeMember(target);
    	for (Player member : party.getMembers()) 
        {
    		member.sendMessage(ChatColor.GOLD + target.getName() + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " è uscito dal party");    		
        }
        target.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Sei uscito dal party!");
    	
    }
}

