package main;

import org.bukkit.plugin.java.JavaPlugin;

public class SwordMain extends JavaPlugin {
	private DataManager dataManager;
	private EventManager eventManager;

	@Override
	public void onEnable() {	
		dataManager = new DataManager(this);
		eventManager = new EventManager(this, dataManager);
		this.getServer().getPluginManager().registerEvents(eventManager, this);
	}
	
	@Override
	public void onDisable() {
		dataManager.quit();
		dataManager = null;
		eventManager = null;
	}	
}
