package stfadventure.item;

import org.bukkit.entity.Player;

public enum Weapon {
	NONE("_NONE"),
	AXE("_AXE"),
	PICKAXE("_PICKAXE"),
	SWORD("_SWORD"),
	SPADE("_SPADE"),
	HOE("_HOE"),
	BOW("BOW");
	
	String weapon;
	
	private Weapon(String weapon) {
		this.weapon = weapon;
	}
	
	private String getStringMap() {
		return weapon;
	}
	
	private static Weapon getWeapon(Player player) {
		String weapon = player.getItemInHand().getType().name();
		for (Weapon weaponType : Weapon.values()) {
			if (weapon.contains(weaponType.getStringMap())) {
				return weaponType;
			}
		}
		return NONE;
	}
	
	public static boolean match(Player player, Weapon weapon) {
		return (Weapon.getWeapon(player).equals(weapon));
	}
}
