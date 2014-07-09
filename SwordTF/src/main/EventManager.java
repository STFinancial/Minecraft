package main;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventManager implements Listener {
	private final Main plugin;

	private static final Set<Material> SWORDS = new HashSet<Material>(Arrays.asList(
			new Material[] {Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.STONE_SWORD, Material.WOOD_SWORD}));

	public EventManager(Main main) {
		plugin = main;
	}

	@EventHandler
	private void onLogin(PlayerJoinEvent event) {
		loadPlayerData(event.getPlayer());
	}

	@EventHandler
	private void onQuit(PlayerQuitEvent event) {
		removePlayerData(event.getPlayer());
	}

	@EventHandler
	private void onPlayerAnimation(PlayerAnimationEvent event){
		UUID playerID = event.getPlayer().getUniqueId();
		if(SWORDS.contains(event.getPlayer().getItemInHand().getType())){
			plugin.getLogger().info(event.getPlayer().getName() + " Has just tried to swing their "+ event.getPlayer().getItemInHand().getType()
					+ "with " + plugin.getDataManager().currentEnergy(playerID)
					+ " energy");
		
			if(plugin.getDataManager().canPlayerSwing(playerID)){
				plugin.getLogger().info("And it was good");
				plugin.getDataManager().swing(playerID);
			}else{
				plugin.getLogger().info("And it failed"); //Need to add DamageEvent and cancel that as well, also block animation packets? for cleanliness, Can't stop client animation
				event.setCancelled(true);
			}
		}
	}

	public void load() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			loadPlayerData(player);
		}
	}

	public void quit() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			removePlayerData(player);
		}
	}

	private void loadPlayerData(Player player) {
		plugin.getDataManager().add(player);
	}

	private void removePlayerData(Player player) {
		plugin.getDataManager().remove(player);
	}


}
