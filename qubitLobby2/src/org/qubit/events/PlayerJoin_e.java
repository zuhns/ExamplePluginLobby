package org.qubit.events;

import java.lang.reflect.Constructor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import com.viaversion.viaversion.api.Via;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.qubit.items.*;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;

public class PlayerJoin_e implements Listener
{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		updateTabWithLuckPermsInfo(player);
		
		event.setJoinMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit " + ChatColor.GRAY + " >> "+ ChatColor.WHITE + player.getName() + " è entrato nel server!");
		
		// Applicazione dell'effetto al giocatore
		PotionEffect speedEffect = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1);
		player.addPotionEffect(speedEffect);
		
		player.setHealth(20);
		player.setFoodLevel(20);
		
		player.getInventory().clear(); //svuota l'inventario
		
        
        //pvp sword nella hotbar (indice 0)
		ItemStack sword = Item_pvpsword.createPvpSword();
		player.getInventory().setItem(0, sword);
		
		//parkour ladder nella hotbar (indice 8)
		ItemStack parkour = Item_parkourladder.createParkour();
		player.getInventory().setItem(8, parkour);
		
		ItemStack navigator = Item_navigator.createNavigator();
		player.getInventory().setItem(4, navigator);
		
		// Imposta lo slot iniziale al centro della hotbar (indice 4)
		player.getInventory().setHeldItemSlot(4);
		player.updateInventory();
		
		if(checkPlayerVersion(player))
		{
			// Qui mostri il titolo al giocatore
			showMyTitle(player);		
			player.sendMessage("versione superiore");
			
		}
		else
		{
			showMyTitleVP(player); 
			player.sendMessage("versione inferiore");
		}
	}
	
	public int getPlayerVersion(Player player) 
	{
	    return Via.getAPI().getPlayerVersion(player.getUniqueId());
	}
	
	public boolean checkPlayerVersion(Player player) 
	{
	    int playerVersion = getPlayerVersion(player);
	    
	    // Esempio: Protocol version 578 corrisponde a Minecraft 1.13
	    // Questi valori cambiano con ogni release di Minecraft
	    if (playerVersion < 393) 
	    {
	    	return false;
	        // Giocatore ha una versione precedente alla 1.13
	    } else {
	    	// Giocatore ha la versione 1.13 o successiva
	    	return true;
	    }
	}
	
	
	@EventHandler
    public void onUserDataRecalculate(UserDataRecalculateEvent event) 
	 {
		User user = event.getUser();
        // Ottieni il giocatore associato all'evento
        Player player = Bukkit.getPlayer(user.getUniqueId());

        if (player != null && player.isOnline()) 
        {
            // Aggiorna la tab list quando i dati utente vengono ricalcolati
            updateTabWithLuckPermsInfo(player);
        }
    }
	 
	// Aggiorna questo metodo per usare i team e il loro ordine di priorità
	private void updateTabWithLuckPermsInfo(Player player) 
	{
		LuckPerms api = LuckPermsProvider.get();
	    UserManager userManager = api.getUserManager();
	    User user = userManager.getUser(player.getUniqueId());

	    if (user != null) {
	        CachedMetaData metaData = user.getCachedData().getMetaData();
	        String prefix = metaData.getPrefix();

	        if (prefix != null) {
	            prefix = ChatColor.translateAlternateColorCodes('&', prefix);
	        } else {
	            prefix = "";
	        }

	        // Use the player's group to get the team from the scoreboard
	        ScoreboardManager manager = Bukkit.getScoreboardManager();
	        Scoreboard board = manager.getMainScoreboard();
	        String group = user.getPrimaryGroup();
	        Team team = board.getTeam(group);

	        if (team == null) {
	            team = board.registerNewTeam(group);
	        }

	        // Update the team's prefix, which will show in the tab list
	        team.setPrefix(prefix);

	        // Add the player to the team if they're not already a member
	        if (!team.hasEntry(player.getName())) 
	        {
	            team.addEntry(player.getName());
	        }

	        // Set the player's display name with the prefix, so it shows in the tab list as "ADMIN <name>"
	        player.setPlayerListName(prefix + player.getName());
	    }
	}
	
	public void showMyTitle(Player player) 
	{
	    player.sendTitle(
	        ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit",
	        ChatColor.GOLD + "" + ChatColor.BOLD + "Buon divertimento nel server!",
	        10, 70, 20);
	}
	
	public void showMyTitleVP(Player player) 
	{
	    try 
	    {
	    	 // Usa ChatColor per colorare il titolo e il sottotitolo
	        String titleText = "{\"text\":\"" + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Qu" + ChatColor.WHITE + "" + ChatColor.BOLD + "Bit" + ChatColor.WHITE + "\"}";
	        String subtitleText = "{\"text\":\"" + ChatColor.GOLD +  "" + ChatColor.BOLD + "Buon divertimento nel server!" + "\"}";

	        Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, titleText);
	        Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"));
	        Object titlePacket = titleConstructor.newInstance(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle);
	        sendPacket(player, titlePacket);

	        Object chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, subtitleText);
	        Constructor<?> subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
	        Object subtitlePacket = subtitleConstructor.newInstance(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null), chatSubtitle, 20, 60, 20);
	        sendPacket(player, subtitlePacket);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private void sendPacket(Player player, Object packet) {
	    try {
	        Object handle = player.getClass().getMethod("getHandle").invoke(player);
	        Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
	        playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private Class<?> getNMSClass(String name) {
	    String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	    try {
	        return Class.forName("net.minecraft.server." + version + "." + name);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}
