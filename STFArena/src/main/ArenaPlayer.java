package main;


import java.util.ArrayList;
import java.util.UUID;

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
	ArrayList<String> teams;
	String name;
	UUID uuid;


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
		teams = new ArrayList<String>();
		name = player.getName();
		uuid = player.getUniqueId();
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
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getFocus(){
		return teamFocused;
	}
	public void setFocus(String teamName){
		teamFocused = teamName;
	}
	public void addTeam(String teamName){
		teams.add(teamName);
	}


	public void removeTeam(String teamName){
		teams.remove(teamName);
	}


	public ArrayList<String> getTeams() {
		return teams;
	}


	public String getName() {
		return name;
	}

	public UUID getUUID(){
		return uuid;
	}







}
