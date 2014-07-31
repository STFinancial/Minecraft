package main;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private DataManager dataManager;
	private EventManager eventManager;
	private CommandManager commandManager;
	private QueueManager queueManager;
	private MatchManager matchManager;
	private FileManager fileManager;

	@Override
	public void onEnable() {
		dataManager = new DataManager(this);
		eventManager = new EventManager(this, dataManager);
		this.getServer().getPluginManager().registerEvents(eventManager, this);
		commandManager = new CommandManager(this, dataManager);
		getCommand("arena").setExecutor(commandManager);
		queueManager = new QueueManager(this, dataManager);
		matchManager = new MatchManager(this, dataManager);
		fileManager = new FileManager();
		dataManager.arenaTeams = fileManager.loadArenaTeams();
		//matchManager.addArenas(fileManager.loadArenas(this));
		matchManager.addArena(new Arena("Sewers 1", 2, -68, 15, 177, -38, 15, 159, "IRON_FENCE", this));
		  
		matchManager.addArena(new Arena("Chum's Forest", 2, 27, 5, 211, 79, 5, 251, "IRON_FENCE", this));
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
