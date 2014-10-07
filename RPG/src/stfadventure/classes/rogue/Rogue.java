package stfadventure.classes.rogue;

import net.minecraft.server.v1_7_R4.Block;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import stfadventure.classes.AdventureClass;
import stfadventure.classes.Resource.ResourceType;
import stfadventure.item.ArmorType;
import stfadventure.main.Main;

public class Rogue extends AdventureClass {

	public Rogue(Main plugin, Player player) {
		super(plugin, player);
	}

	@Override
	protected void initializeResource(Main plugin) {
		resource.setType(ResourceType.ENERGY, 0, 100, 10);		
	}

	@Override
	public void getSkill(Event event) {
		// TODO Auto-generated method stub
		
	}
	
	private void poisonedBlade(EntityDamageByEntityEvent event) {
		
	}
	
	private void blockAttack(EntityDamageByEntityEvent event) {
		
	}
	
	private void sneak(PlayerToggleSneakEvent event) {
		
	}

	@Override
	public void setArmorTypes() {
		armorTypes.add(ArmorType.NONE);
	}
}
