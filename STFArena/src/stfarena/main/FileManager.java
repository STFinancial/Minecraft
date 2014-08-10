package stfarena.main;

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
		
	public static File getArenaFolder() {
		if (arenaFolder == null) {
			arenaFolder = new File(new File("").getAbsolutePath() + "/plugins/STFArena");
			if (arenaFolder.exists() == false) {
				if (arenaFolder.mkdir() == false) {
					Bukkit.getLogger().info("Unable to create Arena folder");
				}
			}
		}
		return arenaFolder;
	}

	public static File getTeamsFolder() {
		if (teamsFolder == null) {
			teamsFolder = new File(arenaFolder.getPath() + "/Teams");
			if (teamsFolder.exists() == false) {
				if (teamsFolder.mkdir() == false) {
					Bukkit.getLogger().info("Unable to create Teams folder");
				}
			}
		}
		return teamsFolder;
	}

	public static File getPlayersFolder() {
		if (playersFolder == null) {
			playersFolder = new File(arenaFolder.getPath() + "/Players");
			if (playersFolder.exists() == false) {
				if (playersFolder.mkdir() == false) {
					Bukkit.getLogger().info("Unable to create Players folder");
				}
			}
		}
		return playersFolder;
	}

	public static File getMapsFolder() {
		if (mapsFolder == null) {
			mapsFolder = new File(arenaFolder.getPath() + "/Maps");
			if (mapsFolder.exists() == false) {
				if (mapsFolder.mkdir() == false) {
					Bukkit.getLogger().info("Unable to create Maps folder");
				}
			}
		}
		return mapsFolder;
	}

	public static Map<String, ArenaTeam> loadArenaTeams() {
		Map<String, ArenaTeam> arenaTeams = new HashMap<String, ArenaTeam>();
		for (File arenaFile : getTeamsFolder().listFiles()) {
			if (arenaFile.isFile() && arenaFile.getAbsolutePath().contains(".yml")) {
				ArenaTeam team = new ArenaTeam(arenaFile);
				arenaTeams.put(team.getName(), team);				
			}
		}
		return arenaTeams;
	}
	
	public static void save(ArenaTeam team) {
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
	
	public static ArrayList<Arena> loadArenas(Main plugin) {
		ArrayList<Arena> arenas = new ArrayList<Arena>();
		for (File arenaFile: getMapsFolder().listFiles()) {
			if (arenaFile.isFile() && arenaFile.getAbsolutePath().contains(".yml")) {
				Arena arena  = new Arena(arenaFile, plugin);
				arenas.add(arena);
			}
		}
		return arenas;
	}
}
