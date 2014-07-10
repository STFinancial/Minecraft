package main;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventManager implements Listener {
	private final Main plugin;
	private final DataManager dataManager;
	
	public EventManager(Main plugin, DataManager dataManager) {
		this.plugin = plugin;
		this.dataManager = dataManager;
	}
	
	private boolean holdingSword(Player player) {
		return player.getItemInHand().getType().name().contains("_SWORD");
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
	private void onPlayerAnimation(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			if (holdingSword(damager)) {
				plugin.getLogger().info(damager.getName() + " has just tried to damage");
				if (dataManager.canPlayerDamage(damager)) {
					plugin.getLogger().info("Damage was accepted");
				} else {
					plugin.getLogger().info("Damage was prevented due to energy losses");
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void onPlayerAnimation(PlayerAnimationEvent event) {
		if (holdingSword(event.getPlayer())) {
			Player player = event.getPlayer();
			plugin.getLogger().info(event.getPlayer().getName() + " Has just tried to swing with " + dataManager.currentEnergy(player) + " energy");
			if (dataManager.canPlayerSwing(player)) {
				dataManager.swing(player);
				plugin.getLogger().info("And it was good, now at " + dataManager.currentEnergy(player) + " energy");
			} else {
				plugin.getLogger().info("Not enough energy");
				event.setCancelled(true);
			}
		}
	}
}
