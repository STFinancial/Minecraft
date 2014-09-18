package stfadventure.classes.hunter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;

import stfadventure.classes.AdventureClass;
import stfadventure.classes.Resource;
import stfadventure.classes.Resource.ResourceType;
import stfadventure.classes.Skill;
import stfadventure.main.Main;
import stfadventure.main.managers.PlayerManager;

public class Hunter extends AdventureClass {

	public Hunter(Main plugin, Player player) {
		super(plugin, player);
	}

	@Override
	protected void initializeResource(Main plugin) {
		resource = new Resource(plugin, scoreboard, ResourceType.LIFE, 100);
	}

	@Override
	protected void initializeSkills() {
		skills.put(PlayerJoinEvent.class.getName(), new Skill(0, 0));
	}
	
	@Override
	public void getSkill(Event event) {
		switch (event.getEventName()) {
		case "PlayerInteractEvent":
			summonWolf((PlayerInteractEvent) event);
			break;
		case "EntityDamageByEntityEvent":
			bowKnockBack((EntityDamageByEntityEvent) event);
			break;
		}
	}
	
	private void bowKnockBack(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if (player.hasPermission("STFAdventure")) {
				if (player.getItemInHand().getType().equals(Material.BOW)) {
					Vector direction = player.getLocation().getDirection().multiply(5);
					event.getEntity().setVelocity(direction);
				}
			}
		}
	}
	
	private void summonWolf(PlayerInteractEvent event) {
		if (event.getPlayer().hasPermission("STFAdventure")) {
			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				Location location = event.getClickedBlock().getRelative(0, 1, 0).getLocation();
				Wolf wolf = (Wolf) event.getPlayer().getWorld().spawnEntity(location, EntityType.WOLF);
				wolf.setAdult();
				wolf.setOwner(event.getPlayer());
			}
		}
	}
}
