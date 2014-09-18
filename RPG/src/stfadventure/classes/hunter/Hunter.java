package stfadventure.classes.hunter;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import stfadventure.classes.AdventureClass;
import stfadventure.classes.Resource;
import stfadventure.classes.Resource.ResourceType;
import stfadventure.classes.Skill;
import stfadventure.main.Main;

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
}
