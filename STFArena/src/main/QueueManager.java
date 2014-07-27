package main;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;

public class QueueManager implements Runnable {

	Main plugin;
	DataManager dataManager;
	ArrayList<ArenaTeam> queue2s = new ArrayList<ArenaTeam>();
	ArrayList<ArenaTeam> queue3s = new ArrayList<ArenaTeam>();
	ArrayList<ArenaTeam> queue5s = new ArrayList<ArenaTeam>();
	int taskID;

	public QueueManager(Main plugin, DataManager dataManager) {
		this.plugin = plugin;
		this.dataManager = dataManager;
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 200, 200);
	}

	private void update() {
		update(false);
	}

	private void update(boolean fromScheduler) {
		// Here I check queues for how many people they have
		// 2 or more i start at the top and work my way down
		// if teams elo are close enough given the time the queue is at and
		// given the number of players online
		// match up those teams and send to matchManager
		// remove from queue(after loop or use iterator)

		// If queue has person I increment their timers, else reset them
		if (fromScheduler) {
			for (ArenaTeam t : queue2s) {
				t.addTime();
				for (UUID p : t.getPlayers()) {
					Bukkit.getPlayer(p).sendMessage("This is the queue debug, you are currently in queue for 2s with " + t.getName());
					Bukkit.getPlayer(p).sendMessage("You are one of the " + queue2s.size() + " teams in queue for this size");
					Bukkit.getPlayer(p).sendMessage("You have been in queue for about " + t.getTimeInQueue() + " seconds");
				}
			}
			for (ArenaTeam t : queue3s) {
				t.addTime();
				for (UUID p : t.getPlayers()) {
					Bukkit.getPlayer(p).sendMessage("This is the queue debug, you are currently in queue for 3s with " + t.getName());
					Bukkit.getPlayer(p).sendMessage("You are one of the " + queue3s.size() + " teams in queue for this size");
					Bukkit.getPlayer(p).sendMessage("You have been in queue for about " + t.getTimeInQueue() + " seconds");
				}
			}
			for (ArenaTeam t : queue5s) {
				t.addTime();
				for (UUID p : t.getPlayers()) {
					Bukkit.getPlayer(p).sendMessage("This is the queue debug, you are currently in queue for 5s with " + t.getName());
					Bukkit.getPlayer(p).sendMessage("You are one of the " + queue5s.size() + " teams in queue for this size");
					Bukkit.getPlayer(p).sendMessage("You have been in queue for about " + t.getTimeInQueue() + " seconds");
				}
			}
		}

		for (int i = 0; i < queue2s.size(); i++) {
			for (int j = i + 1; j < queue2s.size(); j++) {
				if (inEloRange(queue2s.get(i), queue2s.get(j))) {
					plugin.getMatchManager().add(queue2s.get(i), queue2s.get(j));
					queue2s.remove(j);
					queue2s.remove(i);
					update();
					return;
				}
			}
		}

		for (int i = 0; i < queue3s.size(); i++) {
			for (int j = i + 1; j < queue3s.size(); j++) {
				if (inEloRange(queue3s.get(i), queue3s.get(j))) {
					plugin.getMatchManager().add(queue3s.get(i), queue3s.get(j));
					queue3s.remove(j);
					queue3s.remove(i);
					update();
					return;
				}
			}
		}

		for (int i = 0; i < queue5s.size(); i++) {
			for (int j = i + 1; j < queue5s.size(); j++) {
				if (inEloRange(queue5s.get(i), queue5s.get(j))) {
					plugin.getMatchManager().add(queue5s.get(i), queue5s.get(j));
					queue5s.remove(j);
					queue5s.remove(i);
					update();
					return;
				}
			}
		}
	}

	private boolean inEloRange(ArenaTeam t1, ArenaTeam t2) {
		int r1 = t1.getRating();
		int r2 = t2.getRating();
		int w1 = t1.getTimeInQueue();
		int w2 = t2.getTimeInQueue();
		int online = Bukkit.getOnlinePlayers().length;
		if (online < 10) {
			if (Math.abs(r1 - r2) < (100 + w1 + w2) * 3) {
				return true;
			}
		} else if (online < 30) {
			if (Math.abs(r1 - r2) < (100 + w1 + w2) * 2) {
				return true;
			}
		} else {
			if (Math.abs(r1 - r2) < (20 + w1 + w2) * 2) {
				return true;
			}
		}
		return false;
	}

	public void addTeamToQueue(ArenaTeam t) {
		t.resetTime();
		switch (t.getSize()) {
		case 2:
			queue2s.add(t);
			break;
		case 3:
			queue3s.add(t);
			break;
		default:
			queue5s.add(t);
			break;
		}
		update();
	}

	public void removeTeamFromQueue(ArenaTeam t) {
		switch (t.getSize()) {
		case 2:
			queue2s.remove(t);
			break;
		case 3:
			queue3s.remove(t);
			break;
		default:
			queue5s.remove(t);
			break;
		}
		update();
	}

	@Override
	public void run() {
		update(true);
	}

}
