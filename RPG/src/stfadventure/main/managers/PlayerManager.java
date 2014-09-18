package stfadventure.main.managers;

import java.io.IOException;

import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.World;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftHumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

import stfadventure.classes.AdventureClass;
import stfadventure.classes.hunter.Hunter;
import stfadventure.classes.hunter.HunterSkills;
import stfadventure.classes.necromancer.NecroMinion;
import stfadventure.classes.necromancer.NecroSkeleton;
import stfadventure.classes.necromancer.Necromancer;
import stfadventure.classes.wizard.Wizard;
import stfadventure.main.Main;

public class PlayerManager implements Listener, Runnable {
	private final Main plugin;
	private int taskId = -1;
	
	public PlayerManager(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		for (Player player : Bukkit.getOnlinePlayers()) {
			setAdventureClass(plugin, player);
			player.addAttachment(plugin, "STFAdventure", true);
		}
		Bukkit.getPluginManager().registerEvents(new HunterSkills(), plugin);
	}
	
	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) {
//		setAdventureClass(plugin, event.getPlayer());
//		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, TimeUtil.convertMinutesToTicks(5), TimeUtil.convertMinutesToTicks(5));
		setAdventureClass(plugin, event.getPlayer());
		event.getPlayer().addAttachment(plugin, "STFAdventure", true);
	}
	
	@EventHandler
	private void onPlayerQuit(PlayerQuitEvent event) {
//		getAdventureClass(event.getPlayer()).save();
	}
	
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
			}			
		}
//		if (event.getAction().equals(Action.LEFT_CLICK_AIR)) {
//			if (event.getPlayer().getItemInHand().getType().equals(Material.WOOD_SWORD)) {
//				PlayerManager.getAdventureClass(event.getPlayer()).primaryAttack(null);
//			}			
//		}
//		if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
//			PlayerManager.getAdventureClass(event.getPlayer()).secondaryAttack(null);
//		}
	}
	
	public static void setAdventureClass(Main plugin, Player player) {
		FixedMetadataValue data = new FixedMetadataValue(plugin, new Necromancer(plugin, player));
		player.setMetadata("STFAdventureClass", data);
	}
	
	public static AdventureClass getAdventureClass(Player player) {
		return (AdventureClass) player.getMetadata("STFAdventureClass").get(0).value();
	}
	
	public void stop() {
		if (taskId == -1) {
			Bukkit.getScheduler().cancelTask(taskId);
			taskId = -1;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void run() {
//		if (Bukkit.getOnlinePlayers().length > 0) {
//			for (Player player : Bukkit.getOnlinePlayers()) {
//				getAdventureClass(player).save();
//			}
//		}
//		else {
//			Bukkit.getScheduler().cancelTask(taskId);
//			taskId = -1;
//		}
//	}
}
