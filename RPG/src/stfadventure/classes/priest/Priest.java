package stfadventure.classes.priest;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Villager;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import stfadventure.classes.AdventureClass;
import stfadventure.classes.Resource.ResourceType;
import stfadventure.item.ArmorType;
import stfadventure.item.Weapon;
import stfadventure.main.Main;

public class Priest extends AdventureClass {

	public Priest(Main plugin, Player player) {
		super(plugin, player);
	}

	@Override
	protected void initializeResource(Main plugin) {
		resource.setType(ResourceType.HOLY_ENERGY, 100, 100, 10);		
	}

	@Override
	public void getSkill(Event event) {
		switch (event.getEventName()) {
		case "EntityDamageEvent":
			holyLight((EntityDamageEvent) event);
			break;
		case "EntityDamageByEntityEvent":
			cleanse((EntityDamageByEntityEvent) event);
		case "PlayerInteractEntityEvent":
			holySmite((PlayerInteractEntityEvent) event);
			break;
		case "PlayerInteractEvent":
			divineProtection((PlayerInteractEvent) event);
			break;
		default:
			break;
		}

	}
	
	private void holyLight(EntityDamageEvent event) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 1));
	}
	
	private void cleanse(EntityDamageByEntityEvent event) {
		if (Weapon.match(player, Weapon.PICKAXE) && event.getEntity() instanceof LivingEntity) {
			((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 1));
		}
	}
	
	private void divinePower(LivingEntity entity, boolean heal) {
		if (heal) {
			entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 1, false));
		}
		else {
			player.getWorld().strikeLightningEffect(entity.getLocation());
			entity.damage(50);
		}
	}

	private void holySmite(PlayerInteractEntityEvent event) {
		if (Weapon.match(player, Weapon.PICKAXE) && event.getRightClicked() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getRightClicked();
			if (entity instanceof Tameable) {
				Tameable animal = (Tameable) entity;
				if (animal.getOwner().equals(player)) {
					divinePower(entity, true);
				}
			}
			if (entity instanceof Monster) {
				divinePower(entity, false);
			}
			if (entity instanceof Villager) {
				divinePower(entity, true);
			}
		}
	}
	
	private void divineProtection(PlayerInteractEvent event) {
		if (Weapon.match(player, Weapon.PICKAXE)) {
			if (event.getAction().equals(Action.RIGHT_CLICK_AIR) && player.isSneaking()) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 4));
				player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 4));
				for (Entity entity : player.getNearbyEntities(10, 5, 10)) {
					if (entity instanceof Player) {
						((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 4));
						((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 4));
					}
				}
			}
		}
	}

	@Override
	public void setArmorTypes() {
		armorTypes.add(ArmorType.IRON);
		armorTypes.add(ArmorType.GOLD);
		armorTypes.add(ArmorType.DIAMOND);
	}
}
