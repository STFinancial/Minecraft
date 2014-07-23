package main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import combat.EventManager;


public class Main extends JavaPlugin {
	public static Plugin plugin;
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new EventManager(), this);
		plugin = this;
		for (Player player : Bukkit.getOnlinePlayers()) {
			EventManager.addGamePlayer(player);
		}
	}
	
	@Override
	public void onDisable() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			EventManager.removeGamePlayer(player);
		}
	}
}
