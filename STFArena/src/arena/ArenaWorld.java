package arena;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;

public class ArenaWorld {

	public static World build() {
		if (Bukkit.getWorld("Arena") == null) {
			Bukkit.getLogger().info("Building Arena World");
			WorldCreator creator = new WorldCreator("Arena");
			creator.environment(Environment.NORMAL).generateStructures(false).type(WorldType.FLAT);
			World arenaWorld = Bukkit.getServer().createWorld(creator);
			arenaWorld.setDifficulty(Difficulty.HARD);
			arenaWorld.setSpawnFlags(false, false);
			arenaWorld.setAnimalSpawnLimit(0);
			arenaWorld.setMonsterSpawnLimit(0);
			arenaWorld.setWaterAnimalSpawnLimit(0);
			arenaWorld.setPVP(true);
			arenaWorld.setAutoSave(true);
		}
		return Bukkit.getWorld("Arena");
	}
}
