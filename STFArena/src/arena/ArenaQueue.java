package arena;

import java.util.HashSet;
import java.util.Set;

import main.DataManager;
import main.STFArena;

import org.bukkit.Bukkit;

public class ArenaQueue {

	private final STFArena plugin;
	private final DataManager dataManager;
	
	public ArenaQueue(STFArena plugin, DataManager dataManager) {
		this.plugin = plugin;
		this.dataManager = dataManager;
	}
	
	public void addTeam(ArenaTeam team) {
		ArenaTeam removeTeam = null;
		for (ArenaTeam inQueue : queue) {
			if (inQueue.getSize() == team.getSize()) {
				removeTeam = inQueue;
				inProgress.add(new ArenaMatch(team, inQueue));
				break;
			}
		}
		queue.remove(removeTeam);		
	}
	
	public void removeTeam(ArenaTeam team) {
		if (queue.contains(team)) {
			queue.remove(team);
		}
	}
	
	public void cancelMatch(ArenaMatch match) {
		if (inProgress.contains(match)) {
			inProgress.removeAll(match.getTeams());
			queue.addAll(match.getTeams());
			Bukkit.getLogger().info("Match sucessfully canceled!");
		}
		else {
			Bukkit.getLogger().info("Match not found!");
		}
	}
	
	private void match() {
		
	}
}
