package stfadventure.monk;

import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import stfadventure.wizard.Wizard;
import util.Weapon;

public class Necromancer extends Wizard {

	public Necromancer(Player player, int level, int exp) {
	}
	
	@Override
	public void primaryAttack(Weapon weapon) {
		if (weapon.equals(Weapon.SPADE)) {
			player.setNoDamageTicks(player.getMaximumNoDamageTicks());
			player.setLastDamage(Integer.MAX_VALUE);
			WitherSkull skull = player.launchProjectile(WitherSkull.class);
			skull.setYield(0);
			player.setNoDamageTicks(0);
		}
	}
	
	@Override
	public void secondaryAttack(Weapon weapon) {
		if (weapon.equals(Weapon.SPADE)) {
			player.launchProjectile(EnderPearl.class);	
		}
	}
	
	@Override
	public void primaryAttackEffect(EntityDamageByEntityEvent event) {
		((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40, 2));
	}	
}
