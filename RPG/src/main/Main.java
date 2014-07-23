package main;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import combat.EventManager;


public class Main extends JavaPlugin {
	public static Plugin plugin;
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new EventManager(), this);
		plugin = this;
	}
	
	@Override
	public void onDisable() {
	}
}
