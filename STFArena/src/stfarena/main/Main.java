package stfarena.main;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private DataManager dataManager;

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
		dataManager.quit();
	}
}
