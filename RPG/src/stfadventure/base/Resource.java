package stfadventure.base;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import stfadventure.main.Main;

public class Resource implements Runnable {
	private final Main plugin;
	private int taskId = -1;
	private final Scoreboard scoreboard;
	private final ResourceType type;
	private int currentAmount = 0, maximumAmount = 0, regainAmount = 0;
	
	public Resource(Main plugin, Scoreboard scoreboard, ResourceType type) {
		this.plugin = plugin;
		this.scoreboard = scoreboard;
		this.type = type;
	}
	
	public Resource(Main plugin, AdventureClass adventureClass, ResourceType type, int currentAmount) {
		this(plugin, adventureClass, type);
		this.currentAmount = currentAmount;
	}

	@Override
	public void run() {
		int currentAmount = score.getScore();
		currentAmount = currentAmount + regainAmount;
		
		if (currentAmount > maximumAmount) {
			currentAmount = maximumAmount;
			stop();
		}
		if (currentAmount < MINIMUM_AMOUNT) {
			currentAmount = MINIMUM_AMOUNT;
			stop();
		}
			
		score.setScore(currentAmount);					
	}
	
	public void start(long delay, long repeatLength) {
		if (taskId == -1) {
			taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, delay, repeatLength);
		}
	}
	
	public void gainResource(int amount) {
		int currentAmount = score.getScore();
		currentAmount = currentAmount + amount;
		if (currentAmount > maximumAmount) {
			currentAmount = maximumAmount;
		}
		score.setScore(currentAmount);
	}
	
	public boolean useResource(int amount) {
		int currentAmount = score.getScore();
		if (currentAmount - amount >= MINIMUM_AMOUNT) {
			currentAmount = currentAmount - amount;
			score.setScore(currentAmount);
			return true;
		}
		return false;		
	}
	
	public int getCurrentAmount() {
		return score.getScore();
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
