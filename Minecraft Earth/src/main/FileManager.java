package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class FileManager {
	private final File playerData;
	private final YamlConfiguration techFile;
	
	public FileManager(Main plugin) {	
		File serverFolder = plugin.getDataFolder();
		playerData = new File(serverFolder.getPath() + "/Player Data");
		if (playerData.exists() == false) {
			try {
				playerData.createNewFile();
			} catch (IOException e) {
				plugin.getLogger().info("Unable to create Player Data folder");
			}			
		}
		File techs = new File(serverFolder.getPath() + "/Player Data/techs.yml");
		if (techs.exists() == false) {
			try {
				InputStream techInput = plugin.getResource("/Minecraft Earth/techs.yml");
				OutputStream techOutput = new FileOutputStream(techs);
				
				int data = 0;
				while ((data = techInput.read()) != -1) {
					techOutput.write(data);
				}
				techInput.close();
				techOutput.close();
			} catch (IOException e) {
				plugin.getLogger().info("Unable to create Techs file");
				
			}
		}
		techFile = YamlConfiguration.loadConfiguration(techs);
	}
	
	public HashMap<String, String> parseTechs() {
		HashMap<String, String> techs = new HashMap<String, String>();
		for(String tech: techFile.getKeys(false)) {
			for(String allowed : techFile.getStringList(tech)) {
				techs.put(allowed, tech);
			}
		}
		return techs;
	}
		
	public void load() {
		
	}
	
	public void quit() {
		
	}
	
	public Technology loadPlayerData(Player player) {
		File data = new File(playerData.getPath() + "/" + player.getName() + ".yml");
		YamlConfiguration.loadConfiguration(data).getList("techs");
		return null;
	}
	
	public boolean savePlayerData(Player player, List<String> techs) {
		File data = new File(playerData.getPath() + "/" + player.getName() + ".yml");
		try {
			YamlConfiguration.loadConfiguration(data).set("techs", techs);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

}
