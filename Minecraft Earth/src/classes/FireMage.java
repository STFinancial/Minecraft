package classes;

import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.player.PlayerAnimationEvent;

public class FireMage extends Class {
	
	public FireMage(Player player) {
		super(player);
	}
	
	public void playClassAnimation(PlayerAnimationEvent event) {
		Player player = event.getPlayer();
		if (ClassUtil.usingStaff(player)) {
			if (level < 50) {
				event.getPlayer().launchProjectile(SmallFireball.class);
			}
			if (level >= 50) {
				event.getPlayer().launchProjectile(LargeFireball.class);
			}
		}
	}	
}
