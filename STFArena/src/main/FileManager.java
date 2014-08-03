package main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import arena.Arena;
import arena.ArenaTeam;

public class FileManager {
	private static File arenaFolder;
	private static File teamsFolder;
	private static File playersFolder;
	private static File mapsFolder;
	
	public FileManager() {
		arenaFolder = new File(new File("").getAbsolutePath() + "/plugins/STFArena");
		if (arenaFolder.exists() == false) {
			if (arenaFolder.mkdir() == false) {
				Bukkit.getLogger().info("Unable to create Arena folder");
			}
		}
		teamsFolder = new File(arenaFolder.getPath() + "/Teams");
		if (teamsFolder.exists() == false) {
			if (teamsFolder.mkdir() == false) {
				Bukkit.getLogger().info("Unable to create Teams folder");
			}
		}
		playersFolder = new File(arenaFolder.getPath() + "/Players");
		if (playersFolder.exists() == false) {
			if (playersFolder.mkdir() == false) {
				Bukkit.getLogger().info("Unable to create Players folder");
			}
		}
		mapsFolder = new File(arenaFolder.getPath() + "/Maps");
		if (mapsFolder.exists() == false) {
			if (mapsFolder.mkdir() == false) {
				Bukkit.getLogger().info("Unable to create Maps folder");
			}
		}
	}
	
	public static File getArenaFolder() {
		return arenaFolder;
	}
	
	public static File getTeamsFolder() {
		return teamsFolder;
	}
	
	public static File getPlayersFolder() {
		return playersFolder;
	}
	
	public static File getMapsFolder() {
		return mapsFolder;
	}
	
	public Map<String, ArenaTeam> loadArenaTeams() {
		Map<String, ArenaTeam> arenaTeams = new HashMap<String, ArenaTeam>();
		for (File arenaFile : teamsFolder.listFiles()) {
			if (arenaFile.isFile() && arenaFile.getAbsolutePath().contains(".yml")) {
				ArenaTeam team = new ArenaTeam(arenaFile);
				arenaTeams.put(team.getName(), team);				
			}
		}
		return arenaTeams;
	}
	
	public ArrayList<Arena> loadArenas(STFArena plugin) {
		ArrayList<Arena> arenas = new ArrayList<Arena>();
		for (File arenaFile: mapsFolder.listFiles()) {
			if (arenaFile.isFile() && arenaFile.getAbsolutePath().contains(".yml")) {
				Arena arena  = new Arena(arenaFile);
				arenas.add(arena);
			}
		}
		return arenas;
	}
}
