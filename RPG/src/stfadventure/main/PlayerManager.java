package stfadventure.main;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

import stfadventure.classes.AdventureClass;
import stfadventure.util.TimeUtil;

public class PlayerManager implements Listener, Runnable {
	private final Main plugin;
	private int taskId = -1;
	
	public PlayerManager(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) {
//		setAdventureClass(plugin, event.getPlayer());
//		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, TimeUtil.convertMinutesToTicks(5), TimeUtil.convertMinutesToTicks(5));
	}
	
	@EventHandler
	private void onPlayerQuit(PlayerQuitEvent event) {
//		getAdventureClass(event.getPlayer()).save();
	}
	
//	public static void setAdventureClass(Main plugin, Player player) {
//		AdventureClass adventureClass = AdventureClassFactory.getAdventureClass(plugin, player);
//		FixedMetadataValue data = new FixedMetadataValue(plugin, adventureClass);
//		player.setMetadata("STFAdventureClass", data);
//	}
	
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
		if (Bukkit.getOnlinePlayers().length > 0) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				getAdventureClass(player).save();
			}
		}
		else {
			Bukkit.getScheduler().cancelTask(taskId);
			taskId = -1;
		}
	}
}
