package wizard;

import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import util.Weapon;
import main.RPGClass;

public class Wizard extends RPGClass {

	public Wizard(Player player, int level, int exp) {
		super(player, level, exp);
	}

	@Override
	public void primaryAttack(Weapon weapon) {
		if (weapon.equals(Weapon.HOE)) {
			player.setNoDamageTicks(player.getMaximumNoDamageTicks());
			player.setLastDamage(Integer.MAX_VALUE);
			SmallFireball fireball = player.launchProjectile(SmallFireball.class);
			fireball.setShooter(player);
			player.setNoDamageTicks(0);
		}
	}

	@Override
	public void secondaryAttack(Weapon weapon) {
		if (weapon.equals(Weapon.HOE)) {
			player.launchProjectile(EnderPearl.class);	
		}
	}

	@Override
	public void specialAttack(Weapon weapon) {
		if (weapon.equals(Weapon.HOE)) {
			player.sendMessage("You must specialize before you get a special attack!");		
		}
	}

	public void primaryAttackEffect(EntityDamageByEntityEvent event) {}
}
