package main;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.inventory.ItemStack;

public class EventManager implements Listener {
	private final Main plugin;
	private final DataManager pManager;
	
	public EventManager(Main main, DataManager permissionsManager) {
		plugin = main;
		pManager = permissionsManager;
	}
	
	@EventHandler
	private void onCraft(CraftItemEvent event) {
		Player player = (Player) event.getWhoClicked();
		Material item = event.getRecipe().getResult().getType();
		if (pManager.check(player, item) == false) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	private void onItemDrop(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player == false) {
			EntityDamageEvent cause = event.getEntity().getLastDamageCause();			
			if (cause instanceof EntityDamageByEntityEvent) {
				Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
				if (damager instanceof Player) {
					for (ItemStack item : event.getDrops()) {
						if (pManager.check((Player) damager, item.getType()) == false) {
							event.getDrops().remove(item);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	private void onItemUse(PlayerAnimationEvent event) {
		if (event.getAnimationType().equals(PlayerAnimationType.ARM_SWING)) {
			Player player = event.getPlayer();
			Material item = player.getItemInHand().getType();
			if (pManager.check(player, item) == false) {
				event.setCancelled(true);
			}
		}
	}

}
