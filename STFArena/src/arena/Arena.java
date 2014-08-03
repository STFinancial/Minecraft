package arena;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
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
import org.bukkit.util.Vector;

public class Arena {
	private final static World arenaWorld = buildWorld();
	private final String name;
	private final int size;
	private final Location redSpawn;
	private final Location blueSpawn;
	private final Chunk arenaStart;
	private final Chunk arenaEnd;
	private ArenaTeam redTeam;
	private ArenaTeam blueTeam;
	private final Material doorMaterial;
	private final List<Location> doors = new ArrayList<Location>();
	private boolean available = true;

	public Arena(String name, int size, Vector red, Vector blue, Vector cornerStart, Vector cornerEnd, String material) {
		this.name = name;
		this.size = size;
		Bukkit.getLogger().info("Loading " + size + "s map "+ name);
		redSpawn = new Location(arenaWorld, red.getX(), red.getY(), red.getZ());
		blueSpawn = new Location(arenaWorld, blue.getX(), blue.getY(), blue.getZ());
		arenaStart = arenaWorld.getChunkAt(cornerStart.getBlockX(), cornerStart.getBlockZ());
		arenaEnd = arenaWorld.getChunkAt(cornerEnd.getBlockX(), cornerEnd.getBlockZ());		
		doorMaterial = Material.getMaterial(material);
	}
	
	public Arena(File arenaFile) {
		YamlConfiguration arenaData = YamlConfiguration.loadConfiguration(arenaFile);
		this.name = arenaData.getString("name");
		this.size = arenaData.getInt("size");
		redSpawn = new Location(arenaWorld, arenaData.getInt("redX"), arenaData.getInt("redY"), arenaData.getInt("redZ"));
		blueSpawn = new Location(arenaWorld, arenaData.getInt("blueX"), arenaData.getInt("blueY"), arenaData.getInt("blueZ"));
		arenaStart = arenaWorld.getChunkAt(arenaData.getInt("startX"), arenaData.getInt("startZ"));
		arenaEnd = arenaWorld.getChunkAt(arenaData.getInt("endX"), arenaData.getInt("endZ"));
		doorMaterial = Material.getMaterial(arenaData.getString("door material"));
	}

	public boolean isAvailable() {
		return available;
	}
	
	public static World buildWorld() {
		Bukkit.getLogger().info("Building Arena World");
		WorldCreator creator = new WorldCreator("Arena");
		creator.environment(Environment.NORMAL).generateStructures(false).type(WorldType.FLAT);
		World world = Bukkit.getServer().createWorld(creator);
		world.setDifficulty(Difficulty.HARD);
		world.setSpawnFlags(false, false);
		world.setAnimalSpawnLimit(0);
		world.setMonsterSpawnLimit(0);
		world.setWaterAnimalSpawnLimit(0);
		world.setPVP(true);
		world.setAutoSave(true);
		return world;
	}

	public void assignTeams(ArenaTeam redTeam, ArenaTeam blueTeam) {
		this.redTeam = redTeam;
		this.blueTeam = blueTeam;
		available = false;
	}

	public String name() {
		return name;
	}

	public int size() {
		return size;
	}

	public ArenaTeam getRedTeam() {
		return redTeam;
	}

	public ArenaTeam getBlueTeam() {
		return blueTeam;
	}

	public void startMatch() {
		for (int x = redSpawn.getBlockX() - 5; x < redSpawn.getBlockX() + 5; x++) {
			for (int y = redSpawn.getBlockY() - 5; y < redSpawn.getBlockY() + 5; y++) {
				for (int z = redSpawn.getBlockZ() - 5; z < redSpawn.getBlockZ() + 5; z++) {
					if (arenaWorld.getBlockAt(x, y, z).getType().equals(doorMaterial)) { 
						arenaWorld.getBlockAt(x, y, z).setType(Material.AIR);
						doors.add(new Location(arenaWorld, x, y, z));
					}
				}
			}
		}
		for (int x = blueSpawn.getBlockX() - 5; x < blueSpawn.getBlockX() + 5; x++) {
			for (int y = blueSpawn.getBlockY() - 5; y < blueSpawn.getBlockY() + 5; y++) {
				for (int z = blueSpawn.getBlockZ() - 5; z < blueSpawn.getBlockZ() + 5; z++) {
					if (arenaWorld.getBlockAt(x, y, z).getType().equals(doorMaterial)) { 
						arenaWorld.getBlockAt(x, y, z).setType(Material.AIR);
						doors.add(new Location(arenaWorld, x, y, z));
					}
				}
			}
		}
	}

	public void cleanup() {
		for(Location location : doors) {
			arenaWorld.getBlockAt(location).setType(doorMaterial);
		}
		for (int x = arenaStart.getX(); x < arenaEnd.getX(); x++) {
			for (int z = arenaStart.getZ(); z < arenaEnd.getZ(); z++) {
				for (Entity entity : arenaWorld.getChunkAt(x, z).getEntities()) {
					if (entity instanceof Player == false) {
						entity.remove();
					}
				}
			}
		}
		redTeam = null;
		blueTeam = null;
		available = true;
	}
}
