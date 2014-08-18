package stfadventure.classes;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import stfadventure.wizard.Cryomancer;
import stfadventure.wizard.Wizard;

public class AdventureClassFactory {
		
	public static AdventureClass getAdventureClass(AdventureClassType classType, JavaPlugin plugin, Player player) {
		switch (classType) {
		case ARCHER:
		case RANGER:
		case HUNTER:
		case WARRIOR:
		case CRUSADER:
		case VIKING:
		case WIZARD:
			return new Wizard(plugin, player, 0, 0);
		case PYROMANCER:
		case CRYOMANCER:
			return new Cryomancer(plugin, player, 0, 0);
		case MONK:
		case NECROMANCER:
		case PRIEST:
		case BEGINNER:
		default:
			return new Beginner(null, null, 0, 0);
		}
	}
}
