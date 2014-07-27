package main;


import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;


public class ArenaPlayer{
	private Status status;
	private String teamFocused;
	boolean saved;
	private final ArrayList<String> teams;
	private final String name;
	private final UUID uuid;


	//Data to save state on entering match;
	private float exhaustion, saturation, exp;
	private int level, remainingAir;
	private ItemStack[] inventory;
	private ItemStack[] armor;
	private Location location;
	private double health;
	private Vector velocity;
	private EntityType vehicleType;
	private HorseData horse;
	private boolean inVehicle = false;


	public ArenaPlayer(Player player){
		status = Status.FREE;
		teamFocused = null;
		saved = false;
		teams = new ArrayList<String>();
		name = player.getName();
		uuid = player.getUniqueId();
	}


	public void saveState() {
		Player player = Bukkit.getPlayer(uuid);
		inVehicle = false;
		
		exhaustion = player.getExhaustion();
		saturation = player.getSaturation();
		level = player.getLevel();
		exp = player.getExp();
		remainingAir = player.getRemainingAir();
		inventory = player.getInventory().getContents();
		armor = player.getInventory().getArmorContents();
		player.getInventory().clear();
		location = player.getLocation();
		health = player.getHealth();
		if (player.isInsideVehicle()) {
			Entity vehicle = player.getVehicle();
			vehicleType = vehicle.getType();
			if (vehicleType.equals(EntityType.HORSE)) {
				horse = new HorseData(vehicle);
			}
			velocity = vehicle.getVelocity();
			player.leaveVehicle();
			vehicle.remove();
			inVehicle = true;
		}
		else {
			velocity = player.getVelocity();
		}
		
		saved = true;
	}


	public void loadState() {
		if(saved) {
			Player player = Bukkit.getPlayer(uuid);
			player.teleport(location);
			player.setExhaustion(exhaustion);
			player.setSaturation(saturation);
			player.setLevel(level);
			player.setExp(exp);
			player.setHealth(health);
			player.setRemainingAir(remainingAir);
			player.getInventory().clear();
			player.getInventory().setContents(inventory);
			player.getInventory().setArmorContents(armor);
			
			if (inVehicle) {
				Entity vehicle = location.getWorld().spawnEntity(location, vehicleType);
				if (vehicleType.equals(EntityType.PIG)) {
					((Pig) vehicle).setSaddle(true);
				}
				else if (vehicleType.equals(EntityType.HORSE)) {
					horse.morphTarget((Horse) vehicle);
					((Horse) vehicle).setOwner(player);
				}
				
				vehicle.setPassenger(player);
				vehicle.setVelocity(velocity);
			}
			else {
				player.setVelocity(velocity);
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
