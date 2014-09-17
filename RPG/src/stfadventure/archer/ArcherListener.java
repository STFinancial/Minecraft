package stfadventure.archer;

import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.World;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftHumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import stfadventure.classes.necromancer.NecroMinion;
import stfadventure.classes.necromancer.NecroSkeleton;
import stfadventure.item.ItemUtil;
import stfadventure.main.PlayerManager;
import stfadventure.resource.ResourceType;

public class ArcherListener implements Listener {

	@EventHandler
	public void spawnFriendlyZombie(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (event.getPlayer().getItemInHand().getType().equals(Material.WOOD_SWORD)) {
				World world = ((CraftWorld) event.getPlayer().getWorld()).getHandle();
				EntityHuman necromancer = ((CraftHumanEntity) event.getPlayer()).getHandle();
				NecroMinion minion = new NecroSkeleton(world);
				minion.setOwner(necromancer);
				//			minion = ThreadLocalRandom.current().nextBoolean()? new NecroSkeleton(world) : new NecroZombie(world);
				Location l = event.getClickedBlock().getLocation().add(0, 1, 0);
				((EntityCreature) minion).setLocation(l.getX(), l.getY(), l.getZ(), l.getPitch(), l.getYaw());
				world.addEntity(((EntityCreature) minion));

				ItemUtil.setDisplayName(event.getPlayer().getItemInHand(), "test");
				event.getPlayer().setItemInHand(ItemUtil.testAttribute(event.getPlayer().getItemInHand()));
//				ItemUtil.setResource(event.getPlayer().getItemInHand(), ResourceType.ENERGY);
			}			
		}
		if (event.getAction().equals(Action.LEFT_CLICK_AIR)) {
			if (event.getPlayer().getItemInHand().getType().equals(Material.WOOD_SWORD)) {
				PlayerManager.getAdventureClass(event.getPlayer()).primaryAttack(null);
			}			
		}
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			PlayerManager.getAdventureClass(event.getPlayer()).secondaryAttack(null);
		}
	}

}
