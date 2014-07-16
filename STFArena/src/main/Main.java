package main;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private DataManager dataManager;
	private EventManager eventManager;
	private CommandManager commandManager;

	@Override
	public void onEnable() {	
		dataManager = new DataManager(this);
		eventManager = new EventManager(this, dataManager);
		this.getServer().getPluginManager().registerEvents(eventManager, this);
		commandManager = new CommandManager(this,dataManager);
		getCommand("arena").setExecutor(commandManager);
	}
	
	@Override
	public void onDisable() {
		dataManager.quit();
		eventManager.quit();
		dataManager = null;
		eventManager = null;
		commandManager = null;
	}	
	
	public CommandManager getCommandManager(){
		return commandManager;
	}
}
