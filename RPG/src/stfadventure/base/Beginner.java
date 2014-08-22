package stfadventure.base;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;

import stfadventure.classes.AdventureClass;
import stfadventure.events.AdventureEvent;
import stfadventure.main.Main;

public class Beginner extends AdventureClass {

	public Beginner(Main plugin, Player player, YamlConfiguration playerConfig) {
		super(plugin, player, playerConfig);
	}

	@Override
	protected Score getResourceType() {
		return scoreboard.getObjective("resource").getScore(ChatColor.BLACK + "None: ");
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
