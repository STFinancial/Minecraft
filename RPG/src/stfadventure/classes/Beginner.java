package stfadventure.classes;

import java.util.UUID;

public class Beginner extends AdventureClass {

	public Beginner(UUID id, int level, int exp) {
		super(id, level, exp);
	}

	@Override
	public AdventureClassType getType() {
		return AdventureClassType.BEGINNER;
	}

}
