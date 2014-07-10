package main;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
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

	@EventHandler
	private void onPlayerAnimation(EntityDamageByEntityEvent event) {
			if (event.getDamager() instanceof Player) {
				Player damager = (Player) event.getDamager();
				if (Main.SWORDS.contains(damager.getItemInHand().getType())) {
					plugin.getLogger().info(damager.getName() + " has just tried to damage");
					if(plugin.getDataManager().canPlayerDamage(damager.getUniqueId())){
						plugin.getLogger().info("Damage was accepted"); 
					}else{
						plugin.getLogger().info("Damage was prevented due to energy losses"); 
						event.setCancelled(true);
					}
				}
			}
	}

	@EventHandler
	private void onPlayerAnimation(PlayerAnimationEvent event) {
		UUID playerID = event.getPlayer().getUniqueId();
		if (Main.SWORDS.contains(event.getPlayer().getItemInHand().getType())) {
			plugin.getLogger().info(
					event.getPlayer().getName()
							+ " Has just tried to swing with "
							+ plugin.getDataManager().currentEnergy(playerID)
							+ " energy");

			if (plugin.getDataManager().canPlayerSwing(playerID)) {
				plugin.getDataManager().swing(playerID);
				plugin.getLogger().info(
						"And it was good, now at "
								+ plugin.getDataManager().currentEnergy(
										playerID) + " energy");
			} else {
				plugin.getLogger().info("Not enough energy"); 
				event.setCancelled(true);
			}
		}
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
		plugin.getDataManager().add(player);
	}

	private void removePlayerData(Player player) {
		plugin.getDataManager().remove(player);
	}

}
