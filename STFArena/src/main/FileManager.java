package main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

public class FileManager {
	private static File arenaFolder;
	
	public FileManager() {
		arenaFolder = new File(new File("").getAbsolutePath() + "/plugins/STFArena");
		if (arenaFolder.exists() == false) {
			if (arenaFolder.mkdir() == false) {
				Bukkit.getLogger().info("Unable to create Player Data folder");
			}
		}
		loadArenaTeams();
	}
	
	public static File getArenaFolder() {
		return arenaFolder;
	}
	
	public Map<String, ArenaTeam> loadArenaTeams() {
		Map<String, ArenaTeam> arenaTeams = new HashMap<String, ArenaTeam>();
		for (File arenaFile : arenaFolder.listFiles()) {
			if (arenaFile.isFile() && arenaFile.getAbsolutePath().contains(".yml")) {
				ArenaTeam team = new ArenaTeam(arenaFile);
				arenaTeams.put(team.getName(), team);				
			}
		}
		return arenaTeams;
	}
}
