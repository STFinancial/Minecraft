package main;

import org.bukkit.entity.Player;

public abstract class GamePlayer {
	protected int level = 1, exp = 0, expNext = level * 5;
	protected final Player player;
	
	public GamePlayer(Player player, int level, int exp) {
		this.player = player;
		this.level = level;
		this.exp = exp;
	}
	
	public abstract void primaryAttack();
	
	public abstract void secondaryAttack();
	
	public abstract void specialAttack();
}
