package qsik;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import qsik.MatchManager.MatchStatus;
import arena.ArenaTeam;

//@TODO massive work in progress
public class Arena {
	private final static World arenaWorld = buildWorld();
	private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	private final String name;
	private final int size;
	private final Location redSpawn;
	private final Location blueSpawn;
	private ArenaTeam redTeam;
	private ArenaTeam blueTeam;
	private Team rTeam;
	private Team bTeam;
	private final Material doorMaterial;
	private final List<Location> doors = new ArrayList<Location>();
	private final Set<UUID> redPlayersAlive = new HashSet<UUID>();
	private final Set<UUID> bluePlayersAlive = new HashSet<UUID>();
	
	public Arena(String name, int size, int redX, int redY, int redZ, int blueX, int blueY, int blueZ, String material) {
		this.name = name;
		this.size = size;
		Bukkit.getLogger().info("Loading " + size + "s map "+ name);
		redSpawn = new Location(arenaWorld, redX, redY, redZ);
		blueSpawn = new Location(arenaWorld, blueX, blueY, blueZ);
		doorMaterial = Material.getMaterial(material);
		Bukkit.getLogger().info("Loading " + size + "s map "+ name);
		buildScoreboard();
	}
	
	public Arena(File arenaFile) {
		YamlConfiguration aC = YamlConfiguration.loadConfiguration(arenaFile);
		this.name = aC.getString("name");
		this.size = aC.getInt("size");
		redSpawn = new Location(arenaWorld, aC.getInt("redX"), aC.getInt("redY"), aC.getInt("redZ"));
		blueSpawn = new Location(arenaWorld, aC.getInt("blueX"), aC.getInt("blueY"), aC.getInt("blueZ"));
		doorMaterial = Material.getMaterial(aC.getString("door material"));
		Bukkit.getLogger().info("Loading " + size + "s map "+ name);
		buildScoreboard();
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

	public void assignTeams(ArenaTeam redTeam, ArenaTeam blueTeam) {
		this.redTeam = redTeam;
		this.blueTeam = blueTeam;
		redPlayersAlive.addAll(redTeam.getPlayers());
		bluePlayersAlive.addAll(blueTeam.getPlayers());
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

	private void sendAllPlayers(String message) {
		for (UUID p : redTeam.getPlayers()) {
			if(Bukkit.getPlayer(p) != null){
				if(Bukkit.getPlayer(p).isOnline())
					Bukkit.getPlayer(p).sendMessage(message);
			}
		}
		for (UUID p : blueTeam.getPlayers()) {
			if(Bukkit.getPlayer(p) != null){
				if(Bukkit.getPlayer(p).isOnline())
					Bukkit.getPlayer(p).sendMessage(message);
			}
		}
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

	private void closeDoors() {
		for (Location location : doors){
			arenaWorld.getBlockAt(location).setType(doorMaterial);
		}
	}

	public MatchStatus recordDeath(UUID id) {
		if (redPlayersAlive.contains(id)){
			redPlayersAlive.remove(id);
			sendAllPlayers(Bukkit.getPlayer(id).getName() + " on Red team has been slain!");
			if(redPlayersAlive.isEmpty()){
				sendAllPlayers(ChatColor.GREEN + "All members of Red Team have been slain!");
				sendAllPlayers(ChatColor.GREEN + "Match over, teleporting in 5 seconds");
				return MatchStatus.BLUE_WON;
			}
		}
		if (bluePlayersAlive.contains(id)){
			bluePlayersAlive.remove(id);
			sendAllPlayers(Bukkit.getPlayer(id).getName() + " on Blue team has been slain!");
			if(bluePlayersAlive.isEmpty()){
				sendAllPlayers(ChatColor.GREEN + "All members of Blue Team have been slain!");
				sendAllPlayers(ChatColor.GREEN + "Match over, teleporting in 5 seconds");
				return MatchStatus.RED_WON;
			}
		}
		return MatchStatus.IN_PROGRESS;
	}

	public void clean(){
		removeScoreboard();
		closeDoors();
		clearFloor();
		redPlayersAlive.clear();
		bluePlayersAlive.clear();
		redTeam = null;
		blueTeam = null;
	}
	
	public void buildScoreboard() {
		rTeam = scoreboard.registerNewTeam("Red Team");
		bTeam = scoreboard.registerNewTeam("Blue Team");
		rTeam.setAllowFriendlyFire(false);
		bTeam.setAllowFriendlyFire(false);
		rTeam.setPrefix(ChatColor.RED + "Red Team: ");
		bTeam.setPrefix(ChatColor.BLUE + "Blue Team: ");
	}
	
	public void assignScoreboard() {
		for (UUID id : redTeam.getPlayers()) {
			rTeam.addPlayer(Bukkit.getPlayer(id));
			Bukkit.getPlayer(id).setScoreboard(scoreboard);
		}
		for (UUID id : blueTeam.getPlayers()) {
			bTeam.addPlayer(Bukkit.getPlayer(id));
			Bukkit.getPlayer(id).setScoreboard(scoreboard);
		}
		Objective objective = scoreboard.registerNewObjective("Health", "health");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	public void removeScoreboard() {
		for (UUID id : redTeam.getPlayers()) {
			rTeam.removePlayer(Bukkit.getPlayer(id));
			Bukkit.getPlayer(id).setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		}
		for (UUID id : blueTeam.getPlayers()) {
			bTeam.removePlayer(Bukkit.getPlayer(id));
			Bukkit.getPlayer(id).setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		}
		scoreboard.getObjective(DisplaySlot.SIDEBAR).unregister();
	}
}
