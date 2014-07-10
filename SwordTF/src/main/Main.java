package main;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Main extends JavaPlugin {
	EventManager eventManager;
	DataManager dataManager;
	public static final int ENERGY_PER_SWING = 40;
	public static final int ENERGY_PER_TICK = 1;
	public static final int TICKS_PER_UPDATE = 5;
	public static final Set<Material> SWORDS = new HashSet<Material>(
			Arrays.asList(new Material[] { Material.DIAMOND_SWORD,
					Material.IRON_SWORD, Material.GOLD_SWORD,
					Material.STONE_SWORD, Material.WOOD_SWORD }));
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
