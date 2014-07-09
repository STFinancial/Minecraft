package main;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DataManager {
	Plugin plugin;
	private HashMap<UUID, PlayerData> playerData;
	
	public DataManager (Plugin plugin){
		this.plugin = plugin;
		playerData = new HashMap<UUID, PlayerData>();
	}

	public void remove(Player player) {
		playerData.remove(player.getUniqueId());
	}

	public void add(Player player) {
		playerData.put(player.getUniqueId(), new PlayerData(plugin));
	}
	
	public boolean canPlayerSwing(UUID playerID){
		return playerData.get(playerID).swingReady();
	}
	
	public void swing(UUID playerID){
		playerData.get(playerID).swingPerformed();
	}
	
	public int currentEnergy(UUID playerID){
		return playerData.get(playerID).energy;
	}
}
