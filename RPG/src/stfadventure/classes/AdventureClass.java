package stfadventure.classes;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.comphenix.protocol.events.PacketEvent;

import stfadventure.main.Main;

public abstract class AdventureClass {
	protected final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	protected final Player player;
	protected final Main plugin;
	protected int level = 1, exp = 0, expNext = 5;
	protected final Stats stats = new Stats();
	protected final Map<String, Ability> skills = new HashMap<String, Ability>();
	protected Resource resource;
	
	public AdventureClass(Main plugin, Player player) {
		this.player = player;
		this.plugin = plugin;
//		stats.calculateStats(player.getInventory().getArmorContents());		
		Objective local = scoreboard.registerNewObjective("local", "dummy");
		local.setDisplaySlot(DisplaySlot.SIDEBAR);
		local.setDisplayName("Info");
		Objective global = scoreboard.registerNewObjective("global", "dummy");
		global.setDisplaySlot(DisplaySlot.BELOW_NAME);
		global.setDisplayName("Test");
		player.setScoreboard(scoreboard);
		initializeResource(plugin);
		initializeSkills();
	}
	
	public AdventureClass(Main plugin, Player player, YamlConfiguration config) {
		this(plugin, player);
		level = config.getInt("level");
		exp = config.getInt("exp");
		expNext = level * 5;
	}
	
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
	
	public abstract void checkArmor(Event event);
	
	protected abstract void initializeResource(Main plugin);
	
	protected abstract void initializeSkills();
	
	public abstract void getSkill(Event event);
	
	public void info() {
		player.sendMessage(this.getClass().getSimpleName());
	}
}
