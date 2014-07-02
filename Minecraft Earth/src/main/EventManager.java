package main;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class EventManager implements Listener {
	private final Main plugin;
	private final PermissionsManager pManager;
	
	public EventManager(Main main, PermissionsManager permissionsManager) {
		plugin = main;
		pManager = permissionsManager;
	}
	
	@EventHandler
	private void onCraft(CraftItemEvent event) {
		pManager.check(event.getWhoClicked(), event.getRecipe().getResult().getType());
	}
	
	@EventHandler
	private void onDrop(EntityDeathEvent event) {
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

}
