package stfadventure.classes.hunter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;

import stfadventure.classes.AdventureClass;
import stfadventure.main.managers.PlayerManager;

public class HunterSkills implements Listener {
	
	@EventHandler
	private void speedPassive(PlayerJoinEvent event) {
		
	}
	
	@EventHandler
	private void bowKnockBack(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			AdventureClass playerClass = PlayerManager.getAdventureClass(player);
			if (player.hasPermission("STFAdventure")) {
				if (player.getItemInHand().getType().equals(Material.BOW)) {
					Vector direction = player.getLocation().getDirection().multiply(5);
					event.getEntity().setVelocity(direction);
				}
			}
		}
	}
	
	@EventHandler
	private void summonWolf(PlayerInteractEvent event) {
		if (event.getPlayer().hasPermission("STFAdventure")) {
			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				Location location = event.getClickedBlock().getRelative(0, 1, 0).getLocation();
				Wolf wolf = (Wolf) event.getPlayer().getWorld().spawnEntity(location, EntityType.WOLF);
				wolf.setAdult();
				wolf.setOwner(event.getPlayer());
			}
		}
	}
	
	@EventHandler
	private void wolfPassive(EntityDamageByEntityEvent event) {
		
	}

}
