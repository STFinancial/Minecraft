package main;

import java.awt.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import util.Weapon;

public abstract class RPGClass {
	protected int level = 1, exp = 0, expNext = level * 5;
	protected final Player player;
	
	public RPGClass(Player player, int level, int exp) {
		this.player = player;
		this.level = level;
		this.exp = exp;
	}
	
	public int getLevel() {
		return level;
	}
	
	public abstract void primaryAttack(Weapon weapon);
	
	public abstract void secondaryAttack(Weapon weapon);
	
	public abstract void specialAttack(Weapon weapon);
	
//	public abstract void primaryAttackEffect(Event event);
//	
//	public abstract void secondaryAttackEffect(Event event);
//	
//	public abstract void specialAttackEffect(Event event);
}
