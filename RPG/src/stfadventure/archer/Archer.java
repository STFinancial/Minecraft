package stfadventure.archer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;

import stfadventure.classes.AdventureClass;
import stfadventure.classes.AdventureClassType;
import stfadventure.events.AdventureEvent;

public class Archer extends AdventureClass {

	public Archer(JavaPlugin plugin, Player player, int level, int exp) {
		super(plugin, player, level, exp);
	}

	@Override
	protected Score getResourceType() {
		return scoreboard.getObjective("resource").getScore(ChatColor.YELLOW + "Energy: ");
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
