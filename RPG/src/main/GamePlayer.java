package main;

import org.bukkit.entity.Player;

import util.Util;

public abstract class GamePlayer {
	protected int level = 1, exp = 0, expNext = level * 5;
	protected final Player player;
	
	public GamePlayer(Player player, int level, int exp) {
		this.player = player;
		this.level = level;
		this.exp = exp;
	}
	
	public abstract void primaryAttack(Util.Weapon weapon);
	
	public abstract void secondaryAttack(Util.Weapon weapon);
	
	public abstract void specialAttack(Util.Weapon weapon);
}
