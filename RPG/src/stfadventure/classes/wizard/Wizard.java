package stfadventure.classes.wizard;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import stfadventure.classes.AdventureClass;
import stfadventure.classes.Resource.ResourceType;
import stfadventure.item.ArmorType;
import stfadventure.main.Main;

public abstract class Wizard extends AdventureClass {

	public Wizard(Main plugin, Player player) {
		super(plugin, player);
	}


	@Override
	public void setArmorTypes() {
		armorTypes.add(ArmorType.CHAINMAIL);
	}

	@Override
	protected void initializeResource(Main plugin) {
		resource.setType(ResourceType.MANA, 100, 100, 5);		
	}

	@Override
	public void getSkill(Event event) {
		switch (event.getEventName()) {
		case "PlayerInteractEvent":
			teleport((PlayerInteractEvent) event);
			break;
		}

	}

	public void teleport(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			Location destination = player.getLocation().add(player.getLocation().getDirection().multiply(5));
			Vector facing = player.getLocation().getDirection();
			destination = destination.getWorld().getHighestBlockAt(destination).getLocation();
			destination.setDirection(facing);
			player.teleport(destination);
		}
	}
}
