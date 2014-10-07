package stfadventure.classes.wizard.cryomancer;

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

import stfadventure.classes.wizard.Wizard;
import stfadventure.main.Main;

public class Cryomancer extends Wizard {
	private final Blizzard blizzard;

	public Cryomancer(Main plugin, Player player) {
		super(plugin, player);
		blizzard = new Blizzard(plugin, player);
	}

	public void getSkill(Event event) {
		super.getSkill(event);
		switch (event.getEventName()) {
		case "PlayerInteractEvent":
			snowball((PlayerInteractEvent) event);
			blizzard((PlayerInteractEvent) event);
			break;
		}
		
	}
	
	private void snowball(PlayerInteractEvent event) {
			player.setNoDamageTicks(player.getMaximumNoDamageTicks());
			player.setLastDamage(Integer.MAX_VALUE);
			Snowball snowball = player.launchProjectile(Snowball.class);
			snowball.setShooter(player);
			player.setNoDamageTicks(0);
			resource.start(0, 20);
	}
	
	private void blizzard(PlayerInteractEvent event) {
		blizzard.start();
	}
}
