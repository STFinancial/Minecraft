package stfadventure.classes;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;

import stfadventure.events.AdventureEvent;

public class Beginner extends AdventureClass {

	public Beginner(JavaPlugin plugin, Player player, int level, int exp) {
		super(plugin, player, level, exp);
	}

	@Override
	protected Score getResourceType() {
		return null;
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
