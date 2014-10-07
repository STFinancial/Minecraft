package stfadventure.classes.paladin;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import stfadventure.classes.AdventureClass;
import stfadventure.item.ArmorType;
import stfadventure.main.Main;

public class Paladin extends AdventureClass {

	public Paladin(Main plugin, Player player) {
		super(plugin, player);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initializeResource(Main plugin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSkill(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setArmorTypes() {
		armorTypes.add(ArmorType.GOLD);
		armorTypes.add(ArmorType.IRON);
		armorTypes.add(ArmorType.DIAMOND);
	}
}
