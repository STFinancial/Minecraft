package main;

import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
	private void onInteractWithEntity(PlayerInteractEntityEvent event){
		if(holdingSword(event.getPlayer())){
			if (dataManager.canPlayerBlock(event.getPlayer())) {
				dataManager.criticalBlock(event.getPlayer());
			}
		}
	}

	@EventHandler
	private void onInteract(PlayerInteractEvent event){
		if(holdingSword(event.getPlayer())){
			if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
				if (dataManager.canPlayerBlock(event.getPlayer())) {
					dataManager.block(event.getPlayer());
				} else {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void onDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			if (holdingSword(damager)) {
				if (dataManager.canPlayerDamage(damager)) {
				} else {
					event.setCancelled(true);
				}
			}
		}

		if(event.isCancelled() == false && event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			if(player.isBlocking()){
				if(dataManager.canCriticalBlock(player)){
					if(event.getEntityType() != EntityType.CREEPER && event.getEntityType() != EntityType.FIREBALL && event.getEntityType() != EntityType.SPLASH_POTION 
							&& event.getEntityType() != EntityType.PRIMED_TNT && event.getEntityType() != EntityType.FALLING_BLOCK ){
						dataManager.criticalBlocked(player);
						//plugin.getLogger().info("Critical BLOCK!!!!!!!!!!!!!!");
						//player.sendMessage("Blocked!");
						event.setCancelled(true);
						if(event.getDamager() instanceof Player){
							((Player) event.getDamager()).playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1.0f+(float)Math.random());
							dataManager.energyChange((Player) event.getDamager(), -10);
						}
						player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1.0f+(float)Math.random());
					}
				}

			}
		}
	}

	@EventHandler
	private void onPlayerAnimation(PlayerAnimationEvent event) {
		if (holdingSword(event.getPlayer())) {
			Player player = event.getPlayer();
			//plugin.getLogger().info(event.getPlayer().getName() + " Has just tried to swing with " + dataManager.currentEnergy(player) + " energy");
			if (dataManager.canPlayerSwing(player)) {
				dataManager.swing(player);
				//plugin.getLogger().info("And it was good, now at " + dataManager.currentEnergy(player) + " energy");
			} else {
				//plugin.getLogger().info("Not enough energy");
				event.setCancelled(true);
			}
		}
	}
}
