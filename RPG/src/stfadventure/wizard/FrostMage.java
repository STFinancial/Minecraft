package stfadventure.wizard;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import stfadventure.main.Main;
import util.Weapon;

public class FrostMage extends Wizard {
	private Blizzard blizzard;

	public FrostMage(Player player, int level, int exp) {
		super(player, level, exp);
	}

	@Override
	public void primaryAttack(Weapon weapon) {
		if (weapon.equals(Weapon.HOE)) {
			player.setNoDamageTicks(player.getMaximumNoDamageTicks());
			player.setLastDamage(Integer.MAX_VALUE);
			Snowball snowball = player.launchProjectile(Snowball.class);
			snowball.setShooter(player);
			player.setNoDamageTicks(0);
		}
	}

	@Override
	public void specialAttack(Weapon weapon) {
		if (weapon.equals(Weapon.HOE)) {
			if (blizzard != null) {
				blizzard.cancel();
			}
			blizzard = new Blizzard(player, level); 
			blizzard.runTaskTimer(Main.plugin, 0, 1);
		}
	}

	@Override
	public void primaryAttackEffect(EntityDamageByEntityEvent event) {
		((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 2));
	}
}
