package main;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class ArenaPlayer{
	Status status;
	String teamFocused;
	Player player;
	boolean saved;
	
	//Data to save state on entering match;
	float exhaustion, saturation;
	int level, totalExp, airRemaining;
	PlayerInventory inventory;
	Location location;
	World world;
	double health;
	Vector velocity;
	
	public ArenaPlayer(Player player){
		status = Status.FREE;
		teamFocused = null;
		this.player = player;
		saved = false;
	}
	
	public void saveState(){
		
		
		
		saved = true;
	}
	
	public void loadState(){
		if(saved){
			
		}
	}
	
	public Status getStatus(){
		return status;
	}
	
	public String getTeamFocused(){
		return teamFocused;
	}
}
