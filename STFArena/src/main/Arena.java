package main;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

//@TODO massive work in progress
public class Arena implements Runnable {
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

	public Arena(String name, int size, int redX, int redY, int redZ, int blueX, int blueY, int blueZ, String doorMaterial, Main main) {
		this.name = name;
		this.size = size;
		redSpawn = new Location(Bukkit.getWorld("Arena"), redX, redY, redZ);
		redSpawn = new Location(Bukkit.getWorld("Arena"), blueX, blueY, blueZ);
		door = Material.getMaterial(doorMaterial);
		this.plugin = main;
	}

	public boolean isEmpty() {
		return (redTeam == null && blueTeam == null);
	}

	public void add(ArenaTeam t1, ArenaTeam t2) {
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
			Bukkit.getScheduler().cancelTask(taskID);
		}

	}

}
