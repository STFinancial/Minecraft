package stfadventure.classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import stfadventure.item.Armor;
import stfadventure.item.ArmorType;
import stfadventure.main.Main;

public abstract class AdventureClass {
	protected final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	protected final Player player;
	protected final Main plugin;
	protected int level = 1, exp = 0, expNext = 5;
	protected final Stats stats = new Stats();
	protected final Map<String, Ability> skills = new HashMap<String, Ability>();
	protected Resource resource;
	protected final Set<ArmorType> armorTypes = new HashSet<ArmorType>();

	public AdventureClass(Main plugin, Player player) {
		this.player = player;
		this.plugin = plugin;
		resource = new Resource(plugin);
		//		stats.calculateStats(player.getInventory().getArmorContents());		
		Objective local = scoreboard.registerNewObjective("local", "dummy");
		local.setDisplaySlot(DisplaySlot.SIDEBAR);
		local.setDisplayName("Info");
		Objective global = scoreboard.registerNewObjective("global", "dummy");
		global.setDisplaySlot(DisplaySlot.BELOW_NAME);
		global.setDisplayName("Test");
		player.setScoreboard(scoreboard);
		initializeResource(plugin);
		//		initializeSkills();
		setArmorTypes();
		checkArmor();
	}

	//	public AdventureClass(Main plugin, Player player, YamlConfiguration config) {
	//		this(plugin, player);
	//		level = config.getInt("level");
	//		exp = config.getInt("exp");
	//		expNext = level * 5;
	//	}

	//	public Map<String, Object> getSaveInfo() {
	//		Map<String, Object> info = new HashMap<String, Object>();
	//		info.put("class", this.getClass().getSimpleName());
	//		info.put("level", level);
	//		info.put("exp", exp);
	//		info.put("resource", resource.getCurrentAmount());
	//		info.put("primarySkill", primarySkill.cooldownRemaining());
	//		info.put("secondarySkill", secondarySkill.cooldownRemaining());
	//		info.put("specialSkill", specialSkill.cooldownRemaining());
	//		return info;
	//	}

	public abstract void setArmorTypes();

	public void checkArmor() {
		for (ItemStack armorPiece : player.getInventory().getArmorContents()) {
			if (armorTypes.contains(ArmorType.getArmorType(armorPiece)) == false) {
				switch (Armor.getArmor(armorPiece)) {
				case HELMET:
					player.getInventory().setHelmet(null);
					break;
				case CHESTPLATE:
					player.getInventory().setChestplate(null);
					break;
				case LEGGINGS:
					player.getInventory().setLeggings(null);
					break;
				case BOOTS:
					player.getInventory().setBoots(null);
					break;
				case NONE:
				default:
					break;
				}
				if (player.getInventory().addItem(armorPiece).isEmpty() == false) {
					player.getWorld().dropItem(player.getLocation(), armorPiece);
				}
			}
		}
	}

	protected abstract void initializeResource(Main plugin);

	//	protected abstract void initializeSkills();

	public abstract void getSkill(Event event);

	public void info() {
		player.sendMessage(this.getClass().getSimpleName());
	}
}
