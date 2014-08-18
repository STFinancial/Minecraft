package stfadventure.classes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import stfadventure.events.AdventureEvent;
import stfadventure.resource.Resource;

public abstract class AdventureClass {
	protected final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	protected final Player player;
	protected int level = 0, exp = 0, expNext = 5;
	protected final Resource resource;
	
	public AdventureClass(JavaPlugin plugin, Player player, int level, int exp) {
		this.player = player;
		this.level = level;
		this.exp = exp;
		this.expNext = level * 5;
		resource = new Resource(plugin, buildScoreboard());
		player.setScoreboard(scoreboard);
	}
	
	protected abstract Score buildScoreboard();

	public abstract AdventureClassType getType();
	
	public abstract void primaryAttack(AdventureEvent event);
	
	public abstract void secondaryAttack(AdventureEvent event);
	
	public abstract void specialAttack(AdventureEvent event);
}
