package stfadventure.classes.wizard.pyromancer;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import stfadventure.classes.wizard.Wizard;
import stfadventure.main.Main;

public class Pyromancer extends Wizard {
	public Pyromancer(Main plugin, Player player, YamlConfiguration playerConfig) {
		super(plugin, player, playerConfig);
		// TODO Auto-generated constructor stub
	}

	private Apocalypse apocalypse;

//	@Override
//	public void primaryAttack(Weapon weapon) {
//		if (weapon.equals(Weapon.HOE)) {
//			player.setNoDamageTicks(player.getMaximumNoDamageTicks());
//			player.setLastDamage(Integer.MAX_VALUE);
//			LargeFireball fireball = player.launchProjectile(LargeFireball.class);
//			fireball.setYield(0);
//			player.setNoDamageTicks(0);
//		}
//	}
//
//	@Override
//	public void specialAttack(Weapon weapon) {
//		if (weapon.equals(Weapon.HOE)) {
//			if (apocalypse != null) {
//				apocalypse.cancel();
//			}
//			apocalypse = new Apocalypse(player); 
//			apocalypse.runTaskTimer(STFAdventure.plugin, 0, 60);
//		}
//	}
//
//	@Override
//	public void primaryAttackEffect(EntityDamageByEntityEvent event) {
////		event.getEntity().setFireTicks(level);
//	}
}
