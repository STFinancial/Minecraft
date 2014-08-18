package stfadventure.resource;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;

public class Resource implements Runnable {
	private final JavaPlugin plugin;
	private int taskId = -1;
	private final Score resourceAmount;
	private int maximumAmount = 100;
	private int currentAmount = maximumAmount;
	private int regainAmount = 10;
	
	public Resource(JavaPlugin plugin, Score score) {
		this.plugin = plugin;
		resourceAmount = score;
		resourceAmount.setScore(currentAmount);
	}

	@Override
	public void run() {
		if (currentAmount < maximumAmount) {
			currentAmount = currentAmount + regainAmount;
			
			if (currentAmount > maximumAmount) {
				currentAmount = maximumAmount;
			}
			
			resourceAmount.setScore(currentAmount);
		}
		else {
			stop();
		}		
	}	
	
	private void start() {
		if (taskId == -1) {
			taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 20);
		}
	}
	
	public boolean subtractAmount(int amount) {
		if (currentAmount - amount >= 0) {
			currentAmount = currentAmount - amount;
			resourceAmount.setScore(currentAmount);
			start();
			return true;
		}
		return false;		
	}
	
	public int getMaximumAmount() {
		return maximumAmount;
	}

	public void setMaximumAmount(int maximumAmount) {
		this.maximumAmount = maximumAmount;
	}
	
	public int getRegainAmount() {
		return regainAmount;
	}

	public void setRegainAmount(int regainAmount) {
		this.regainAmount = regainAmount;
	}

	public void stop() {
		if (taskId != -1) {
			Bukkit.getScheduler().cancelTask(taskId);
			taskId = -1;
		}	
	}
}
