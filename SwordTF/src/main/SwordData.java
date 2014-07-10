package main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class SwordData implements Runnable {
	public static final int ENERGY_PER_SWING = 40;
	public static final int ENERGY_PER_TICK = 1;
	public static final int TICKS_PER_UPDATE = 5;
	private int energy = 100, taskID = -1;
	private final BukkitScheduler scheduler;
	private final Main plugin;
	boolean damageReady = false;
	private final Scoreboard board = Bukkit.getScoreboardManager()
			.getNewScoreboard();
	private Objective objective;
	private Score score;
	private final Player player;

	public SwordData(Main plugin, Player player) {
		scheduler = Bukkit.getServer().getScheduler();
		this.plugin = plugin;
		this.player = player;
		player.setScoreboard(board);
		objective = board.registerNewObjective(" ", "dummy");
		score = objective.getScore("Energy:");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		score.setScore(energy);
	}

	public int getEnergy() {
		return energy;
	}

	public boolean swingReady() {
		damageReady = false;
		return energy >= ENERGY_PER_SWING;
	}

	public boolean damageReady() {
		return damageReady;
	}

	public void swingPerformed() {
		energy -= ENERGY_PER_SWING;
		if (taskID != -1) {
			scheduler.cancelTask(taskID);
		}
		taskID = scheduler.scheduleSyncRepeatingTask(plugin, this,
				TICKS_PER_UPDATE, TICKS_PER_UPDATE);
		
		//@TODO <TESTING> check if animation is applied too early (if so we will add in scheduled Task instead)
		if (energy < 40) {	
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.SLOW_DIGGING, 40, 10, true));
		}
		
		damageReady = true;
	}

	public void stop() {
		if (taskID != -1) {
			scheduler.cancelTask(taskID);
		}
	}

	@Override
	public void run() {
		if (energy >= 100) {
			scheduler.cancelTask(taskID);
			taskID = -1;
		} else {
			energy += TICKS_PER_UPDATE * ENERGY_PER_TICK;
			score.setScore(energy);
			if (energy >= ENERGY_PER_SWING) {
				if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
					player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
				}
			}
		}
	}

}
