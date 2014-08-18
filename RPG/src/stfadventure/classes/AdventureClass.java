package stfadventure.classes;

import java.util.UUID;

import stfadventure.events.AdventureEvent;

public abstract class AdventureClass {
	protected final UUID id;
	protected int level = 0, exp = 0, expNext = 5;
	
	
	public AdventureClass(UUID id, int level, int exp) {
		this.id = id;
		this.level = level;
		this.exp = exp;
		this.expNext = level * 5;
	}

	public abstract AdventureClassType getType();
	
	public abstract void primaryAttack(AdventureEvent event);
	
	public abstract void secondaryAttack(AdventureEvent event);
	
	public abstract void specialAttack(AdventureEvent event);
}
