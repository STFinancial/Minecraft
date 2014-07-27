package main;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;

public class ArenaWorld {

	public static World build() {
		if (Bukkit.getWorld("Arena") == null) {
			WorldCreator creator = new WorldCreator("Arena");
			creator.environment(Environment.NORMAL).generateStructures(false).type(WorldType.FLAT);
			World arenaWorld = Bukkit.getServer().createWorld(creator);
			arenaWorld.setDifficulty(Difficulty.HARD);
			arenaWorld.setSpawnFlags(false, false);
			arenaWorld.setPVP(true);
			arenaWorld.setAutoSave(true);
		}
		Bukkit.getWorlds().get(0).getEntities().clear();
		return Bukkit.getWorld("Arena");
	}
}
