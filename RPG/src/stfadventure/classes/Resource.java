package stfadventure.classes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import stfadventure.main.Main;

public class Resource implements Runnable {
	public enum ResourceType {
		MANA (ChatColor.BLUE + "Mana: "), 
		LIFE (ChatColor.GREEN + "Life: "), 
		ESSENCE (ChatColor.GRAY + "Essence: "), 
		RAGE (ChatColor.RED + "Rage: "),
		HOLY_ENERGY (ChatColor.YELLOW + "Holy Energy: ");
		
		private final String display;
		
		private ResourceType(String display) {
			this.display = display;
		}
		
		public String getDisplay() {
			return display;
		}		
	}
	
	private static final int MINIMUM_AMOUNT = 0;
	private final Main plugin;
	private int taskId = -1;
	private final Score global;
	private final Score local;
	private int currentAmount = 0, maximumAmount = 0, regainAmount = 0;
	
	public Resource(Main plugin, Scoreboard scoreboard, ResourceType type) {
		this.plugin = plugin;
		global = scoreboard.getObjective("global").getScore(type.getDisplay());
		local = scoreboard.getObjective("local").getScore(type.getDisplay());
		updateScore();
	}
	
	public Resource(Main plugin, Scoreboard scoreboard, ResourceType type, int currentAmount) {
		this(plugin, scoreboard, type);
		this.currentAmount = currentAmount;
		updateScore();		
	}
	
	private void updateScore() {
		global.setScore(currentAmount);
		local.setScore(currentAmount);
	}

	@Override
	public void run() {
		currentAmount = currentAmount + regainAmount;
		
		if (currentAmount > maximumAmount) {
			currentAmount = maximumAmount;
			stop();
		}
		if (currentAmount < MINIMUM_AMOUNT) {
			currentAmount = MINIMUM_AMOUNT;
			stop();
		}
			
		updateScore();			
	}
	
	public void start(long delay, long repeatLength) {
		if (taskId == -1) {
			taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, delay, repeatLength);
		}
	}
	
	private void stop() {
		if (taskId != -1) {
			Bukkit.getScheduler().cancelTask(taskId);
			taskId = -1;
		}	
	}
	
	public void addResource(int amount) {
		currentAmount = currentAmount + amount;
		if (currentAmount > maximumAmount) {
			currentAmount = maximumAmount;
		}
		updateScore();
	}
	
	public boolean useResource(int amount) {
		if (currentAmount - amount >= MINIMUM_AMOUNT) {
			currentAmount = currentAmount - amount;
			updateScore();
			return true;
		}
		return false;		
	}
	
	public int getCurrentAmount() {
		return currentAmount;
	}
	
	public void setCurrentAmount(int amount) {
		currentAmount = amount;
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
}
