package main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Main extends JavaPlugin {
	EventManager eventManager;
	DataManager dataManager;
	public static final int ENERGY_PER_SWING = 40;
	public static final int ENERGY_PER_TICK = 3;
	@Override
	public void onEnable() {
		
		eventManager = new EventManager(this);
		dataManager = new DataManager(this);
		this.getServer().getPluginManager().registerEvents(eventManager, this);
	}
	
	@Override
	public void onDisable() {
		eventManager.quit();
	}

	public DataManager getDataManager() {
		return dataManager;
	}
	
}
