package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

//@TODO massive work in progress
public class Arena implements Runnable {
	private final static World arenaWorld = ArenaWorld.build();
	String name;
	int size;
	Location redSpawn;
	Location blueSpawn;
	ArenaTeam redTeam;
	ArenaTeam blueTeam;
	int taskID;
	int timeTillTeleport;
	int timeTillDoorOpen;
	Main plugin;
	Material door;
	ArrayList<Location> doors;
	HashSet<UUID> redPlayersAlive;
	HashSet<UUID> bluePlayersAlive;

	public Arena(String name, int size, int redX, int redY, int redZ, int blueX, int blueY, int blueZ, String doorMaterial, Main main) {
		this.name = name;
		this.size = size;
		Bukkit.getLogger().info("We made it to here! creating "+ name);
		redSpawn = new Location(arenaWorld, redX, redY, redZ);
		blueSpawn = new Location(arenaWorld, blueX, blueY, blueZ);
		
		
		door = Material.getMaterial(doorMaterial);
		this.plugin = main;
	}

	public boolean isEmpty() {
		return (redTeam == null && blueTeam == null);
	}

	public void add(ArenaTeam t1, ArenaTeam t2) {
		redPlayersAlive = new HashSet<UUID>();
		bluePlayersAlive = new HashSet<UUID>();
		for(UUID p:t1.getPlayers()){
			redPlayersAlive.add(p);
		}
		for(UUID p:t2.getPlayers()){
			bluePlayersAlive.add(p);
		}
		redTeam = t1;
		blueTeam = t2;
		timeTillTeleport = 10;
		timeTillDoorOpen = 15;
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 20, 20);
	}

	public String getName() {
		return name;
	}

	public int getSize() {
		return size;
	}

	public ArenaTeam getRedTeam() {
		return redTeam;
	}

	public ArenaTeam getBlueTeam() {
		return blueTeam;
	}

	private void sendAllPlayers(String message) {
		for (UUID p : redTeam.getPlayers()) {
			Bukkit.getPlayer(p).sendMessage(message);
		}
		for (UUID p : blueTeam.getPlayers()) {
			Bukkit.getPlayer(p).sendMessage(message);
		}
	}
	
	private void clearFloor() {
		double radius = redSpawn.distance(blueSpawn) / 2;
		Location center = redSpawn.add(redSpawn.getDirection().midpoint(blueSpawn.getDirection()));
		center = arenaWorld.getHighestBlockAt(center).getLocation();
		Item cookie = arenaWorld.dropItem(center, new ItemStack(Material.COOKIE));
		for (Entity item : cookie.getNearbyEntities(radius, radius, radius)) {
			item.remove();
		}
		cookie.remove();
	}

	private void openDoors() {
		clearFloor();
		doors = new ArrayList<Location>();
		for (int x = redSpawn.getBlockX() - 5; x < redSpawn.getBlockX() + 5; x++) {
			for (int y = redSpawn.getBlockY() - 5; y < redSpawn.getBlockY() + 5; y++) {
				for (int z = redSpawn.getBlockZ() - 5; z < redSpawn.getBlockZ() + 5; z++) {
					if (arenaWorld.getBlockAt(x, y, z).getType().equals(door)) { 
						arenaWorld.getBlockAt(x, y, z).setType(Material.AIR);
						doors.add(new Location(arenaWorld, x, y, z));
					}
				}
			}
		}
		for (int x = blueSpawn.getBlockX() - 5; x < blueSpawn.getBlockX() + 5; x++) {
			for (int y = blueSpawn.getBlockY() - 5; y < blueSpawn.getBlockY() + 5; y++) {
				for (int z = blueSpawn.getBlockZ() - 5; z < blueSpawn.getBlockZ() + 5; z++) {
					if (arenaWorld.getBlockAt(x, y, z).getType().equals(door)) { 
						arenaWorld.getBlockAt(x, y, z).setType(Material.AIR);
						doors.add(new Location(arenaWorld, x, y, z));
					}
				}
			}
		}
	}

	private void closeDoors() {
		for(Location location : doors){
			arenaWorld.getBlockAt(location).setType(door);
		}
	}

	@Override
	public void run() {
		if (timeTillTeleport > 0) {
			if (timeTillTeleport == 10 || timeTillTeleport < 4)
				sendAllPlayers("Teleporting to arena: " + name + " in " + timeTillTeleport + "seconds");
			timeTillTeleport--;
		} else if (timeTillDoorOpen == 15) {
			for (UUID p : redTeam.getPlayers()) {
				plugin.getDataManager().getPlayer(p).saveState();
				Bukkit.getLogger().info("with arena "+ name);
				Bukkit.getLogger().info("with ax location " + redSpawn.getBlockX());
				Bukkit.getLogger().info("red spawn is in world " + redSpawn.getWorld().getName());
				Bukkit.getLogger().info(" with player " + Bukkit.getPlayer(p).getName());
				Bukkit.getPlayer(p).teleport(redSpawn);
			}
			for (UUID p : blueTeam.getPlayers()) {
				plugin.getDataManager().getPlayer(p).saveState();
				Bukkit.getPlayer(p).teleport(blueSpawn);
			}
			timeTillDoorOpen--;
		} else if (timeTillDoorOpen > 0) {
			if (timeTillDoorOpen == 10 || timeTillDoorOpen == 5 || timeTillDoorOpen < 4)
				sendAllPlayers("Match will start in " + timeTillDoorOpen + "seconds");
			timeTillDoorOpen--;
		} else {
			sendAllPlayers("The Match Begins!");
			openDoors();
			Bukkit.getScheduler().cancelTask(taskID);
			taskID = -1;
		}

	}

	public void recordDeath(UUID ID) {
		if(redPlayersAlive.contains(ID)){
			redPlayersAlive.remove(ID);
			sendAllPlayers(Bukkit.getPlayer(ID).getName() + " on Red team has been slain!");
		}
		if(bluePlayersAlive.contains(ID)){
			bluePlayersAlive.remove(ID);
			sendAllPlayers(Bukkit.getPlayer(ID).getName() + " on Blue team has been slain!");
		}
		if(redPlayersAlive.size() == 0){
			sendAllPlayers("All members of Blue team have been slain!");
			plugin.getMatchManager().gameFinished(this, false);
		}else if(bluePlayersAlive.size() == 0){
			sendAllPlayers("All members of Red team have been slain!");
			plugin.getMatchManager().gameFinished(this, true);
		}
		
	}
	
	public void clean(){
		closeDoors();
		redPlayersAlive = null;
		bluePlayersAlive = null;
		redTeam = null;
		blueTeam = null;
	}
}
