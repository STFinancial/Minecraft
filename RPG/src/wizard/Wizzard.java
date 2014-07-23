package wizard;

import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;

import main.GamePlayer;

public class Wizzard extends GamePlayer {

	public Wizzard(Player player, int level, int exp) {
		super(player, level, exp);
	}

	@Override
	public void primaryAttack() {
		player.launchProjectile(SmallFireball.class);
	}

	@Override
	public void secondaryAttack() {
		player.launchProjectile(EnderPearl.class);		
	}

	@Override
	public void specialAttack() {
		player.sendMessage("You must specialize before you get a special attack!");		
	}
}
