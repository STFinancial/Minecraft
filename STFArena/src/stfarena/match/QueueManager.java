package stfarena.match;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Bukkit;

import stfarena.arena.ArenaTeam;
import stfarena.main.Main;

public class QueueManager implements Runnable {
	private final Main plugin;
	private final MatchManager matchManager;
	private Queue<ArenaTeam> queue = new ConcurrentLinkedQueue<ArenaTeam>();
	private int taskID = -1;

	public QueueManager(Main plugin) {
		this.plugin = plugin;
		matchManager = new MatchManager(this);
	}
	
	public void queueTeam(ArenaTeam team) {
		queue.add(team);
		if (queue.size() > 1 && taskID == -1) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 0);
		}
	}
	
	public void cancel(ArenaTeam team) {
		queue.remove(team);
	}

	@Override
	public void run() {
		ArenaTeam redTeam = queue.poll();
		for (ArenaTeam blueTeam : queue) {
			if (redTeam.size() == blueTeam.size() && ArenaTeam.inEloRange(redTeam, blueTeam)) {
				redTeam.sendMessage("Match found against " + blueTeam.name());
				blueTeam.sendMessage("Match found against " + redTeam.name());
			}
		}
	}

}
