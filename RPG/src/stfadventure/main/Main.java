package stfadventure.main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import stfadventure.classes.AdventureClassType;
import stfadventure.events.ListenerManager;


public class Main extends JavaPlugin {
	private PlayerManager playerManager;
	
	@Override
	public void onEnable() {
		playerManager = new PlayerManager(this);
		new ListenerManager(this);
	}
	
	@Override
	public void onDisable() {
		playerManager.run();
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("class")) {
			YamlConfiguration playerConfig = FileManager.getPlayerConfig((Player) sender);
			playerConfig.set("class", args[0].toUpperCase());
			playerConfig.lo
			PlayerManager.setAdventureClass(this, (Player) sender);
		}
		if (cmd.getName().equalsIgnoreCase("info")) {
			PlayerManager.getAdventureClass((Player) sender).info();
		}
		return false;		
	}
}
