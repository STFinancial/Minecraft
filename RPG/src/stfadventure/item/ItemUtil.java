package stfadventure.item;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import stfadventure.base.Resource.ResourceType;

public class ItemUtil {
	
	public static List<String> getAttributes(ItemStack item) {
		List<String> lore = item.getItemMeta().getLore();
		return lore;		
	}
	
	public static int getResource(ItemStack item, ResourceType resourceType) {
		List<String> lore = getAttributes(item);
		for (String attribute : lore) {
			if (attribute.toLowerCase().contains(resourceType.name().toLowerCase())) {
				return Integer.parseInt(attribute.substring(attribute.indexOf(" ") + 1));
			}
		}
		return 0;
	}

}
