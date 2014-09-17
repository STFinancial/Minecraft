package stfadventure.classes;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;
import org.bukkit.util.Vector;

import stfadventure.events.AdventureEvent;
import stfadventure.main.Main;

public class Archer extends AdventureClass {

	public Archer(Main plugin, Player player, YamlConfiguration playerConfiguration) {
		super(plugin, player, playerConfiguration);
	}

	@Override
	protected Score getResourceType() {
		return scoreboard.getObjective("resource").getScore(ChatColor.YELLOW + "Energy: ");
	}

	@Override
	public void primaryAttack(Event event) {
		EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
		Vector knockback = player.getLocation().getDirection().multiply(level / 20);
		damageEvent.getEntity().setVelocity(knockback);
	}

	@Override
	public void secondaryAttack(Event event) {
	}

	@Override
	public void specialAttack(Event event) {		
	}

	@Override
	protected void initializeScoreboard() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initializeSkills() {
		// TODO Auto-generated method stub
		
	}
}
