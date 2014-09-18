package stfadventure.classes.necromancer;

import org.bukkit.entity.Player;
import stfadventure.classes.AdventureClass;
import stfadventure.classes.Resource;
import stfadventure.classes.Resource.ResourceType;
import stfadventure.main.Main;

public class Necromancer extends AdventureClass {

	public Necromancer(Main plugin, Player player) {
		super(plugin, player);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initializeResource(Main plugin) {
		resource = new Resource(plugin, scoreboard, ResourceType.ESSENCE, 100);
		
	}

	@Override
	protected void initializeSkills() {
		// TODO Auto-generated method stub
		
	}	
}
