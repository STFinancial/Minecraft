package stfadventure.resource;

import org.bukkit.ChatColor;

public enum ResourceType {
	MANA (100, 5, ChatColor.BLUE + "Mana: "),
	RAGE (100, -5, ChatColor.RED + "Rage: "),
	LIFE (100, 5, ChatColor.WHITE + "Life: "),
	ENERGY (100, 10, ChatColor.YELLOW + "Energy: ");
	
	private int maximumAmount, baseRegen;
	private String display;
	
	private ResourceType(int max, int regen, String disp) {
		maximumAmount = max;
		baseRegen = regen;
		display = disp;
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
