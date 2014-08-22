package stfadventure.events;

import org.bukkit.Bukkit;
import stfadventure.archer.ArcherListener;
import stfadventure.main.Main;
import stfadventure.monk.MonkListener;
import stfadventure.warrior.WarriorListener;

public class ListenerManager {
	private final ArcherListener archerListener;
//	private final WizardListener wizardListener;
	private final WarriorListener warriorListener;
	private final MonkListener monkListener;
	
	public ListenerManager(Main plugin) {
		archerListener = new ArcherListener();
//		wizardListener = new WizardListener();
		warriorListener = new WarriorListener();
		monkListener = new MonkListener();
//		Bukkit.getPluginManager().registerEvents(wizardListener, plugin);
		Bukkit.getPluginManager().registerEvents(archerListener, plugin);
		Bukkit.getPluginManager().registerEvents(warriorListener, plugin);
		Bukkit.getPluginManager().registerEvents(monkListener, plugin);
	}
}
