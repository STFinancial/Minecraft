package main;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;

public class EventManager implements Listener {
	private static enum ArenaPotion {
		FIRE_RESISTANCE, HARM, HEAL, INCREASE_DAMAGE, INVISIBILITY, JUMP, NIGHT_VISION,
		POISON, REGENERATION, SLOW, SPEED, WATER_BREATHING, WEAKNESS;

		public static boolean isFriendly(String potionEffectType) {
			switch (potionEffectType) {
			case "FIRE_RESISTANCE":
			case "HEAL":
			case "INCREASE_DAMAGE":
			case "INVISIBILITY":
			case "JUMP":
			case "NIGHT_VISION":
			case "REGENERATION":
			case "SPEED":
			case "WATER_BREATHING":
				return true;
			default:
				return false;
			}
		}

		public static boolean isHostile(String potionEffectType) {
			switch (potionEffectType) {
			case "HARM":
			case "POISON":
			case "SLOW":
			case "WEAKNESS":
				return true;
			default:
				return false;
			}
		}	
	};
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

	@EventHandler
	private void preventFriendlyFire(EntityDamageByEntityEvent event) {
		//		if (inArenaWorld(event.getEntity())) {
		//			if (event.getEntity() instanceof Player) {
		//				Player player = (Player) event.getEntity();
		//				if (event.getCause())
		//			}
		//		}
	}

	@EventHandler
	private void friendlyPotionEffect(PotionSplashEvent event) {
		if (inArenaWorld(event.getEntity())) {
			PotionEffect potionEffect = event.getPotion().getEffects().iterator().next();
			for (LivingEntity entity : event.getAffectedEntities()) {
				if (ArenaPotion.isFriendly(potionEffect.getType().toString())) {
					if ()
				}
				else if (ArenaPotion.isHostile(potionEffect.getType().toString())) {

				}
			}			
		}
	}

	@EventHandler
	private void onPlayerRespawn(PlayerRespawnEvent event) {
		//WHAT TO DO!?
	}

	@EventHandler
	private void onPlayerDeath(PlayerDeathEvent event) {
		if (inArenaWorld(event.getEntity())) {
			//WHAT TO DO!?
		}
	}

	@EventHandler
	private void preventBlockBreak(BlockBreakEvent event) {
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE) == false) {
			event.setCancelled(true);
		}
	}

	private boolean inArenaWorld(Entity entity) {
		return entity.getLocation().getWorld().equals(Bukkit.getWorld("Arena"));
	}

	public void quit() {
		PlayerInteractEvent.getHandlerList().unregister(plugin);
		PlayerAnimationEvent.getHandlerList().unregister(plugin);
		PlayerInteractEntityEvent.getHandlerList().unregister(plugin);
		EntityDamageByEntityEvent.getHandlerList().unregister(plugin);
	}
}
