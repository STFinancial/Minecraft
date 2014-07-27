package main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventManager implements Listener {
	private final Main plugin;
	private final DataManager dataManager;

	public EventManager(Main plugin, DataManager dataManager) {
		this.plugin = plugin;
		this.dataManager = dataManager;
	}

	@EventHandler
	private void onLogin(PlayerJoinEvent event) {
		dataManager.add(event.getPlayer());
	}

	@EventHandler
	private void onQuit(PlayerQuitEvent event) {
		dataManager.remove(event.getPlayer());
	}

	@EventHandler
	private void onInteractWithEntity(PlayerInteractEntityEvent event) {

	}

	@EventHandler
	private void onInteract(PlayerInteractEvent event) {

	}

	@EventHandler
	private void onDamage(EntityDamageByEntityEvent event) {

	}

	@EventHandler
	private void onPlayerAnimation(PlayerAnimationEvent event) {

	}

	public void quit() {
		PlayerInteractEvent.getHandlerList().unregister(plugin);
		PlayerAnimationEvent.getHandlerList().unregister(plugin);
		PlayerInteractEntityEvent.getHandlerList().unregister(plugin);
		EntityDamageByEntityEvent.getHandlerList().unregister(plugin);
	}
}
