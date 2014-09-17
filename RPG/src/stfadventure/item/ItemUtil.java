package stfadventure.item;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import stfadventure.item.Attributes.Attribute;
import stfadventure.item.Attributes.Attribute.Builder;
import stfadventure.item.Attributes.AttributeType;
import stfadventure.item.NbtFactory.NbtCompound;
import stfadventure.resource.ResourceType;

public class ItemUtil {
	
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
		
	public static void addStats(ItemStack item, Map<StatType, Integer> stats) {
		for (StatType stat : stats.keySet()) {
			getTag(item).putPath(stat.getStatName(), stats.get(stat));
			String lore = stat.getStatName() + ": " + stats.get(stat);
			addLore(item, lore);
		}
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
