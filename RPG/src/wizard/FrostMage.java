package wizard;

import main.Main;

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import util.Util.Weapon;

public class FrostMage extends Wizzard {
	private Blizzard blizzard;

	public FrostMage(Player player, int level, int exp) {
		super(player, level, exp);
	}

	@Override
	public void primaryAttack(Weapon weapon) {
		if (weapon.equals(Weapon.HOE)) {
			player.launchProjectile(Snowball.class);	
		}
	}

	@Override
	public void specialAttack(Weapon weapon) {
		if (weapon.equals(Weapon.HOE)) {
			if (blizzard != null) {
				blizzard.cancel();
			}
			blizzard = new Blizzard(player);
			blizzard.runTaskTimer(Main.plugin, 0, 1);
		}
	}
}
