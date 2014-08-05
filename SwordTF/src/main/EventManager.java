package main;

import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
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
	private final DataManager dataManager;

	public EventManager(SwordMain plugin, DataManager dataManager) {
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
		if(event.isCancelled() == false){
			if (event.getDamager() instanceof Player) {
				Player damager = (Player) event.getDamager();
				if (holdingSword(damager)) {
					if (dataManager.canPlayerDamage(damager)) {
					} else {
						event.setCancelled(true);
					}
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
		

		if(event.isCancelled() == false && event.getDamager() instanceof Arrow){
			Arrow arrow = (Arrow) event.getDamager();
			if(event.getEntity() instanceof Player && arrow.getShooter() instanceof Player){
				
				if(arrow.getLocation().getY() > (event.getEntity().getLocation().getY() + 1.6)){
					event.setDamage(event.getDamage() * 1.5);
				}else{
					event.setDamage(event.getDamage() * 1.25);
				}
			}
		}
		
	}

	@EventHandler
	private void onPlayerAnimation(PlayerAnimationEvent event) {
		if (holdingSword(event.getPlayer())) {
			Player player = event.getPlayer();
			if (dataManager.canPlayerSwing(player)) {
				dataManager.swing(player);
			} else {
				event.setCancelled(true);
			}
		}
	}
}
