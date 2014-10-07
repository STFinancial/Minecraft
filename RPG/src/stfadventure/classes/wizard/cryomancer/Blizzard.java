package stfadventure.classes.wizard.cryomancer;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
			for (Entity entity : player.getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
				if (entity instanceof LivingEntity) {
					((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 4));
				}
			}
			for (int i = 0; i < 10; i++) {
				generateSnowball();
			}
			snowfall++;
		}
		else {
			stop();
		}		
	}
	
	private void generateSnowball() {
		int x = ThreadLocalRandom.current().nextInt(-RADIUS, RADIUS + 1);
		int z = ThreadLocalRandom.current().nextInt(-RADIUS, RADIUS + 1);
		Snowball snowball = (Snowball) player.getWorld().spawnEntity(player.getLocation().add(x, 5, z), EntityType.SNOWBALL);
		snowball.setShooter(player);
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
