package stfadventure.resource;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;

public class Resource implements Runnable {
	private static final int MINIMUM_AMOUNT = 0;
	private final JavaPlugin plugin;
	private int taskId = -1;
	private final Score resourceAmount;
	private int maximumAmount = 100;
	private int currentAmount = maximumAmount;
	private int regainAmount = 10;
	
	public Resource(JavaPlugin plugin, Score score) {
		this.plugin = plugin;
		resourceAmount = score;
		if (resourceAmount != null) {
			resourceAmount.setScore(currentAmount);
		}
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
	
	public void start(long delay, long repeatLength) {
		if (taskId == -1) {
			taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, delay, repeatLength);
		}
	}
	
	public void addAmount(int amount) {
		currentAmount = currentAmount + amount;
		if (currentAmount > maximumAmount) {
			currentAmount = maximumAmount;
		}
		if (currentAmount < MINIMUM_AMOUNT) {
			currentAmount = MINIMUM_AMOUNT;
		}
	}
	public boolean subtractAmount(int amount) {
		if (currentAmount - amount >= 0) {
			currentAmount = currentAmount - amount;
			resourceAmount.setScore(currentAmount);
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
