package stfarena.main;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private QueueManager queueManager;

	@Override
	public void onEnable() {
		queueManager = new QueueManager(this);
	}

	@Override
	public void onDisable() {
	}
}
