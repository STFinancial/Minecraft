package arena;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

public class FileManager {
	private static File arenaFolder;
	private static File teamsFolder;
	private static File playersFolder;
	
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
		loadArenaTeams();
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
}
