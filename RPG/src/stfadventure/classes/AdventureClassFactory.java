package stfadventure.classes;

import stfadventure.wizard.Necromancer;

public class AdventureClassFactory {
		
	public static AdventureClass getAdventureClass(AdventureClassType classType, int level, int exp) {
		switch (classType) {
		case ARCHER:
		case RANGER:
		case HUNTER:
		case WARRIOR:
		case CRUSADER:
		case VIKING:
		case WIZARD:
		case PYROMANCER:
		case CRYOMANCER:
		case MONK:
		case NECROMANCER:
			return new Necromancer(level, exp);
		case PRIEST:
			return new Priest(level, exp);
		case BEGINNER:
		default:
			return new Beginner(level, exp);
			
		}		
	}
}
