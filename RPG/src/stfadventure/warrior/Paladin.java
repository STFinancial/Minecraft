package stfadventure.warrior;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;

import stfadventure.main.Main;

public class Paladin extends Warrior {

	public Paladin(Main plugin, Player player, YamlConfiguration playerConfig) {
		super(plugin, player, playerConfig);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected Score getResourceType() {
		return scoreboard.getObjective("resource").getScore(ChatColor.WHITE + "Life: ");
	}
}
