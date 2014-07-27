package util;

import org.bukkit.entity.Player;

public enum Weapon {
	NONE, FIST, BOW, FISHING_ROD, SWORD, AXE, SPADE, PICKAXE, HOE;
	
	public static Weapon getWeapon(Player player) {
		switch (player.getItemInHand().getType()) {
		case AIR:
			return NONE;
		case BOW:
			return BOW;
		case FISHING_ROD:
			return FISHING_ROD;
		case WOOD_SWORD:
		case STONE_SWORD:
		case GOLD_SWORD:
		case IRON_SWORD:
		case DIAMOND_SWORD:
			return SWORD;
		case WOOD_AXE:
		case STONE_AXE:
		case GOLD_AXE:
		case IRON_AXE:
		case DIAMOND_AXE:
			return AXE;
		case WOOD_SPADE:
		case STONE_SPADE:
		case GOLD_SPADE:
		case IRON_SPADE:
		case DIAMOND_SPADE:
			return SPADE;
		case WOOD_PICKAXE:
		case STONE_PICKAXE:
		case GOLD_PICKAXE:
		case IRON_PICKAXE:
		case DIAMOND_PICKAXE:
			return PICKAXE;
		case WOOD_HOE:
		case STONE_HOE:
		case GOLD_HOE:
		case IRON_HOE:
		case DIAMOND_HOE:
			return HOE;
		default:
			return NONE;
		}
	}
}
