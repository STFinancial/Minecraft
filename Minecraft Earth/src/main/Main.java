package main;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	DataManager dataManager;
	
	@Override
	public void onEnable() {
		dataManager = new DataManager(this);
	}
	
	@Override
	public void onDisable() {
		dataManager.quit();
	}
}
