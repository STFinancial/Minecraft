package arena;

import java.util.HashSet;
import java.util.Set;

import main.DataManager;

public class ArenaManager {
	private final Set<Arena> arenas = new HashSet<Arena>();
	private final DataManager dataManager;
	
	public ArenaManager(DataManager dataManager) {
		this.dataManager = dataManager;		
	}

}
