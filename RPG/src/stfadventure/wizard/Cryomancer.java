package stfadventure.wizard;

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.plugin.java.JavaPlugin;

import stfadventure.classes.AdventureClassType;
import stfadventure.events.AdventureEvent;

public class Cryomancer extends Wizard {
	private final Blizzard blizzard;

	public Cryomancer(JavaPlugin plugin, Player player, int level, int exp) {
		super(plugin, player, level, exp);
		blizzard = new Blizzard(plugin, player);
	}
	
	@Override
	public AdventureClassType getType() {
		return AdventureClassType.CRYOMANCER;
	}

	@Override
	public void primaryAttack(AdventureEvent event) {
		if (resource.subtractAmount(10)) {
			player.setNoDamageTicks(player.getMaximumNoDamageTicks());
			player.setLastDamage(Integer.MAX_VALUE);
			Snowball snowball = player.launchProjectile(Snowball.class);
			snowball.setShooter(player);
			player.setNoDamageTicks(0);
			resource.start(0, 20);
		}
		else {
			player.sendMessage("Not enough mana!");
		}
	}
	
	@Override
	public void specialAttack(AdventureEvent event) {
		if (resource.subtractAmount(50)) {
			blizzard.start();
			resource.start(0, 20);
		}
		else {
			player.sendMessage("Not enough mana!");
		}
	}
}
