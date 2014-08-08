package qsik;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;

import arena.ArenaTeam;

public class ArenaQueue {
	private static final Set<ArenaTeam> queue = new HashSet<ArenaTeam>();
	private static final Set<ArenaMatch> inProgress = new HashSet<ArenaMatch>();
	
	public static void addTeam(ArenaTeam team) {
		ArenaTeam removeTeam = null;
		for (ArenaTeam inQueue : queue) {
			if (inQueue.getSize() == team.getSize()) {
				removeTeam = inQueue;
				inProgress.add(new ArenaMatch(null, null, team, inQueue));
				break;
			}
		}
		queue.remove(removeTeam);		
	}
	
	public static void removeTeam(ArenaTeam team) {
		if (queue.contains(team)) {
			queue.remove(team);
		}
	}
	
	public static void cancelMatch(ArenaMatch match) {
		if (inProgress.contains(match)) {
			inProgress.removeAll(match.getTeams());
			queue.addAll(match.getTeams());
			Bukkit.getLogger().info("Match sucessfully canceled!");
		}
		else {
			Bukkit.getLogger().info("Match not found!");
		}
	}
}
