package stfadventure.item;

import org.bukkit.inventory.ItemStack;

public enum ArmorType {
	NONE("NONE_"),
	LEATHER("LEATHER_"),
	GOLD("GOLD_"),
	DIAMOND("DIAMOND_"),
	IRON("IRON_"),
	CHAINMAIL("CHAINMAIL_");
	
	String armorType;
	
	private ArmorType(String armorType) {
		this.armorType = armorType;		
	}
	
	private String getStringMap() {
		return armorType;
	}
	
	public static ArmorType getArmorType(ItemStack armorPiece) {
		if (armorPiece != null) {
			for (ArmorType armorType : ArmorType.values()) {
				if (armorPiece.getType().name().contains(armorType.getStringMap())) {
					return armorType;
				}
			}
		}
		
		return NONE;
	}
}
