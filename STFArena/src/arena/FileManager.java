package arena;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

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
		//loadArenaTeams();
		//loadArenas();
	}
	
	public File getArenaFolder() {
		return arenaFolder;
	}
	
	public File getTeamsFolder() {
		return teamsFolder;
	}
	
	public File getPlayersFolder() {
		return playersFolder;
	}
	
	public File getMapsFolder() {
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
	
	public void save(ArenaTeam team) {
		YamlConfiguration arenaConfiguration = YamlConfiguration.loadConfiguration(team.getFile());
		arenaConfiguration.set("name", team.getName());
		arenaConfiguration.set("win", team.getNumberOfWins());
		arenaConfiguration.set("loss", team.getNumberOfLosses());
		arenaConfiguration.set("rating", team.getRating());
		arenaConfiguration.set("size", team.getSize());
		List<String> playerList = new ArrayList<String>();
		for (UUID playerUuid : team.getPlayers()) {
			playerList.add(playerUuid.toString());
		}
		arenaConfiguration.set("players", playerList);
		try {
			arenaConfiguration.save(team.getFile());
		} catch (IOException e) {
			Bukkit.getLogger().info("Unable to save Arena file for " + team.getName());
		}
	}
	
	public ArrayList<Arena> loadArenas(Main plugin) {
		ArrayList<Arena> arenas = new ArrayList<Arena>();
		for (File arenaFile: mapsFolder.listFiles()) {
			if (arenaFile.isFile() && arenaFile.getAbsolutePath().contains(".yml")) {
				Arena arena  = new Arena(arenaFile, plugin);
				arenas.add(arena);
			}
		}
		return arenas;
	}
}
