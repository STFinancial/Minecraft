package arena;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import net.minecraft.server.v1_7_R3.ChatBaseComponent;

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
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

//@TODO massive work in progress
public class Arena implements Runnable {
	private final static World arenaWorld = buildWorld();
	private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	String name;
	int size;
	Location redSpawn;
	Location blueSpawn;
	ArenaTeam redTeam;
	ArenaTeam blueTeam;
	int taskId = -1;
	int timeTillTeleport;
	int timeTillDoorOpen;
	Main plugin;
	Material door;
	ArrayList<Location> doors;
	HashSet<UUID> redPlayersAlive;
	HashSet<UUID> bluePlayersAlive;
	File arenaFile;
	boolean matchOver = false;
	boolean redWon = false;
	
	public Arena(String name, int size, int redX, int redY, int redZ, int blueX, int blueY, int blueZ, String doorMaterial, Main main) {
		this.name = name;
		this.size = size;
		Bukkit.getLogger().info("Loading " + size + "s map "+ name);
		redSpawn = new Location(arenaWorld, redX, redY, redZ);
		blueSpawn = new Location(arenaWorld, blueX, blueY, blueZ);
		door = Material.getMaterial(doorMaterial);
		this.plugin = main;
	}
	
	public Arena(File arenaFile, Main main) {
		this.arenaFile = arenaFile;
		YamlConfiguration aC = YamlConfiguration.loadConfiguration(arenaFile);
		this.name = aC.getString("name");
		this.size = aC.getInt("size");
		redSpawn = new Location(arenaWorld, aC.getInt("redX"), aC.getInt("redY"), aC.getInt("redZ"));
		blueSpawn = new Location(arenaWorld, aC.getInt("blueX"), aC.getInt("blueY"), aC.getInt("blueZ"));
		door = Material.getMaterial(aC.getString("door material"));
		Bukkit.getLogger().info("Loading " + size + "s map "+ name);
		this.plugin = main;
	}
	
	public static World buildWorld() {
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
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 20, 20);
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
		int maxX = Math.max(redSpawn.getBlockX(), blueSpawn.getBlockZ()) + distance;
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
		if (matchOver) {
			plugin.getMatchManager().gameFinished(this, redWon);
		}
		else {
			if (timeTillTeleport > 0) {
				if (timeTillTeleport == 10 || timeTillTeleport < 4)
					sendAllPlayers("Teleporting to arena: " + name + " in " + timeTillTeleport + "seconds");
				timeTillTeleport--;
			} else if (timeTillDoorOpen == 15) {
				boolean redTeamValid = true;
				boolean blueTeamValid = true;
				for (UUID p : redTeam.getPlayers()) {
					if (Bukkit.getPlayer(p).isValid() == false) {
						redTeamValid = false;
						break;
					}	
				}
				for (UUID p : blueTeam.getPlayers()) {
					if (Bukkit.getPlayer(p).isValid() == false) {
						blueTeamValid = false;
						break;
					}	
				}
				if (redTeamValid && blueTeamValid) {
					for (UUID p : redTeam.getPlayers()) {
						plugin.getDataManager().getPlayer(p).saveState();
						Bukkit.getPlayer(p).teleport(redSpawn);
						plugin.getDataManager().getPlayer(p).matchStart();
					}
					for (UUID p : blueTeam.getPlayers()) {
						plugin.getDataManager().getPlayer(p).saveState();
						Bukkit.getPlayer(p).teleport(blueSpawn);
						plugin.getDataManager().getPlayer(p).matchStart();
					}
					buildScoreboard();
				}
				else {
					sendAllPlayers("Match canceled due to invalid player");
					Bukkit.getScheduler().cancelTask(taskId);
					if (redTeamValid) {
						plugin.getQueueManager().addTeamToQueue(redTeam);
					}
					if (blueTeamValid) {
						plugin.getQueueManager().addTeamToQueue(blueTeam);
					}
					clean();
					plugin.getMatchManager().addArena(this);
				}
				timeTillDoorOpen--;
			} else if (timeTillDoorOpen > 0) {
				if (timeTillDoorOpen == 10 || timeTillDoorOpen == 5 || timeTillDoorOpen < 4)
					sendAllPlayers("Match will start in " + timeTillDoorOpen + "seconds");
				timeTillDoorOpen--;
			} else {
				sendAllPlayers("The Match Begins!");
				openDoors();
				Bukkit.getScheduler().cancelTask(taskId);
				taskId = -1;
			}
		}
	}

	public void recordDeath(UUID ID) {
		if(redPlayersAlive.contains(ID)){
			redPlayersAlive.remove(ID);
			sendAllPlayers(Bukkit.getPlayer(ID).getName() + " on Red team has been slain!");
			if(redPlayersAlive.size() == 0 && bluePlayersAlive.size() > 0){
				sendAllPlayers("All members of Blue team have been slain!");
				matchOver = true;
				redWon = false;
				sendAllPlayers("Match over, teleporting in 5 seconds");
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, 100);
			}
		}
		if(bluePlayersAlive.contains(ID)){
			bluePlayersAlive.remove(ID);
			sendAllPlayers(Bukkit.getPlayer(ID).getName() + " on Blue team has been slain!");
			if(bluePlayersAlive.size() == 0 && redPlayersAlive.size() > 0){
				sendAllPlayers("All members of Red team have been slain!");
				matchOver = true;
				redWon = true;
				sendAllPlayers("Match over, teleporting in 5 seconds");
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, 100);
			}
		}
	}

	public void clean(){
		closeDoors();
		clearFloor();
		redPlayersAlive = null;
		bluePlayersAlive = null;
		redTeam = null;
		blueTeam = null;
		matchOver = false;
		redWon = false;
		taskId = -1;
	}
	
	public void buildScoreboard() {
		Team rTeam = scoreboard.registerNewTeam(ChatColor.RED + "Red Team");
		rTeam.setAllowFriendlyFire(false);
		for (UUID id : redTeam.getPlayers()) {
			rTeam.addPlayer(Bukkit.getOfflinePlayer(id));
			Bukkit.getPlayer(id).setScoreboard(scoreboard);
		}
		
		Team bTeam = scoreboard.registerNewTeam(ChatColor.BLUE + "Blue Team");
		for (UUID id : blueTeam.getPlayers()) {
			bTeam.addPlayer(Bukkit.getOfflinePlayer(id));
			Bukkit.getPlayer(id).setScoreboard(scoreboard);
		}
		bTeam.setAllowFriendlyFire(false);
		
		Objective objective = scoreboard.registerNewObjective("Health", "health");
	}
}
