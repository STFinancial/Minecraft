package classes;

import org.bukkit.entity.Player;

public class ClassUtil {
	public static boolean usingStaff(Player player) {
		return player.getItemInHand().getType().name().contains("_HOE");
	}
	
	public static boolean usingSword(Player player) {
		return player.getItemInHand().getType().name().contains("_SWORD");
	}
	
	public static boolean usingShovel(Player player) {
		return player.getItemInHand().getType().name().contains("_SPADE");
	}
	
	public static boolean usingAxe(Player player) {
		return player.getItemInHand().getType().name().contains("_AXE");
	}


	public static boolean usingPickaxe(Player player) {
		return player.getItemInHand().getType().name().contains("_PICKAXE");
	}
	
	public static boolean usingFishingRod(Player player) {
		return player.getItemInHand().getType().name().contains("FISHING_ROD");
	}
	
	public static int getWeaponLevel(Player player) {
		String[] toolType = player.getItemInHand().getType().name().split("_");
		switch (toolType[0]) {
		case "WOODEN":
			return 1;
		case "STONE":
			return 2;
		case "IRON":
			return 3;
		case "GOLD":
			return 4;
		case "DIAMOND":
			return 5;
		default:
			return 0;
		}
	}

}
