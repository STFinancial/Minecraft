package main;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Blizzard extends BukkitRunnable {
	private static final int RADIUS = 5;
	private Location location;
	private final World world;
	private final Player player;
	private final Random random;
	private int times;
	
	public Blizzard(Plugin plugin, Player player) {
		this.player = player;
		location = player.getLocation();
		world = location.getWorld();
		random = new Random();
		this.runTaskTimer(plugin, 0, 1);
	}

	@Override
	public void run() {
		if (player.isValid() && times < 30) {
			int x = random.nextBoolean()? 1 : -1;
			x = x * random.nextInt(RADIUS + 5);
			int z = random.nextBoolean()? 1 : -1;
			z = z * random.nextInt(RADIUS + 5);
			world.spawnEntity(player.getLocation().add(x, 5, z), EntityType.SNOWBALL);
			times++;
		}
		else {
			this.cancel();
		}		
	}
}
