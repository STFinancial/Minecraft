package stfadventure.classes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import stfadventure.main.Main;

public class Resource implements Runnable {
	public enum ResourceType {
		MANA (ChatColor.BLUE + "Mana: "), 
		LIFE (ChatColor.GREEN + "Life: "), 
		ESSENCE (ChatColor.GRAY + "Essence: "), 
		ENERGY (ChatColor.YELLOW + "Energy: "),
		HOLY_ENERGY (ChatColor.WHITE + "Holy Energy: ");
		
		private final String display;
		
		private ResourceType(String display) {
			this.display = display;
		}
		
		public String getDisplay() {
			return display;
		}		
	}
	
	enum State {
		START,
		STOP,
		WAIT;
	}
	
	private static final int MINIMUM_AMOUNT = 0;
	private final Main plugin;
	private int taskId = -1;
	private ResourceType type;
	private int currentAmount = 0, maximumAmount = 0, regainAmount = 0;
	
	public Resource(Main plugin) {
		this.plugin = plugin;
	}
	
	public void setType(ResourceType type, int currentAmount, int maximumAmount, int regainAmount) {
		this.type = type;
		this.currentAmount = currentAmount;
		this.maximumAmount = maximumAmount;
		this.regainAmount = regainAmount;
	}
	
	public ResourceType getType() {
		return type;
	}
	
	public String getDisplayType() {
		return type.getDisplay();
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
	}
	
	public boolean useResource(int amount) {
		if (currentAmount - amount >= MINIMUM_AMOUNT) {
			currentAmount = currentAmount - amount;
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
