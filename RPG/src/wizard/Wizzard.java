package wizard;

import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;

import util.Util.Weapon;
import main.GamePlayer;

public class Wizzard extends GamePlayer {

	public Wizzard(Player player, int level, int exp) {
		super(player, level, exp);
	}

	@Override
	public void primaryAttack(Weapon weapon) {
		if (weapon.equals(Weapon.HOE)) {
			player.launchProjectile(SmallFireball.class);
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
}
