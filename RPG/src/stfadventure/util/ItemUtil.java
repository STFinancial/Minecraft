package stfadventure.util;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import stfadventure.base.Resource.ResourceType;
import stfadventure.custom.Attributes;
import stfadventure.custom.Attributes.Attribute;
import stfadventure.custom.Attributes.Attribute.Builder;
import stfadventure.custom.Attributes.AttributeType;
import stfadventure.custom.NbtFactory;
import stfadventure.custom.NbtFactory.NbtCompound;

public class ItemUtil {
	
	private static NbtCompound getTag(ItemStack item) {
		ItemStack stack = NbtFactory.getCraftItemStack(item);
		return NbtFactory.fromItemTag(stack);
	}
	
	public static void setDisplayName(ItemStack item, String name) {
		getTag(item).putPath("display.Name", name);
	}
	
	public static void setLore(ItemStack item, List<Object> loreList) {
		getTag(item).putPath("display.Lore", NbtFactory.createList(loreList));
	}
	
	public static void addLore(ItemStack item, Object lore) {
		List<Object> loreList = getTag(item).getList("display.Lore", true);
		loreList.add(lore);
		setLore(item, loreList);
	}
		
	public static ItemStack testAttribute(ItemStack item) {
		Attributes attributes = new Attributes(item);
		Builder builder = Attribute.newBuilder();
		builder.name("Damage").type(AttributeType.GENERIC_ATTACK_DAMAGE).amount(21);
		attributes.add(builder.build());
		return attributes.getStack();
	}
	
	public static void setResource(ItemStack item, ResourceType resourceType) {
		getTag(item).putPath("resource", 200);
		Bukkit.getLogger().info("" + getTag(item).getInteger("resource", 0));
	}
}
