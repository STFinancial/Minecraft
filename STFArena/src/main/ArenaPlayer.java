package main;


import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;


public class ArenaPlayer{
	private Status status;
	private String teamFocused;
	private final Player player;
	boolean saved;
	private final ArrayList<String> teams;
	private final String name;
	private final UUID uuid;


	//Data to save state on entering match;
	private float exhaustion, saturation, exp;
	private int level, remainingAir;
	private ItemStack[] inventory;
	private Location location;
	private double health;
	private Vector velocity;
	private Entity vehicle = null;
	private boolean inVehicle = false;


	public ArenaPlayer(Player player){
		status = Status.FREE;
		teamFocused = null;
		this.player = player;
		saved = false;
		teams = new ArrayList<String>();
		name = player.getName();
		uuid = player.getUniqueId();
		saveState(player);
	}


	public void saveState(Player player) {
		vehicle = null;
		inVehicle = false;
		
		exhaustion = player.getExhaustion();
		saturation = player.getSaturation();
		level = player.getLevel();
		exp = player.getExp();
		remainingAir = player.getRemainingAir();
		inventory = player.getInventory().getContents();
		player.getInventory().clear();
		location = player.getLocation();
		health = player.getHealth();
		if (player.isInsideVehicle()) {
			vehicle = player.getVehicle();
			velocity = player.getVehicle().getVelocity();
			player.leaveVehicle();	
			inVehicle = true;
		}
		else {
			velocity = player.getVelocity();
		}
		
		saved = true;
	}


	public void loadState() {
		if(saved) {
			player.teleport(location);
			player.setExhaustion(exhaustion);
			player.setSaturation(saturation);
			player.setLevel(level);
			player.setExp(exp);
			player.setHealth(health);
			player.setRemainingAir(remainingAir);
			player.getInventory().clear();
			player.getInventory().setContents(inventory);
			
			try {
				vehicle.setPassenger(player);
				vehicle.setVelocity(velocity);
			}
			catch (NullPointerException e) {
				if (inVehicle == false) {
					player.setVelocity(velocity);
				}
			}
		}
		
		saved = false;
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
