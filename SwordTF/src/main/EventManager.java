package main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventManager implements Listener {
	private final Main plugin;
	
	public EventManager(Main main) {
		plugin = main;
	}
	
	@EventHandler
	private void onLogin(PlayerJoinEvent event) {
		loadPlayerData(event.getPlayer());
	}
	
	@EventHandler
	private void onQuit(PlayerQuitEvent event) {
		removePlayerData(event.getPlayer());
	}
	
	public void load() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			loadPlayerData(player);
		}
	}
	
	public void quit() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			removePlayerData(player);
		}
	}
	
	private void loadPlayerData(Player player) {
	}
	
	private void removePlayerData(Player player) {
	}
	
	
}
