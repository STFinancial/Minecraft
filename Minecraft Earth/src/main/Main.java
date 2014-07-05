package main;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	DataManager pManager;
	
	@Override
	public void onEnable() {
		pManager = new DataManager(this);
	}
	
	@Override
	public void onDisable() {
		pManager.cleanup();
	}
}
