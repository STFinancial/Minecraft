package stfarena.main;

import javax.sound.midi.Receiver;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;

import stfarena.main.ArenaPlayer.Status;

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

//		public static boolean isHostile(String potionEffectType) {
//			switch (potionEffectType) {
//			case "HARM":
//			case "POISON":
//			case "SLOW":
//			case "WEAKNESS":
//				return true;
//			default:
//				return false;
//			}
//		}	
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

//	@EventHandler
//	private void preventFriendlyFire(EntityDamageByEntityEvent event) {
//		if (inArenaWorld(event.getEntity())) {
//			if (event.getEntity() instanceof Player) {
//				Player target = (Player) event.getEntity();
//				if (event.getDamager() instanceof Projectile) {
//					Projectile missile = (Projectile) event.getDamager();
//					if (missile.getShooter() instanceof Player) {
//						if (dataManager.areAllies((Player) missile.getShooter(), target)) {
//							event.setCancelled(true);
//						}
//					}
//				}
//				else if (event.getDamager() instanceof Player) {
//					if (dataManager.areAllies((Player) event.getDamager(), target)) {
//						event.setCancelled(true);
//					}
//				}
//			}
//		}
//	}

	@EventHandler
	private void friendlyPotionEffect(PotionSplashEvent event) {
		if (inArenaWorld(event.getEntity())) {
			PotionEffect potionEffect = event.getPotion().getEffects().iterator().next();
			Player thrower = (Player) event.getPotion().getShooter();
			if (ArenaPotion.isFriendly(potionEffect.getType().getName())) {
				for (LivingEntity entity : event.getAffectedEntities()) {
					if (entity instanceof Player) { 
						Player reciever = (Player) event.getEntity();
						if (thrower.getScoreboard().getPlayerTeam(thrower).hasPlayer(reciever) == false) {
							event.setIntensity(entity, 0);
						}
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerRespawn(PlayerRespawnEvent event) {
		if (dataManager.getPlayer(event.getPlayer()).getStatus().equals(Status.IN_GAME)) {
			dataManager.getPlayer(event.getPlayer()).setStatus(Status.FREE);
			dataManager.getPlayer(event.getPlayer()).setFocus(null);
			dataManager.getPlayer(event.getPlayer()).setArena(null);
			event.setRespawnLocation(dataManager.getPlayer(event.getPlayer()).getLocation());
			dataManager.getPlayer(event.getPlayer()).loadState(event.getPlayer());
			event.getPlayer().sendMessage("You have respawned and left the arena");
		}
	}

	@EventHandler
	private void blockPlaceEvent(BlockPlaceEvent event) {
		if (inArenaWorld(event.getPlayer())) {
			if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE) == false) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void onPlayerDeath(PlayerDeathEvent event) {
		if (plugin.getDataManager().getPlayer(event.getEntity().getUniqueId()).getStatus().equals(Status.IN_GAME)) {
			plugin.getMatchManager().recordDeath((Player) event.getEntity());
		}
	}

	@EventHandler
	private void preventBlockBreak(BlockBreakEvent event) {
		if (inArenaWorld(event.getPlayer())) {
			if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE) == false) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	private void preventPets(EntityTeleportEvent event) {
		if (event.getTo().getWorld().equals(Bukkit.getWorld("Arena"))) {
			if (event.getEntity() instanceof Wolf || event.getEntity() instanceof Ocelot) {
				Tameable pet = (Tameable) event.getEntity();
				Player player = Bukkit.getPlayer(pet.getOwner().getUniqueId());
				Location location = event.getFrom();
				if (player.getBedSpawnLocation() != null) {
					location = player.getBedSpawnLocation();
				}
				event.getEntity().teleport(location);
				if (pet instanceof Wolf) {
					((Wolf) pet).setSitting(true);
				}
				else if (pet instanceof Ocelot) {
					((Ocelot) pet).setSitting(true);
				}			
			}
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
