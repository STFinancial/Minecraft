package stfadventure.base;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import stfadventure.events.AdventureEvent;
import stfadventure.main.Main;

public abstract class AdventureClass {
	protected final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	public final Objective local = scoreboard.registerNewObjective("sidebar", "dummy");
	public final Objective global = scoreboard.registerNewObjective("overhead", "dummy");
	public final Player player;
	protected final Main plugin;
	protected int level = 1, exp = 0, expNext = 5;
	protected Skill primarySkill, secondarySkill, specialSkill;
	protected final Resource resource;
	
	public AdventureClass(Main plugin, Player player) {
		this.player = player;
		this.plugin = plugin;
		this.resource = null;
		
		local.setDisplaySlot(DisplaySlot.SIDEBAR);
		global.setDisplaySlot(DisplaySlot.BELOW_NAME);
		player.setScoreboard(scoreboard);
	}
	
	protected abstract void initializeScoreboard();
	
	protected abstract void initializeSkills();
	
	public AdventureClass(Main plugin, Player player, YamlConfiguration config) {
		this(plugin, player);
		level = config.getInt("level");
		exp = config.getInt("exp");
		expNext = level * 5;
	}
	
	public Map<String, Object> getSaveInfo() {
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("class", this.getClass().getSimpleName());
		info.put("level", level);
		info.put("exp", exp);
		info.put("resource", resource.getCurrentAmount());
		info.put("primarySkill", primarySkill.cooldownRemaining());
		info.put("secondarySkill", secondarySkill.cooldownRemaining());
		info.put("specialSkill", specialSkill.cooldownRemaining());
		return info;
	}
	
	public void info() {
		player.sendMessage(this.getClass().getSimpleName());
	}
	
	protected abstract Score getResourceType();
	
	public abstract void primaryAttack(AdventureEvent event);
	
	public abstract void secondaryAttack(AdventureEvent event);
	
	public abstract void specialAttack(AdventureEvent event);
}
