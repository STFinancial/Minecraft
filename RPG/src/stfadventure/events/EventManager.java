package stfadventure.events;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import stfadventure.main.managers.PlayerManager;

public class EventManager implements Listener {
	
	private void passEvent(Player player, Event event) {
		if (player.hasPermission("STFAdventure")) {
			PlayerManager.getAdventureClass(player).getSkill(event);
		}
	}
	
//	@EventHandler
//	private void playerJoinEvent(PlayerJoinEvent event) {
//		passEvent(event.getPlayer(), event);
//	}
	
	@EventHandler
	private void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			passEvent(player, event);
		}
		else if (event.getDamager() instanceof Projectile) {
			Player player = (Player) ((Projectile) event.getDamager()).getShooter();
			passEvent(player, event);
		}
	}
	
	@EventHandler
	private void playerInteractEvent(PlayerInteractEvent event) {
		passEvent(event.getPlayer(), event);
	}
	
	@EventHandler
	private void blockBreakEvent(BlockBreakEvent event) {
		if (event.getPlayer().hasPermission("STFAdventure") && event.getPlayer().isOp() == false) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	private void playerInteractEntityEvent(PlayerInteractEntityEvent event) {
		passEvent(event.getPlayer(), event);
	}
	
	@EventHandler
	private void entityDamageEvent(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			passEvent((Player) event.getEntity(), event);
		}
	}
	
	@EventHandler
	private void inventoryCloseEvent(InventoryCloseEvent event) {
		if (event.getInventory().getType().equals(InventoryType.CRAFTING)) {
			if (event.getPlayer().hasPermission("STFAdventure")) {
				PlayerManager.getAdventureClass(event.getPlayer()).checkArmor();
			}
		}
	}
}
