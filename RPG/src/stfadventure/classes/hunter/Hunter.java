package stfadventure.classes.hunter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import stfadventure.classes.AdventureClass;
import stfadventure.classes.Resource.ResourceType;
import stfadventure.item.ArmorType;
import stfadventure.item.Weapon;
import stfadventure.main.Main;

public class Hunter extends AdventureClass {

	public Hunter(Main plugin, Player player) {
		super(plugin, player);
	}

	@Override
	protected void initializeResource(Main plugin) {
		resource.setType(ResourceType.ESSENCE, 20, 20, 1);
	}

	@Override
	public void getSkill(Event event) {
		switch (event.getEventName()) {
		case "PlayerInteractEvent":
			summonWolf((PlayerInteractEvent) event);
			break;
		case "EntityDamageByEntityEvent":
			bowKnockBack((EntityDamageByEntityEvent) event);
			headshot((EntityDamageByEntityEvent) event);
			break;
		}
	}

	private void headshot(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Arrow){ 
			Arrow arrow = (Arrow) event.getDamager();
			LivingEntity entity = (LivingEntity) event.getEntity();
			if (arrow.getLocation().getY() >= entity.getLocation().getY() + entity.getEyeHeight()) {
				PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 60, 4);
				entity.addPotionEffect(blindness);
				event.setDamage(event.getDamage() * 1.5);
			}
		}
	}

	private void bowKnockBack(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			if (Weapon.match(player, Weapon.BOW)) {
				Vector direction = player.getLocation().getDirection().multiply(5);
				event.getEntity().setVelocity(direction);
			}
		}
	}

	private void summonWolf(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (Weapon.match(player, Weapon.BOW)) {
				ItemStack item = event.getPlayer().getItemInHand();
				item.setAmount(item.getAmount() - 1);
				Location location = player.getLocation();
				if (event.getClickedBlock().getRelative(0, 1, 0).getType().equals(Material.AIR)) {
					location = event.getClickedBlock().getRelative(0, 1, 0).getLocation();
				}
				Wolf wolf = (Wolf) event.getPlayer().getWorld().spawnEntity(location, EntityType.WOLF);
				wolf.setAdult();
				wolf.setOwner(event.getPlayer());
			}
		}
	}

	@Override
	public void setArmorTypes() {
		armorTypes.add(ArmorType.LEATHER);
	}
}
