package stfadventure.events;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import stfadventure.classes.AdventureClass;
import stfadventure.classes.AdventureClassFactory;
import stfadventure.classes.AdventureClassType;
import stfadventure.main.FileManager;

public class EventManager implements Listener {
	private final JavaPlugin plugin;
	
	public EventManager(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) {
		YamlConfiguration playerConfig  = YamlConfiguration.loadConfiguration(FileManager.getPlayerFile(event.getPlayer()));
		try {
			String adventureClass = playerConfig.getString("class");
			setAdventureClass(AdventureClassType.getAdventureClassType(adventureClass), event.getPlayer());
		}
		catch (NullPointerException e) {
			playerConfig.set("class", "BEGINNER");
			setAdventureClass(AdventureClassType.BEGINNER, event.getPlayer());
		}
	}
	
	@EventHandler
	private void attacks(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.LEFT_CLICK_AIR)) {
			getAdventureClass(event.getPlayer()).primaryAttack(null);
		}
		else if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if (event.getPlayer().isSneaking()) {
				getAdventureClass(event.getPlayer()).specialAttack(null);
			}
			else {
				getAdventureClass(event.getPlayer()).secondaryAttack(null);
			}
		}
	}
	
	private void setAdventureClass(AdventureClassType adventureClassType, Player player) {
		AdventureClass adventureClass = AdventureClassFactory.getAdventureClass(adventureClassType, plugin, player);
		FixedMetadataValue data = new FixedMetadataValue(plugin, adventureClass);
		player.setMetadata("STFAdventureClass", data);
	}
	
	private AdventureClass getAdventureClass(Player player) {
		return (AdventureClass) player.getMetadata("STFAdventureClass").get(0).value();
	}
}
