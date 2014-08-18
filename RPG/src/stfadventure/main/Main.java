package stfadventure.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import stfadventure.events.EventManager;


public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new EventManager(this), this);
	}
	
	@Override
	public void onDisable() {
	}
}
