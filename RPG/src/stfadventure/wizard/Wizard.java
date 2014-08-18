package stfadventure.wizard;

import org.bukkit.ChatColor;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import stfadventure.classes.AdventureClass;
import stfadventure.classes.AdventureClassType;
import stfadventure.events.AdventureEvent;

public class Wizard extends AdventureClass {

	public Wizard(JavaPlugin plugin, Player player, int level, int exp) {
		super(plugin, player, level, exp);
	}

	@Override
	protected Score buildScoreboard() {
		Objective objective = scoreboard.registerNewObjective("resource", "dummy");
		objective.setDisplayName(ChatColor.GREEN + "Resource");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);	
		return objective.getScore(ChatColor.BLUE + "Mana: ");
	}

	@Override
	public AdventureClassType getType() {
		return AdventureClassType.WIZARD;
	}

	@Override
	public void primaryAttack(AdventureEvent event) {
		if (resource.subtractAmount(10)) {
			player.setNoDamageTicks(player.getMaximumNoDamageTicks());
			player.setLastDamage(Integer.MAX_VALUE);
			SmallFireball fireball = player.launchProjectile(SmallFireball.class);
			fireball.setShooter(player);
			player.setNoDamageTicks(0);		
		}
		else {
			player.sendMessage("Not enough mana to shoot fireball!");
		}
	}

	@Override
	public void secondaryAttack(AdventureEvent event) {
		if (resource.subtractAmount(30)) {
			player.launchProjectile(EnderPearl.class).setVelocity(player.getLocation().getDirection().normalize());	
		}
		else {
			player.sendMessage("Not enough mana to teleport!");
		}
	}

	@Override
	public void specialAttack(AdventureEvent event) {
		player.sendMessage("You must specialize before you get a special attack!");		
	}
}
