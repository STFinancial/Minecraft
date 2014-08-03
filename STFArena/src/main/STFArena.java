package main;

import org.bukkit.plugin.java.JavaPlugin;

public class STFArena extends JavaPlugin {
	private DataManager dataManager;
	private EventManager eventManager;
	private CommandManager commandManager;
	private FileManager fileManager;

	@Override
	public void onEnable() {
		fileManager = new FileManager();
		
		dataManager = new DataManager(this);	
		dataManager.arenaTeams = fileManager.loadArenaTeams();
		dataManager.updateLadder();
		
		eventManager = new EventManager(this, dataManager);
		this.getServer().getPluginManager().registerEvents(eventManager, this);
		
		commandManager = new CommandManager(this, dataManager);
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
}
