package stfadventure.base;

import org.bukkit.ChatColor;

public enum ResourceType {
	MANA (0, 100, 5, ChatColor.BLUE + "Mana: "),
	RAGE (0, 100, 0, ChatColor.RED + "Rage: "),
	LIFE (0, 100, 5, ChatColor.WHITE + "Life: "),
	ENERGY (0, 100, 10, ChatColor.YELLOW + "Energy: ");
	
	private int minimumAmount, maximumAmount, baseRegen;
	private String display;
	
	private ResourceType(int min, int max, int regen, String disp) {
		minimumAmount = min;
		maximumAmount = max;
		baseRegen = regen;
		display = disp;
	}
	
	public int getMinimumAmount() {
		return minimumAmount;
	}
	
	public int getMaximumAmount() {
		return maximumAmount;
	}

	public int getBaseRegen() {
		return baseRegen;
	}
	
	public String getDisplay() {
		return display;
	}
}
