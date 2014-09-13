package stfadventure.wizard;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.Score;

import stfadventure.base.AdventureClass;
import stfadventure.events.AdventureEvent;
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
	public void primaryAttack(AdventureEvent event) {
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
	public void secondaryAttack(AdventureEvent event) {
		if (resource.subtractAmount(30)) {
			player.launchProjectile(EnderPearl.class).setVelocity(player.getLocation().getDirection().normalize());	
			resource.start(0, 20);
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
