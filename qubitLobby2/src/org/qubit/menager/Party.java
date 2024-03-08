package org.qubit.menager;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

public class Party 
{
	private Set<Player> members = new HashSet<>();
	private Player leader;
	
	//Costruttore leader
	public Party(Player leader)
	{
		this.leader = leader;
		members.add(leader);
	}
	
	// Aggiungere membro nel party
	public void addMember(Player member)
	{
		members.add(member);
	}
	
	// Rimuovi un membro dal party
    public void removeMember(Player member) 
    {
        members.remove(member);
    }
    
    //Getter per il leader
    public Player getLeader() 
    {
        return leader;
    }

    // Getter per i membri
    public Set<Player> getMembers() 
    {
        return members;
    }
    
    public void setLeader(Player member)
    {
    	this.leader = member;
    }
    
}
