package stfadventure.classes;

public enum AdventureClassType {
	BEGINNER, ARCHER, RANGER, HUNTER, WARRIOR, CRUSADER, VIKING, WIZARD,
	PYROMANCER, CRYOMANCER, MONK, NECROMANCER, PRIEST;

	public static AdventureClassType getAdventureClassType(String classType) {
		for (AdventureClassType adventureClass : AdventureClassType.values()) {
			if (adventureClass.toString().equals(classType.toUpperCase())) {
				return adventureClass;
			}
		}
		return BEGINNER;
	}
}