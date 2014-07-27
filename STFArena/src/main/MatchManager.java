package main;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

//@TODO massive work in progress
public class MatchManager {

	private Main plugin;
	private DataManager dataManager;
	ArrayList<Arena> arena2sUsed = new ArrayList<Arena>();
	ArrayList<Arena> arena3sUsed = new ArrayList<Arena>();
	ArrayList<Arena> arena5sUsed = new ArrayList<Arena>();
	static ArrayList<Arena> arena2sFree = new ArrayList<Arena>();
	ArrayList<Arena> arena3sFree = new ArrayList<Arena>();
	ArrayList<Arena> arena5sFree = new ArrayList<Arena>();

	public MatchManager(Main plugin, DataManager dataManager) {
		this.plugin = plugin;
		this.dataManager = dataManager;
	}

	public void add(ArenaTeam t1, ArenaTeam t2) {
		switch (t1.getSize()) {
		case 2:
			add(t1, t2, arena2sFree, arena2sUsed);
			break;
		case 3:
			add(t1, t2, arena3sFree, arena3sUsed);
			break;
		default:
			add(t1, t2, arena5sFree, arena5sUsed);
			break;
		}
	}

	private void add(ArenaTeam t1, ArenaTeam t2, ArrayList<Arena> possibleArenas, ArrayList<Arena> usedArenas) {
		if (possibleArenas.size() > 0) {

			int r = (int) Math.random() * possibleArenas.size();
		
			for (UUID p : t1.getPlayers()) {
				Bukkit.getPlayer(p).sendMessage("A match has been found against " + t2.getName());
				dataManager.getPlayer(p).setStatus(Status.IN_GAME);
				dataManager.getPlayer(p).setArena(possibleArenas.get(r).getName());
			}
			for (UUID p : t2.getPlayers()) {
				Bukkit.getPlayer(p).sendMessage("A match has been found against " + t1.getName());
				dataManager.getPlayer(p).setStatus(Status.IN_GAME);
				dataManager.getPlayer(p).setArena(possibleArenas.get(r).getName());
			}
			
			if (Math.random() > .5) {
				possibleArenas.get(r).add(t1, t2);
			} else {
				possibleArenas.get(r).add(t2, t1);
			}
			
			usedArenas.add(possibleArenas.get(r));
			possibleArenas.remove(r);
		} else {
			Bukkit.getLogger().info("Massive Failure, no arenas available");
		}
	}

	public void gameFinished(Arena arena, boolean redWon) {
		switch (arena.getSize()) {
		case 2:
			arena2sUsed.remove(arena);
			arena2sFree.add(arena);
			break;
		case 3:
			arena3sUsed.remove(arena);
			arena3sFree.add(arena);
			break;
		case 5:
			arena5sUsed.remove(arena);
			arena5sFree.add(arena);
			break;

		}
		ArenaTeam winners, losers;
		if(redWon){
			winners = arena.getRedTeam();
			losers = arena.getBlueTeam();
		}else{
			losers = arena.getRedTeam();
			winners = arena.getBlueTeam();
		}
		
	
		//Calculate mmr differences
		int eloChange = eloChange(winners.getRating(), losers.getRating());
		winners.addMatch(eloChange);
		losers.addMatch(-1*eloChange);
		for(UUID p:winners.getPlayers()){
			if(Bukkit.getPlayer(p).getHealth() != 0){
				dataManager.getPlayer(p).setStatus(Status.FREE);
				dataManager.getPlayer(p).setFocus(null);
				dataManager.getPlayer(p).loadState();
				Bukkit.getPlayer(p).sendMessage("You have been teleported out of the arena");
			}
		}
		arena.clean();
	}
	
	public void recordDeath(Player player){
		getArena(dataManager.getPlayer(player).getFocus()).recordDeath(player.getUniqueId());
	}
	
	public Arena getArena(String teamName){
		for(Arena a:arena2sUsed){
			if(a.getRedTeam().getName().equals(teamName) || a.getBlueTeam().getName().equals(teamName)){
				return a;
			}
		}
		for(Arena a:arena3sUsed){
			if(a.getRedTeam().getName().equals(teamName) || a.getBlueTeam().getName().equals(teamName)){
				return a;
			}
		}
		for(Arena a:arena5sUsed){
			if(a.getRedTeam().getName().equals(teamName) || a.getBlueTeam().getName().equals(teamName)){
				return a;
			}
		}
		return null;
	}

	public int eloChange(int winning, int losing){
		if(Math.abs(winning - losing) < 100){
			return 10;
		}else{
			if(winning > losing){
				return 5;
			}else{
				return 15;
			}
		}
		
	}

	public static void addArena(Arena arena) {
		arena2sFree.add(arena);
	}
	
}
