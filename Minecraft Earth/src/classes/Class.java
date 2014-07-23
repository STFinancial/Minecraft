package classes;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;

public abstract class Class {
	protected int level = 0;
	protected int exp = 0;
	protected int nextExp;
	protected final Player player;
	
	public Class(Player player) {
		this.player = player;
	}
	
	public void addExp(int amount) {
		exp = exp + amount;
		if (exp >= nextExp && level < 100) {
			level = level + 1;
			exp = 0;
			nextExp = (int) Math.log(level); 
		}
	}
	
	public abstract void playClassAnimation(PlayerAnimationEvent event);

}
