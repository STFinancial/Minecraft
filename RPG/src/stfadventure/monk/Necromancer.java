package stfadventure.monk;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;

public class Necromancer extends Monk {
	
	public Necromancer(JavaPlugin plugin, Player player, int level, int exp) {
		super(plugin, player, level, exp);
	}
	
	@Override
	protected Score getResourceType() {
		return scoreboard.getObjective("resource").getScore(ChatColor.DARK_GRAY + "Life: ");
	}
}
