package main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class PlayerData {
	int energy, taskID = -1, gracePeriodID = -1;
	BukkitScheduler scheduler;
	Plugin plugin;
	boolean ready = false;
	ScoreboardManager manager = Bukkit.getScoreboardManager();
	Scoreboard board = manager.getNewScoreboard();
	Objective objective;
	Score score;
	Player player;
	
	public PlayerData(Plugin plugin, Player player){
		scheduler = Bukkit.getServer().getScheduler();
		energy = 100;
		this.plugin = plugin;
		this.player = player;
		player.setScoreboard(board);
		objective = board.registerNewObjective("Energy:", "dummy");
		score = objective.getScore(player.getName());
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}

	public boolean swingReady(){
		return energy >= Main.ENERGY_PER_SWING;
	}
	
	public boolean damageReady(){
		return ready;
	}

	public void swingPerformed(){
		energy -= Main.ENERGY_PER_SWING;
		if(taskID != -1){
			scheduler.cancelTask(taskID);
		}
		taskID = scheduler.scheduleSyncRepeatingTask(plugin ,new Runnable(){
			
			@Override
			public void run() {
				if(energy>=100){
					scheduler.cancelTask(taskID);
				}else{
					energy += Main.TICKS_PER_UPDATE * Main.ENERGY_PER_TICK;
					score.setScore(energy);
				}
			}

		}, Main.TICKS_PER_UPDATE, Main.TICKS_PER_UPDATE);
		ready = true;
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable(){
			
			@Override
			public void run() {
				ready = false;
			}

		}, 1);
	}



}
