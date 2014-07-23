package classes;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;

public class Ranger extends Class {
	
	public Ranger(Player player) {
		super(player);
	}
	
	public void knockback() {
		
	}
	
	public void blind() {
		
	}
	
	public void freeze() {
		
	}

	@Override
	public void playClassAnimation(PlayerAnimationEvent event) {}
}
