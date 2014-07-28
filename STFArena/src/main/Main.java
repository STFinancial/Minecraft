package main;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private DataManager dataManager;
	private EventManager eventManager;
	private CommandManager commandManager;
	private QueueManager queueManager;
	private MatchManager matchManager;

	@Override
	public void onEnable() {
		dataManager = new DataManager(this);
		eventManager = new EventManager(this, dataManager);
		this.getServer().getPluginManager().registerEvents(eventManager, this);
		commandManager = new CommandManager(this, dataManager);
		getCommand("arena").setExecutor(commandManager);
		queueManager = new QueueManager(this, dataManager);
		matchManager = new MatchManager(this, dataManager);
//		matchManager.addArena(new Arena("Test Arena",2, -30, 4, -15, -7, 4, -15, "WOOL", this));
	}

	@Override
	public void onDisable() {
		dataManager.quit();
		eventManager.quit();
		dataManager = null;
		eventManager = null;
		commandManager = null;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public QueueManager getQueueManager() {
		return queueManager;
	}

	public MatchManager getMatchManager() {
		return matchManager;
	}
	
	public DataManager getDataManager(){
		return dataManager;
	}

}
