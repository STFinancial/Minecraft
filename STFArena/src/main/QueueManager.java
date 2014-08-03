package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;

import arena.ArenaMatch;
import arena.ArenaTeam;

import com.avaje.ebeaninternal.server.persist.Constant;

public class QueueManager implements Runnable {
	private static final Integer TICKSPERUPDATE = 20; 
	private final STFArena plugin;
	DataManager dataManager;
	private final Map<ArenaTeam, Integer> inQueue = new HashMap<ArenaTeam, Integer>();
	private final Set<ArenaMatch> inProgress = new HashSet<ArenaMatch>();
	private final Set<Integer> startQueue = new HashSet<Integer>(); 
	int taskID = -1;

	public QueueManager(STFArena plugin, DataManager dataManager) {
		this.plugin = plugin;
	}

	private boolean inEloRange(ArenaTeam teamOne, ArenaTeam teamTwo) {
		double ratingOne = teamOne.getRating();
		double ratingTwo = teamTwo.getRating();
		int waitOne = inQueue.get(teamOne) * TICKSPERUPDATE;
		int waitTwo = inQueue.get(teamTwo) * TICKSPERUPDATE;
		if (Math.abs(ratingOne - ratingTwo) < (15 + waitOne + waitTwo) * 3) {
			return true;
		}
		return false;
	}

	public void queueTeam(ArenaTeam team) {
		if (inProgress.contains(team) == false && inQueue.containsKey(team) == false) {
			inQueue.put(team, 0);
			if (startQueue.contains(team.getSize())) {
				taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, TICKSPERUPDATE);
			}
			else {
				startQueue.add(team.getSize());
			}
		}
	}

	public void dequeueTeam(ArenaTeam team) {
		if (inQueue.size() <= 1) {
			Bukkit.getScheduler().cancelTask(taskID);
		}
	}

	@Override
	public void run() {
		ArenaTeam team = inQueue.keySet().iterator().next();
		for (ArenaTeam otherTeam : inQueue.keySet()) {
			if (otherTeam.equals(team)) {
				
			}
		}
	}
}
