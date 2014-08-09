package stfarena.arena;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import stfarena.main.Main;

//@TODO massive work in progress
public class Arena {
	private final static World arenaWorld = buildWorld();
	private final String name;
	private final int size;
	private final Location redSpawn, blueSpawn;
	private final Material doorMaterial;
	private final List<Location> doors = new ArrayList<Location>();
	
	public Arena(String name, int size, int redX, int redY, int redZ, int blueX, int blueY, int blueZ, String material) {
		this.name = name;
		this.size = size;
		Bukkit.getLogger().info("Loading " + size + "s map "+ name);
		redSpawn = new Location(arenaWorld, redX, redY, redZ);
		blueSpawn = new Location(arenaWorld, blueX, blueY, blueZ);
		doorMaterial = Material.getMaterial(material);
		Bukkit.getLogger().info("Loading " + size + "s map "+ name);
	}
	
	public Arena(File arenaFile) {
		YamlConfiguration aC = YamlConfiguration.loadConfiguration(arenaFile);
		this.name = aC.getString("name");
		this.size = aC.getInt("size");
		redSpawn = new Location(arenaWorld, aC.getInt("redX"), aC.getInt("redY"), aC.getInt("redZ"));
		blueSpawn = new Location(arenaWorld, aC.getInt("blueX"), aC.getInt("blueY"), aC.getInt("blueZ"));
		doorMaterial = Material.getMaterial(aC.getString("door material"));
		Bukkit.getLogger().info("Loading " + size + "s map "+ name);
	}
	
	private static World buildWorld() {
		if (Bukkit.getWorld("Arena") == null) {
			Bukkit.getLogger().info("Building Arena World");
			WorldCreator creator = new WorldCreator("Arena");
			creator.environment(Environment.NORMAL).generateStructures(false).type(WorldType.FLAT);
			World arenaWorld = Bukkit.getServer().createWorld(creator);
			arenaWorld.setDifficulty(Difficulty.HARD);
			arenaWorld.setSpawnFlags(false, false);
			arenaWorld.setAnimalSpawnLimit(0);
			arenaWorld.setMonsterSpawnLimit(0);
			arenaWorld.setWaterAnimalSpawnLimit(0);
			arenaWorld.setPVP(true);
			arenaWorld.setAutoSave(true);
		}
		return Bukkit.getWorld("Arena");
	}

	public boolean teleportTeams(ArenaTeam redTeam, ArenaTeam blueTeam) {
		Set<UUID> players = new HashSet<UUID>();
		players.addAll(redTeam.getPlayers());
		players.addAll(blueTeam.getPlayers());
		for (UUID id : players) {
			if (Bukkit.getPlayer(id).isValid() == false) {
				return false;
			}
		}
		for (UUID id : redTeam.getPlayers()) {
			Bukkit.getPlayer(id).teleport(redSpawn);
		}
		for (UUID id : blueTeam.getPlayers()) {
			Bukkit.getPlayer(id).teleport(blueSpawn);
		}
		return true;
	}

	public String name() {
		return name;
	}

	public int size() {
		return size;
	}

	public void openDoors() {
		if (doors.isEmpty()) {
			addDoor(redSpawn);
			addDoor(blueSpawn);
		}
		for (Location location : doors) {
			arenaWorld.getBlockAt(location).setType(Material.AIR);
		}
	}
	
	private void addDoor(Location door) {
		for (int x = door.getBlockX() - 5; x < door.getBlockX() + 5; x++) {
			for (int y = door.getBlockY() - 5; y < door.getBlockY() + 5; y++) {
				for (int z = door.getBlockZ() - 5; z < door.getBlockZ() + 5; z++) {
					if (arenaWorld.getBlockAt(x, y, z).getType().equals(doorMaterial)) { 
						doors.add(new Location(arenaWorld, x, y, z));
					}
				}
			}
		}
	}

	public void closeDoors() {
		for (Location location : doors){
			arenaWorld.getBlockAt(location).setType(doorMaterial);
		}
		clearFloor();
	}
	
	private void clearFloor() {
		int distance = (int) Math.ceil(redSpawn.distance(blueSpawn));
		int x = Math.min(redSpawn.getBlockX(), blueSpawn.getBlockX()) - distance;
		int z = Math.min(redSpawn.getBlockZ(), blueSpawn.getBlockZ()) - distance;
		int maxX = Math.max(redSpawn.getBlockX(), blueSpawn.getBlockX()) + distance;
		int maxZ = Math.max(redSpawn.getBlockZ(), blueSpawn.getBlockZ()) + distance;
		for(; x < maxX; x++) {
			for (; z < maxZ; z++) {
				for (Entity entity : arenaWorld.getChunkAt(x, z).getEntities()) {
					if (entity instanceof Player == false) {
						entity.remove();
					}
				}
			}
		}
	}
}