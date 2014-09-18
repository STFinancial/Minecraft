package stfadventure.classes.wizard.cryomancer;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;

import stfadventure.classes.wizard.Wizard;
import stfadventure.main.Main;

public class Cryomancer extends Wizard {
	private final Blizzard blizzard;

	public Cryomancer(Main plugin, Player player, YamlConfiguration playerConfig) {
		super(plugin, player);
		blizzard = new Blizzard(plugin, player);
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
