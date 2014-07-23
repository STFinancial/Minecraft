package util;

import org.bukkit.block.Block;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.Openable;
import org.bukkit.material.Redstone;

public class Util {
	public static enum Weapon {NONE, FIST, BOW, SWORD, AXE, SPADE, PICKAXE, FISHING_ROD, HOE};
	
	public static boolean isInteractable(Block block) {
		if (block.getState().getData() instanceof Redstone) {
			return true;
		}
		if (block.getState().getData() instanceof Openable) {
			return true;
		}
		if (block.getState().getData() instanceof Hanging) {
			return true;
		}
		if (block.getState() instanceof InventoryHolder) {
			return true;
		}
		switch (block.getType()) {
		case BED:
		case SIGN:
		case WOODEN_DOOR:
		case IRON_DOOR:
		case WORKBENCH:
			return true;
		default:
			return false;			
		}
	}
	
	public static Weapon getWeapon(Player player) {
		String weapon = player.getItemInHand().getType().name();
		if (weapon.contains("AIR")) {
			return Weapon.FIST;
		}
		else if (weapon.contains("BOW")) {
			return Weapon.BOW;
		}
		else if (weapon.contains("_SWORD")) {
			return Weapon.SWORD;
		}
		else if (weapon.contains("_AXE")) {
			return Weapon.AXE;
		}
		else if (weapon.contains("_SPADE")) {
			return Weapon.SPADE;
		}
		else if (weapon.contains("_PICKAXE")) {
			return Weapon.PICKAXE;
		}
		else if (weapon.contains("FISHING_ROD")) {
			return Weapon.FISHING_ROD;
		}
		else if (weapon.contains("_HOE")) {
			return Weapon.HOE;
		}
		else {
			return Weapon.NONE;
		}
	}
}
