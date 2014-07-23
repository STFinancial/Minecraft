package wizard;

import java.util.Random;

import main.Main;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.scheduler.BukkitRunnable;

public class Blizzard extends BukkitRunnable {
	private static final int RADIUS = 5;
	private Location location;
	private final World world;
	private final Player player;
	private final Random random;
	private int snowfall;
	
	public Blizzard(Player player) {
		this.player = player;
		location = player.getLocation();
		world = location.getWorld();
		random = new Random();
		this.runTaskTimer(Main.plugin, 0, 1);
	}

	@Override
	public void run() {
		if (player.isValid() && snowfall < 30) {
			int x = random.nextBoolean()? 1 : -1;
			x = x * random.nextInt(RADIUS + 5);
			int z = random.nextBoolean()? 1 : -1;
			z = z * random.nextInt(RADIUS + 5);
			Snowball snowball = (Snowball) world.spawnEntity(player.getLocation().add(x, 5, z), EntityType.SNOWBALL);
			snowball.setShooter(player);
			snowfall++;
		}
		else {
			this.cancel();
		}		
	}
}
