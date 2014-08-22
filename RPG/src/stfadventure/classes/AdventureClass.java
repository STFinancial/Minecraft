package stfadventure.classes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import stfadventure.base.Resource;
import stfadventure.events.AdventureEvent;
import stfadventure.main.Main;

public abstract class AdventureClass {
	protected final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	protected final Player player;
	protected int level = 1, exp = 0, expNext = 5;
	protected final Resource resource;
	protected final YamlConfiguration playerConfig;
	
	public AdventureClass(Main plugin, Player player) {
		this.player = player;
		this.playerConfig = playerConfig;
		level = playerConfig.getInt("level");
		exp = playerConfig.getInt("exp");
		expNext = level * 5;
		Objective objective = scoreboard.registerNewObjective("resource", "dummy");
		objective.setDisplayName(ChatColor.GREEN + "Resource");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		resource = new Resource(plugin, getResourceType(), playerConfig);
		player.setScoreboard(scoreboard);
	}
	
	public void save() {
		playerConfig.set("class", this.getClass().getName());
		playerConfig.set("level", level);
		playerConfig.set("exp", exp);
		playerConfig.set("resource", resource.getCurrentAmount());
		
	}
	
	public void info() {
		player.sendMessage(this.getClass().getSimpleName());
	}
	
	protected abstract Score getResourceType();
	
	public abstract void primaryAttack(AdventureEvent event);
	
	public abstract void secondaryAttack(AdventureEvent event);
	
	public abstract void specialAttack(AdventureEvent event);
}
