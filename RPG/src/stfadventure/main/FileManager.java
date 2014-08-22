package stfadventure.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class FileManager {
	private static File mainFolder;
	private static File playersFolder;
		
	public static File getMainFolder() {
		if (mainFolder == null) {
			mainFolder = new File(new File("").getAbsolutePath() + "/plugins/STFAdventure");
			if (mainFolder.exists() == false) {
				if (mainFolder.mkdir() == false) {
					Bukkit.getLogger().info("Unable to create STF Adventure folder");
				}
			}
		}
		return mainFolder;
	}
	
	public static File getPlayersFolder() {
		if (playersFolder == null) {
			playersFolder = new File(getMainFolder().getPath() + "/Players");
			if (playersFolder.exists() == false) {
				if (playersFolder.mkdir() == false) {
					Bukkit.getLogger().info("Unable to create STF Adventure Players folder");
				}
			}
		}
		return playersFolder;
	}
	
	public static YamlConfiguration loadPlayerConfig(Player player) {
		File playerFile = new File(getPlayersFolder() + "/" + player.getUniqueId() + ".yml");
		boolean newFile = false;
		if (playerFile.exists() == false) {
			try {
				playerFile.createNewFile();
				newFile = true;
			} catch (IOException e) {
				Bukkit.getLogger().info("Unable to create player file for " + player.getUniqueId());
			}
		}
		YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
		if (newFile) {
			playerConfig.set("class", "BEGINNER");
			playerConfig.set("level", 1);
			playerConfig.set("exp", 0);
			playerConfig.set("resource", 0);
			try {
				playerConfig.save(playerFile);
			} catch (IOException e) {
				Bukkit.getLogger().info("Unable to save configuration file for " + player.getUniqueId());
			}
		}
		return playerConfig;
	}
}
