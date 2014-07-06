package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
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
	
	public TechData loadPlayerData(Player player, DataManager dataManager) {
		File data = new File(playerData.getPath() + "/" + player.getName() + ".yml");
		if (data.exists() == false) {
			try {
				data.createNewFile();
			}
			catch (IOException e) {
				Bukkit.getLogger().info("Unable to create Player Data file for " + player.getName());
			}
			return new TechData(player, dataManager);
		}
		else {
			YamlConfiguration playerData = YamlConfiguration.loadConfiguration(data);
			int exp = playerData.getInt("exp");
			String currentTech = playerData.getString("currentTech");
			List<String> techs = playerData.getStringList("techs");
			return new TechData(player, dataManager, exp, currentTech, techs);
		}		
	}
	
	public boolean savePlayerData(Player player, TechData techData) {
		File data = new File(playerData.getPath() + "/" + player.getName() + ".yml");
		try {
			YamlConfiguration playerData = YamlConfiguration.loadConfiguration(data);
			playerData.set("exp", techData.getExp());
			playerData.set("currentTech", techData.getCurrentTech());
			playerData.set("techs", techData.getTechs());
			return true;
		} catch(Exception e) {
			Bukkit.getLogger().info("Unable to save Player Data for " + player.getName());
			return false;
		}
	}

}
