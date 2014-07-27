package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

//@TODO massive work in progress
public class Arena extends BukkitRunnable {
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
		redSpawn = new Location(Bukkit.getWorld("Arena"), redX, redY, redZ);
		blueSpawn = new Location(Bukkit.getWorld("Arena"), blueX, blueY, blueZ);
		
		
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
		this.runTaskTimer(plugin, 20, 20);
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

	private void openDoors() {
		doors = new ArrayList<Location>();
		for (int x = redSpawn.getBlockX() - 5; x < redSpawn.getBlockX() + 5; x++) {
			for (int y = redSpawn.getBlockY() - 5; y < redSpawn.getBlockY() + 5; y++) {
				for (int z = redSpawn.getBlockZ() - 5; z < redSpawn.getBlockZ() + 5; z++) {
					Bukkit.getWorld("Arena").getBlockAt(x, y, z).setType(Material.AIR);
					doors.add(new Location(Bukkit.getWorld("Arena"), x, y, z));
				}
			}
		}
	}

	private void closeDoors() {
		for(Location l : doors){
			Bukkit.getWorld("Arena").getBlockAt(l).setType(door);
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
			this.cancel();
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
