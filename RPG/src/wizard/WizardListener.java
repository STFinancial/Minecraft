package wizard;

import main.RPGClass;

import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.Openable;
import org.bukkit.material.Redstone;

import util.Weapon;
import combat.EventManager;

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
					event.setDamage(shooter.getLevel());
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

	private static boolean isInteractable(Block block) {
		if (block == null) {
			return false;
		}
		if (block.getState().getData() instanceof Redstone) {
			return true;
		}
		if (block.getState().getData() instanceof Openable) {
			return true;
		}
		if (block.getState().getData() instanceof Hanging) {
			return true;
		}
		if (block.getState() instanceof InventoryHolder) {
			return true;
		}
		switch (block.getType()) {
		case DIRT:
		case GRASS:
		case SOIL:
		case BED:
		case SIGN:
		case WOODEN_DOOR:
		case IRON_DOOR:
		case WORKBENCH:
			return true;
		default:
			return false;			
		}
	}

	private static boolean isInstantBreak(Block block){
		if (block == null) {
			return true;
		}
		switch (block.getType()) {
		case CROPS:
		case PUMPKIN_STEM:
		case CARROT:
		case POTATO:
		case LONG_GRASS:
		case DEAD_BUSH:
		case FLOWER_POT:
		case MELON_STEM:
		case RED_MUSHROOM:
		case BROWN_MUSHROOM:
		case REDSTONE_COMPARATOR_OFF:
		case REDSTONE_COMPARATOR_ON:
		case TRIPWIRE:
		case TRIPWIRE_HOOK:
		case TORCH:
		case TNT:
		case SUGAR_CANE_BLOCK:
		case SAPLING:
		case REDSTONE_WIRE:
		case REDSTONE_TORCH_OFF:
		case REDSTONE_TORCH_ON:
		case NETHER_WARTS:
		case DIODE_BLOCK_OFF:
		case DIODE_BLOCK_ON:
		case YELLOW_FLOWER:
		case RED_ROSE:
		case DOUBLE_PLANT:
			return true;
		default:
			return false;
		}
	}
}
