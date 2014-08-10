package stfarena.match;

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
import org.bukkit.event.HandlerList;
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

import stfarena.main.Main;
import stfarena.main.TeamCreator;

public class MatchListener implements Listener {
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
	
	private final Match match;

	public MatchListener(Match match) {
		this.match = match;
	}

	@EventHandler
	private void playerDeath(PlayerDeathEvent event) {
		match.recordDeath(event.getEntity());	
	}
	
	@EventHandler
	private void playerQuit(PlayerQuitEvent event) {
		match.recordDeath(event.getPlayer());
	}

	@EventHandler
	private void friendlyPotionEffect(PotionSplashEvent event) {
		if (inArenaWorld(event.getEntity())) {
			PotionEffect potionEffect = event.getPotion().getEffects().iterator().next();
			Player thrower = (Player) event.getPotion().getShooter();
			for (LivingEntity entity : event.getAffectedEntities()) {
				if (ArenaPotion.isFriendly(potionEffect.getType().toString())) {
					if (dataManager.areAllies(thrower, (Player) entity) == false) {
						event.setIntensity(entity, 0);
					}
				}
			}			
		}
	}

	@EventHandler
	private void onPlayerRespawn(PlayerRespawnEvent event) {
		if (dataManager.getPlayer(event.getPlayer()).getStatus() == Status.IN_GAME) {
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
		if (inArenaWorld(event.getEntity())) {
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
}
