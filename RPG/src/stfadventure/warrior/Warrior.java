package stfadventure.warrior;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;

import stfadventure.base.AdventureClass;
import stfadventure.events.AdventureEvent;
import stfadventure.main.Main;

public class Warrior extends AdventureClass {

	public Warrior(Main plugin, Player player, YamlConfiguration playerConfig) {
		super(plugin, player, playerConfig);
	}

	@Override
	protected Score getResourceType() {
		return scoreboard.getObjective("resource").getScore(ChatColor.RED + "Rage: ");
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
