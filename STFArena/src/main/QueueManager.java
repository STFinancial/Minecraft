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


	public QueueManager(Main main, DataManager dataManager) {
		plugin = main;
		this.dataManager = dataManager;
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this,
				200,200);
	}

	private void update(){
		update(false);
	}

	private void update(boolean fromScheduler){
		//Here I check queues for how many people they have
		//2 or more i start at the top and work my way down
		//if teams elo are close enough given the time the queue is at and given the number of players online
		//match up those teams and send to matchManager
		//remove from queue(after loop or use iterator)		

		//If queue has person I increment their timers, else reset them	
		for(ArenaTeam t: queue2s){
			if(fromScheduler)
				t.addTime();
			for(UUID p:t.getPlayers()){
				Bukkit.getPlayer(p).sendMessage("This is the queue system, you are currently in queue for 2s with " + t.getName());
				Bukkit.getPlayer(p).sendMessage("You are one of the " + queue2s.size() + " teams in queue for this size");
				Bukkit.getPlayer(p).sendMessage("You have been in queue for about " + t.getTimeInQueue() + " seconds");
			}
		}
		for(ArenaTeam t: queue3s){
			if(fromScheduler)
				t.addTime();
			for(UUID p:t.getPlayers()){
				Bukkit.getPlayer(p).sendMessage("This is the queue system, you are currently in queue for 3s with " + t.getName());
				Bukkit.getPlayer(p).sendMessage("You are one of the " + queue3s.size() + " teams in queue for this size");
				Bukkit.getPlayer(p).sendMessage("You have been in queue for about " + t.getTimeInQueue() + " seconds");
			}
		}
		for(ArenaTeam t: queue5s){
			if(fromScheduler)
				t.addTime();
			for(UUID p:t.getPlayers()){
				Bukkit.getPlayer(p).sendMessage("This is the queue system, you are currently in queue for 5s with " + t.getName());
				Bukkit.getPlayer(p).sendMessage("You are one of the " + queue5s.size() + " teams in queue for this size");
				Bukkit.getPlayer(p).sendMessage("You have been in queue for about " + t.getTimeInQueue() + " seconds");
			}
		}
	}

	public void addTeamToQueue(ArenaTeam t){
		t.resetTime();
		switch(t.getSize()){
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
		switch(t.getSize()){
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
