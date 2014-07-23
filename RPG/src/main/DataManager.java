package main;

import org.bukkit.plugin.Plugin;

public class DataManager {
	private static Plugin plugin;
	
	public DataManager(Plugin plugin) {
		DataManager.plugin = plugin;
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}

}
