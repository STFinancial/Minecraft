package stfadventure.monk;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;

import stfadventure.main.Main;

public class Necromancer extends Monk {
	
	public Necromancer(Main plugin, Player player, YamlConfiguration playerConfig) {
		super(plugin, player, playerConfig);
	}
	
	@Override
	protected Score getResourceType() {
		return scoreboard.getObjective("resource").getScore(ChatColor.GRAY + "Essence: ");
	}
	
	
}
