package stfadventure.main;

import java.io.File;
import org.bukkit.Bukkit;

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
}
