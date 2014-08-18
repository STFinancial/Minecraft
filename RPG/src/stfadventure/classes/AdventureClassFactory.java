package stfadventure.classes;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import stfadventure.archer.Archer;
import stfadventure.monk.Monk;
import stfadventure.monk.Necromancer;
import stfadventure.warrior.Warrior;
import stfadventure.wizard.Cryomancer;
import stfadventure.wizard.Wizard;

public class AdventureClassFactory {
		
	public static AdventureClass getAdventureClass(AdventureClassType classType, JavaPlugin plugin, Player player) {
		switch (classType) {
		case ARCHER:
			return new Archer(plugin, player, 0, 0);
		case RANGER:
		case HUNTER:
		case WARRIOR:
			return new Warrior(plugin, player, 0, 0);
		case CRUSADER:
		case VIKING:
		case WIZARD:
			return new Wizard(plugin, player, 0, 0);
		case PYROMANCER:
		case CRYOMANCER:
			return new Cryomancer(plugin, player, 0, 0);
		case MONK:
			return new Monk(plugin, player, 0, 0);
		case NECROMANCER:
			return new Necromancer(plugin, player, 0, 0);
		case PRIEST:
		case BEGINNER:
		default:
			return new Beginner(plugin, player, 0, 0);
		}
	}
}
