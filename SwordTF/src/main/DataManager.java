package main;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DataManager {
	private final Main plugin;
	private final EventManager eventManager;
	private HashMap<Player, SwordData> swordData;

	public DataManager(Main plugin) {
		this.plugin = plugin;
		swordData = new HashMap<Player, SwordData>();
		eventManager = new EventManager(plugin, this);
		plugin.getServer().getPluginManager().registerEvents(eventManager, plugin);
		load();
	}

	private void load() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			add(player);
		}
	}

	public void quit() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			remove(player);
		}
	}

	public void remove(Player player) {
		swordData.get(player).stop();
		swordData.remove(player);
	}

	public void add(Player player) {
		swordData.put(player, new SwordData(plugin, player));
	}

	public boolean canPlayerSwing(Player player) {
		return swordData.get(player).swingReady();
	}

	public void swing(Player player) {
		swordData.get(player).swingPerformed();
	}

	public int currentEnergy(Player player) {
		return swordData.get(player).getEnergy();
	}

	public boolean canPlayerDamage(Player player) {
		return swordData.get(player).damageReady();
	}
}
