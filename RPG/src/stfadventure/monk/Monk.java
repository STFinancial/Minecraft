package stfadventure.monk;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;

import stfadventure.base.AdventureClass;
import stfadventure.base.AdventureClassType;
import stfadventure.events.AdventureEvent;
import stfadventure.main.Main;

public class Monk extends AdventureClass {

	public Monk(Main plugin, Player player, YamlConfiguration playerConfig) {
		super(plugin, player, playerConfig);
	}

	@Override
	protected Score getResourceType() {
		return scoreboard.getObjective("resource").getScore(ChatColor.WHITE + "Life: ");
	}

	@Override
	public void primaryAttack(AdventureEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void secondaryAttack(AdventureEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void specialAttack(AdventureEvent event) {
		// TODO Auto-generated method stub
		
	}

}
