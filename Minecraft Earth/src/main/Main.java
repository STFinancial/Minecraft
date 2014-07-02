package main;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	PermissionsManager pManager;
	
	@Override
	public void onEnable() {
		pManager = new PermissionsManager(this);		
	}
	
	@Override
	public void onDisable() {
		pManager.clearPermissions();
	}
}
