package stfadventure.classes.wizard;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.Event;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.Score;
import org.bukkit.util.Vector;

import stfadventure.classes.AdventureClass;
import stfadventure.classes.Skill;
import stfadventure.main.Main;

public class Wizard extends AdventureClass {

//	public Wizard(Main plugin, Player player, YamlConfiguration playerConfig) {
//		super(plugin, player, playerConfig);
//	}

	public Wizard(Main plugin, Player player) {
		super(plugin, player);
	}

	@Override
	protected Score getResourceType() {
		return scoreboard.getObjective("resource").getScore(ChatColor.BLUE + "Mana: ");
	}

	@Override
	public void primaryAttack(Event event) {
//		if (resource.subtractAmount(10)) {
			player.setNoDamageTicks(player.getMaximumNoDamageTicks());
			player.setLastDamage(Integer.MAX_VALUE);
			SmallFireball fireball = player.launchProjectile(SmallFireball.class);
			fireball.setShooter(player);
			player.setNoDamageTicks(0);	
//			resource.start(0, 20);
//		}
//		else {
//			player.sendMessage("Not enough mana to shoot fireball!");
//		}
	}

	@Override
	public void secondaryAttack(Event event) {
		Location destination = player.getLocation().add(player.getLocation().getDirection().multiply(5));
		Vector facing = player.getLocation().getDirection();
		destination = destination.getWorld().getHighestBlockAt(destination).getLocation();
		destination.setDirection(facing);
		player.teleport(destination);
//		EnderPearl pearl = player.launchProjectile(EnderPearl.class);
//		pearl.setVelocity(pearl.getVelocity().setY(-0.1));
//			resource.start(0, 20);
//		}
//		else {
//			player.sendMessage("Not enough mana to teleport!");
//		}
	}

	@Override
	public void specialAttack(Event event) {
		player.sendMessage("You must specialize before you get a special attack!");		
	}

	@Override
	protected void initializeScoreboard() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initializeSkills() {
		primarySkill = new Skill(2, 20);
		secondarySkill = new Skill(10, 30);
		specialSkill = null;		
	}
}
