package main;

import net.minecraft.server.v1_7_R3.AttributeInstance;
import net.minecraft.server.v1_7_R3.EntityInsentient;
import net.minecraft.server.v1_7_R3.GenericAttributes;

import org.bukkit.craftbukkit.v1_7_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.ItemStack;

public class HorseData {
	private final Horse.Color color;
	private final Horse.Style style;
	private final Horse.Variant variant;
	private final String customName;
	private final boolean visibleName;
	private final ItemStack saddle, armor;
	private final double speed, jump;	
	private final int domestication, maxDomestication, age;
	
	public HorseData(Entity vehicle) {
		Horse horse = (Horse) vehicle;
		color = horse.getColor();
		style = horse.getStyle();
		variant = horse.getVariant();
		customName = horse.getCustomName();
		saddle = horse.getInventory().getSaddle();
		armor = horse.getInventory().getArmor();
		AttributeInstance attributes = ((EntityInsentient)((CraftLivingEntity) horse).getHandle()).getAttributeInstance(GenericAttributes.d);
		speed = attributes.getValue();
		jump = horse.getJumpStrength();
		domestication = horse.getDomestication();
		maxDomestication = horse.getMaxDomestication();
		age = horse.getAge();
		visibleName = horse.isCustomNameVisible();		
	}
	
	public void morphTarget(Horse horse) {
		horse.setAdult();
		horse.setAge(age);
		horse.setColor(color);
		horse.setStyle(style);
		horse.setVariant(variant);
		if (visibleName && customName != null) {
			horse.setCustomName(customName);
			horse.setCustomNameVisible(true);
		}
		AttributeInstance attributes = ((EntityInsentient)((CraftLivingEntity) horse).getHandle()).getAttributeInstance(GenericAttributes.d);
        attributes.setValue(speed);
        horse.getInventory().setArmor(armor);
        horse.getInventory().setSaddle(saddle);
        horse.setJumpStrength(jump);
        horse.setDomestication(domestication);
        horse.setMaxDomestication(maxDomestication);
        horse.setAge(age);        
	}
}
