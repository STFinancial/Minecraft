package stfadventure.classes;

import java.lang.reflect.Constructor;

import org.bukkit.entity.Player;

import stfadventure.classes.hunter.Hunter;
import stfadventure.classes.necromancer.Necromancer;
import stfadventure.classes.paladin.Paladin;
import stfadventure.classes.priest.Priest;
import stfadventure.classes.rogue.Rogue;
import stfadventure.classes.wizard.cryomancer.Cryomancer;
import stfadventure.classes.wizard.pyromancer.Pyromancer;
import stfadventure.main.Main;

public enum AdventureClassType {
	HUNTER (Hunter.class), 
	ROGUE (Rogue.class), 
	PALADIN (Paladin.class), 
	PYROMANCER (Pyromancer.class), 
	CRYOMANCER (Cryomancer.class), 
	NECROMANCER (Necromancer.class), 
	PRIEST (Priest.class);
	
	private final Constructor<? extends AdventureClass> ctor;

	
	private AdventureClassType(final Class<? extends AdventureClass> classType) {
		try {
			this.ctor = classType.getConstructor(Main.class, Player.class);
		} catch (NoSuchMethodException e) {
			throw new AssertionError();
		}
	}
	
	public AdventureClass getAdventureClass(Main plugin, Player player) {
		try {
			return ctor.newInstance(plugin, player);
		} catch (Exception e) {
			throw new AssertionError();
		}
	}

	public static AdventureClassType getAdventureClassType(String classType) {
		for (AdventureClassType adventureClass : AdventureClassType.values()) {
			if (adventureClass.name().equals(classType.toUpperCase())) {
				return adventureClass;
			}
		}
		return null;
	}
}