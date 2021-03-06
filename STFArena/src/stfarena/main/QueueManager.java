package stfarena.main;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;

import stfarena.main.ArenaPlayer.Status;

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
		if (fromScheduler) {
			for (ArenaTeam t : queue2s) {
				t.addTime();
				for (UUID p : t.getPlayers()) {
					Bukkit.getPlayer(p).sendMessage("You are one of the " + queue2s.size() + " teams in queue for 2s");
					Bukkit.getPlayer(p).sendMessage("You have been in queue for about " + t.getTimeInQueue() + " seconds");
				}
			}
			for (ArenaTeam t : queue3s) {
				t.addTime();
				for (UUID p : t.getPlayers()) {
					Bukkit.getPlayer(p).sendMessage("You are one of the " + queue3s.size() + " teams in queue for 3s");
					Bukkit.getPlayer(p).sendMessage("You have been in queue for about " + t.getTimeInQueue() + " seconds");
				}
			}
			for (ArenaTeam t : queue5s) {
				t.addTime();
				for (UUID p : t.getPlayers()) {
					Bukkit.getPlayer(p).sendMessage("You are one of the " + queue5s.size() + " teams in queue for 5s");
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
		double r1 = t1.getRating();
		double r2 = t2.getRating();
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
		for (UUID p : t.getPlayers()) {
			Bukkit.getPlayer(p).sendMessage("Your team " + t.getName() + " has entered queue for " + t.getSize() + "s");
			plugin.getDataManager().getPlayer(p).setStatus(Status.QUEUED);
		}
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
