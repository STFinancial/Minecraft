package wizard;

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;

public class FrostMage extends Wizzard {

	public FrostMage(Player player, int level, int exp) {
		super(player, level, exp);
	}

	@Override
	public void primaryAttack() {
		player.launchProjectile(Snowball.class);	
	}

	@Override
	public void specialAttack() {
		new Blizzard(player);		
	}
}
