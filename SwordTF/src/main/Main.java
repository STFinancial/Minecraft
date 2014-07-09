package main;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	EventManager eventManager;
	
	@Override
	public void onEnable() {
		eventManager = new EventManager(this);
		this.getServer().getPluginManager().registerEvents(eventManager, this);
	}
	
	@Override
	public void onDisable() {
		eventManager.quit();
	}
}
