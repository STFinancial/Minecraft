package main;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DataManager {
	private final SwordMain plugin;
	private HashMap<Player, SwordData> swordData;

	public DataManager(SwordMain plugin) {
		this.plugin = plugin;
		swordData = new HashMap<Player, SwordData>();
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

	public boolean canPlayerBlock(Player player) {
		return swordData.get(player).blockReady();
	}

	public void block(Player player) {
		swordData.get(player).blockPerformed();
	}
	
	public void criticalBlock(Player player){
		swordData.get(player).criticalBlock();
	}
	
	public int currentEnergy(Player player) {
		return swordData.get(player).getEnergy();
	}

	public boolean canPlayerDamage(Player player) {
		return swordData.get(player).damageReady();
	}

	public boolean canCriticalBlock(Player player) {
		return swordData.get(player).criticalBlock;
	}

	public void criticalBlocked(Player player) {
		energyChange(player, 10);
		swordData.get(player).criticalBlock = false;
	}
	
	public void energyChange(Player player, int amount){
		swordData.get(player).changeEnergy(amount);
	}
}
