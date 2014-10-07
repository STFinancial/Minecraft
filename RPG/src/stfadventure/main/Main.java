package stfadventure.main;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import stfadventure.classes.AdventureClassType;
import stfadventure.events.EventManager;
import stfadventure.main.managers.CustomEntityType;
import stfadventure.main.managers.PlayerManager;


public class Main extends JavaPlugin {
	private PlayerManager playerManager;
	private EventManager eventManager;
	
	@Override
	public void onEnable() {
		playerManager = new PlayerManager(this);
		eventManager = new EventManager();
		Bukkit.getPluginManager().registerEvents(eventManager, this);
		CustomEntityType.registerEntities();
	}
	
	@Override
	public void onDisable() {
		for (World world : Bukkit.getWorlds()) {
			for (Entity entity : world.getEntities()) {
				if (entity instanceof Skeleton) {
					entity.remove();
				}
			}
		}
//		playerManager.run();
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.removeMetadata("STFAdventure", this);
		}
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);
		CustomEntityType.unregisterEntities();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("class")) {
			PlayerManager.setAdventureClass(this, (Player) sender, AdventureClassType.getAdventureClassType(args[0]));
		}
		if (cmd.getName().equalsIgnoreCase("info")) {
			PlayerManager.getAdventureClass((Player) sender).info();
		}
		return false;		
	}
}
