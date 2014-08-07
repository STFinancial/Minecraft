package wizard;

import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import util.Weapon;
import main.Main;

public class Pyromancer extends Wizard {
	private Apocalypse apocalypse;

	public Pyromancer(Player player, int level, int exp) {
		super(player, level, exp);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void primaryAttack(Weapon weapon) {
		if (weapon.equals(Weapon.HOE)) {
			player.setNoDamageTicks(player.getMaximumNoDamageTicks());
			player.setLastDamage(Integer.MAX_VALUE);
			LargeFireball fireball = player.launchProjectile(LargeFireball.class);
			fireball.setYield(0);
			player.setNoDamageTicks(0);
		}
	}

	@Override
	public void specialAttack(Weapon weapon) {
		if (weapon.equals(Weapon.HOE)) {
			if (apocalypse != null) {
				apocalypse.cancel();
			}
			apocalypse = new Apocalypse(player); 
			apocalypse.runTaskTimer(Main.plugin, 0, 60);
		}
	}

	@Override
	public void primaryAttackEffect(EntityDamageByEntityEvent event) {
//		event.getEntity().setFireTicks(level);
	}
}
