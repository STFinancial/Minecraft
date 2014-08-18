package stfadventure.main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import stfadventure.events.EventManager;
import stfadventure.wizard.BlazeMage;
import stfadventure.wizard.FrostMage;
import stfadventure.wizard.Necromancer;
import stfadventure.wizard.Wizard;
import stfadventure.wizard.WizardListener;


public class Main extends JavaPlugin {
	public static Plugin plugin;
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new EventManager(), this);
		Bukkit.getPluginManager().registerEvents(new WizardListener(), this);
		plugin = this;
		for (Player player : Bukkit.getOnlinePlayers()) {
			EventManager.addPlayer(player);
		}
	}
	
	@Override
	public void onDisable() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			EventManager.removePlayer(player);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("class")) {
			EventManager.removePlayer((Player) sender);
			RPGClass rpgClass;
			switch (args[0].toLowerCase()) {
			case "blazemage":
				rpgClass = new BlazeMage((Player) sender, 100, 0);
				break;
			case "frostmage":
				rpgClass = new FrostMage((Player) sender, 100, 0);
				break;
			case "necromancer":
				rpgClass = new Necromancer((Player) sender, 100, 0);
				break;
			case "wizard":
			default:
				rpgClass = new Wizard((Player) sender, 100, 0);
				break;
			}
			EventManager.addPlayer((Player) sender, rpgClass);
			return true;
		} //If this has happened the function will return true. 
	        // If this hasn't happened the value of false will be returned.
		return false; 
	}
}
