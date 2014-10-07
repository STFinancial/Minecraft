package stfadventure.item;

import org.bukkit.inventory.ItemStack;

public enum Armor {
	NONE("_NONE"),
	HELMET("_HELMET"),
	CHESTPLATE("_CHESTPLATE"),
	LEGGINGS("_LEGGINGS"),
	BOOTS("_BOOTS");
	
	String armor;
	
	private Armor(String armor) {
		this.armor = armor;		
	}
	
	private String getStringMap() {
		return armor;
	}
	
	public static Armor getArmor(ItemStack armorPiece) {
		if (armorPiece != null) {
			for (Armor armor : Armor.values()) {
				if (armorPiece.getType().name().contains(armor.getStringMap())) {
					return armor;
				}
			}
		}
		
		return NONE;
	}
}
