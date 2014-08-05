package main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

public class SwordData implements Runnable {
	public static final int ENERGY_PER_SWING = 40;
	public static final int ENERGY_PER_TICK = 1;
	public static final int TICKS_PER_UPDATE = 2;
	public static final int ENERGY_PER_BLOCK = 30;
	public static final int BLOCK_LENGTH_TICKS = 12;
	int energy = 100;
	private int taskID = -1;
	private int blockID = -1;
	private final BukkitScheduler scheduler;
	private final SwordMain plugin;
	boolean damageReady = false;
	private final Player player;
	boolean criticalBlock = false;

	public SwordData(SwordMain plugin, Player player) {
		scheduler = Bukkit.getServer().getScheduler();
		this.plugin = plugin;
		this.player = player;
	}

	public int getEnergy() {
		return energy;
	}

	public boolean swingReady() {
		damageReady = false;
		if(energy < ENERGY_PER_SWING){
			if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING) == false) 
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 40, 127, true));

			return false;
		}else{
			return true;
		}
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
		damageReady = true;
	}

	public boolean blockReady() {
		if(energy < ENERGY_PER_SWING){
			if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING) == false) 
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 40, 127, true));			
			return false;
		}else{
			return true;
		}
	}

	public void blockPerformed() {
		energy -= ENERGY_PER_BLOCK;
		if (taskID != -1) {
			scheduler.cancelTask(taskID);
		}
		taskID = scheduler.scheduleSyncRepeatingTask(plugin, this,
				TICKS_PER_UPDATE, TICKS_PER_UPDATE);
	}

	public void stop() {
		if (taskID != -1) {
			scheduler.cancelTask(taskID);
		}
		if (blockID != -1) {
			scheduler.cancelTask(blockID);
		}
	}

	@Override
	public void run() {
		if (energy >= 100) {
			scheduler.cancelTask(taskID);
			taskID = -1;
		} else {
			energy += TICKS_PER_UPDATE * ENERGY_PER_TICK;
			if (energy >= ENERGY_PER_SWING - TICKS_PER_UPDATE) {
				if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) 
					player.removePotionEffect(PotionEffectType.SLOW_DIGGING);

			}
		}
	}

	public void criticalBlock() {

		if (blockID != -1) {
			scheduler.cancelTask(blockID);
		}

		criticalBlock = true;
		blockID = scheduler.scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				criticalBlock = false;
				blockID = -1;
			}
		}, BLOCK_LENGTH_TICKS);


	}

	public void changeEnergy(int amount) {
		energy += amount;
	}



}
