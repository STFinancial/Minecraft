package stfadventure.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;

import stfadventure.classes.Resource.ResourceType;
import stfadventure.classes.Stats.StatType;
import stfadventure.item.Attributes.Attribute;
import stfadventure.item.Attributes.Attribute.Builder;
import stfadventure.item.Attributes.AttributeType;
import stfadventure.item.NbtFactory.NbtCompound;

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
		
	public static void addItemStats(ItemStack item, Map<StatType, Integer> stats) {
		for (Entry<StatType, Integer> entry : stats.entrySet()) {
			getTag(item).putPath(entry.getKey().getStatName(), entry.getValue());
			String lore = entry.getKey().getStatName() + ": " + entry.getValue();
			addLore(item, lore);
		}
	}
	
	public static Map<StatType, Integer> getItemStats(ItemStack item) {
		Map<StatType, Integer> stats = new HashMap<StatType, Integer>();
		for (StatType stat : StatType.values()) {
			stats.put(stat, getTag(item).getInteger(stat.getStatName(), 0));
		}
		return stats;
	}
	
	public static void setResource(ItemStack item, ResourceType resourceType, int amount) {
		getTag(item).putPath("resource", amount);
		addLore(item, "Resource: " + amount);
	}
	
	public static ItemStack setAttackDamage(ItemStack item, int damage) {
		Attributes attributes = new Attributes(item);
		Builder builder = Attribute.newBuilder();
		builder.name("Damage").type(AttributeType.GENERIC_ATTACK_DAMAGE).amount(damage);
		attributes.add(builder.build());
		return attributes.getStack();
	}
}
