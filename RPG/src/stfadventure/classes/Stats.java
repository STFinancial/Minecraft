package stfadventure.classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;

import stfadventure.item.ItemUtil;

public class Stats {
	public enum StatType {
		STRENGTH ("Strength"), INTELLIGENCE ("Intelligence"), WILL ("Will"), AGILITY ("Agility");
		
		private String statName;
		
		private StatType(String statName) {
			this.statName = statName;
		}
		
		public String getStatName() {
			return statName;
		}
	}
	
	private final Map<StatType, Integer> stats = new HashMap<StatType, Integer>();
	
	public Stats() {
		for (StatType stat : StatType.values()) {
			stats.put(stat, 0);
		}
	}
	
	public int getStat(StatType stat) {
		return stats.get(stat);
	}
	
	public void calculateStats(ItemStack[] armor) {
		for (ItemStack armorPiece : armor) {
			for (Entry<StatType, Integer> entry : ItemUtil.getItemStats(armorPiece).entrySet()) {
				stats.put(entry.getKey(), stats.get(entry.getKey()) + entry.getValue());
			}
		}
	}
}
