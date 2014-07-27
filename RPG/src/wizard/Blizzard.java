package wizard;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.scheduler.BukkitRunnable;

public class Blizzard extends BukkitRunnable {
	private static final int RADIUS = 5;
	private final int duration;
	private final Player player;
	private int snowfall = 0;
	
	public Blizzard(Player player, int duration) {
		this.player = player;
		this.duration = duration;
	}
	
	@Override
	public void run() {
		if (player.isValid() && snowfall < duration) {
			World world = player.getWorld();
			int x = ThreadLocalRandom.current().nextInt(-RADIUS, RADIUS + 1);
			int z = ThreadLocalRandom.current().nextInt(-RADIUS, RADIUS + 1);
			Snowball snowball = (Snowball) world.spawnEntity(player.getLocation().add(x, 5, z), EntityType.SNOWBALL);
			snowball.setShooter(player);
			snowfall++;
		}
		else {
			this.cancel();
		}		
	}
}
