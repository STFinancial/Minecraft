package stfadventure.classes.necromancer;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import stfadventure.classes.AdventureClass;
import stfadventure.classes.Resource.ResourceType;
import stfadventure.item.ArmorType;
import stfadventure.main.Main;

public class Necromancer extends AdventureClass {

	public Necromancer(Main plugin, Player player) {
		super(plugin, player);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initializeResource(Main plugin) {
		resource.setType(ResourceType.ESSENCE, 100, 100, 10);
		
	}

	@Override
	public void getSkill(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setArmorTypes() {
		armorTypes.add(ArmorType.CHAINMAIL);		
	}	
}
