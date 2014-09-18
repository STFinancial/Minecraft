package stfadventure.classes.wizard.cryomancer;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.plugin.java.JavaPlugin;

public class Blizzard implements Runnable {
	private static final int RADIUS = 5;
	private int duration = 100, snowfall = 0, taskId = -1;
	private final JavaPlugin plugin;
	private final Player player;

	public Blizzard(JavaPlugin plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
	}

	@Override
	public void run() {
		if (player != null && player.isValid() && snowfall < duration) {
			World world = player.getWorld();
			int x = ThreadLocalRandom.current().nextInt(-RADIUS, RADIUS + 1);
			int z = ThreadLocalRandom.current().nextInt(-RADIUS, RADIUS + 1);
			Snowball snowball = (Snowball) world.spawnEntity(player.getLocation().add(x, 5, z), EntityType.SNOWBALL);
			snowball.setShooter(player);
			snowfall++;
		}
		else {
			stop();
		}		
	}

	public void start() {
		if (taskId == -1) {
			taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 1);
		}
		else {
			snowfall = 0;
		}
	}

	public void stop() {
		if (taskId != -1) {
			Bukkit.getScheduler().cancelTask(taskId);
			taskId = -1;
			snowfall = 0;
		}		
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
