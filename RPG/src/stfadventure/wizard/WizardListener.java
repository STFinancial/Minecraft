package stfadventure.wizard;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import stfadventure.events.EventManager;
import stfadventure.main.RPGClass;
import util.Weapon;

public class WizardListener implements Listener {

	@EventHandler
	private void mageAttackDamage(EntityDamageByEntityEvent event) {
		switch (event.getDamager().getType()) {
		case SNOWBALL:
		case FIREBALL:
		case SMALL_FIREBALL:
		case WITHER_SKULL:
			Projectile missile = (Projectile) event.getDamager();
			if (missile.getShooter() instanceof Player && missile.getShooter().equals(event.getEntity()) == false) {
				Player player = (Player) missile.getShooter();
				RPGClass shooter = EventManager.getPlayer(player);
				if (shooter instanceof Wizard) {
					event.setDamage(1);
					((Wizard) shooter).primaryAttackEffect(event);
				}
			}
			break;
		default:
			break;
		}
	}

	@EventHandler
	private void protectBlocksFromFire(BlockIgniteEvent event) {
		if (event.getIgnitingEntity().getType().equals(EntityType.SMALL_FIREBALL)) {
			Projectile missile = (Projectile) event.getIgnitingEntity();
			if (missile.getShooter() instanceof Player) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void preventWitherSkullExplosion(EntityExplodeEvent event) {
		if (event.getEntityType().equals(EntityType.WITHER_SKULL)) {
			WitherSkull skull = (WitherSkull) event.getEntity();
			if (skull.getShooter() instanceof Player) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void mageProtections(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			RPGClass rpgClass = EventManager.getPlayer(player);
			switch (event.getCause()) {
			case LAVA:
			case FIRE:
			case FIRE_TICK:
				if (rpgClass instanceof BlazeMage) {
					event.setCancelled(true);
				}
				break;
			case THORNS:
			case DROWNING:
				if (rpgClass instanceof FrostMage) {
					event.setCancelled(true);
				}
				break;
			case WITHER:
			case POISON:
				if (rpgClass instanceof Necromancer) {
					event.setCancelled(true);
				}
				break;
			default:
				break;
			}
		}
	}

	@EventHandler
	private void magePrimaryAttack(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.LEFT_CLICK_AIR)) {
			EventManager.getPlayer(event.getPlayer()).primaryAttack(Weapon.getWeapon(event.getPlayer()));
		}
	}

	@EventHandler
	private static void mageSecondaryAndSpecialAttack(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			Player player = event.getPlayer();
			if (player.isSneaking()) {
				EventManager.getPlayer(player).specialAttack(Weapon.getWeapon(player));
			}	
			else {

				EventManager.getPlayer(player).secondaryAttack(Weapon.getWeapon(player));
			}
		}
	}
}
