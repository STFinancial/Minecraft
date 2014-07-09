package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class PlayerData {
	int energy, taskID = -1;
	BukkitScheduler scheduler;
	Plugin plugin;

	public PlayerData(Plugin plugin){
		scheduler = Bukkit.getServer().getScheduler();
		energy = 100;
		this.plugin = plugin;
	}

	public boolean swingReady(){
		return energy > Main.ENERGY_PER_SWING;
	}

	public void swingPerformed(){
		energy -= Main.ENERGY_PER_SWING;
		if(taskID != -1){
			scheduler.cancelTask(taskID);
		}
		taskID = scheduler.scheduleSyncRepeatingTask(plugin ,new Runnable(){
			
			@Override
			public void run() {
				if(energy>100){
					scheduler.cancelTask(taskID);
				}else{
					energy+=5;
				}
			}

		}, 5, 5);
	}



}
