package stfadventure.classes.wizard.pyromancer;

import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Apocalypse extends BukkitRunnable {
	private final Player player;
	private boolean explode = false;
	
	public Apocalypse(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		if (player.isValid()) {
			if (explode == false) {
				player.setFireTicks(60);
				explode = true;
			}
			else if (explode) { 
				player.setNoDamageTicks(player.getMaximumNoDamageTicks());
				player.setLastDamage(Integer.MAX_VALUE);
				Location location = player.getLocation();
				double x = location.getX();
				double y = location.getY();
				double z = location.getZ();
				location.getWorld().createExplosion(x, y, z, 4F, false, false);
				player.setNoDamageTicks(0);
				this.cancel();
			}	
		}
		else {
			if (player.getPlayerWeather().equals(WeatherType.DOWNFALL)) {
				player.sendMessage("Apocalypse has been doused");
			}
			this.cancel();
		}
	}
}
