package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileManager {
	
	public FileManager(Main plugin) {		
		File serverFolder = plugin.getDataFolder();
		File playerData = new File(serverFolder.getPath() + "/Player Data");
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
				InputStream techData = plugin.getResource("/Minecraft Earth/techs.yml");
				OutputStream techFile = new FileOutputStream(techs);
				
				int data = 0;
				while ((data = techData.read()) != -1) {
					techFile.write(data);
				}
				techData.close();
				techFile.close();
			} catch (IOException e) {
				plugin.getLogger().info("Unable to create Techs file");
				
			}
		}
	}

}
