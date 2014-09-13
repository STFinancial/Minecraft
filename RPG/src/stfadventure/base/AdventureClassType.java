package stfadventure.base;

import java.lang.reflect.Constructor;

import org.bukkit.entity.Player;

import stfadventure.archer.Archer;
import stfadventure.archer.Hunter;
import stfadventure.archer.Ranger;
import stfadventure.main.Main;
import stfadventure.monk.Monk;
import stfadventure.monk.Necromancer;
import stfadventure.monk.Priest;
import stfadventure.warrior.Berserker;
import stfadventure.warrior.Paladin;
import stfadventure.warrior.Warrior;
import stfadventure.wizard.Cryomancer;
import stfadventure.wizard.Pyromancer;
import stfadventure.wizard.Wizard;

public enum AdventureClassType {
	ARCHER (Archer.class),
	RANGER (Ranger.class), 
	HUNTER (Hunter.class), 
	WARRIOR (Warrior.class), 
	BERSERKER (Berserker.class), 
	PALADIN (Paladin.class), 
	WIZARD (Wizard.class),
	PYROMANCER (Pyromancer.class), 
	CRYOMANCER (Cryomancer.class), 
	MONK (Monk.class), 
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
			if (adventureClass.toString().equals(classType.toUpperCase())) {
				return adventureClass;
			}
		}
		return null;
	}
}