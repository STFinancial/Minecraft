package arena;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import arena.ArenaPlayer.Status;

public class MatchManager {

	private Main plugin;
	private DataManager dataManager;
	ArrayList<Arena> arena2sUsed = new ArrayList<Arena>();
	ArrayList<Arena> arena3sUsed = new ArrayList<Arena>();
	ArrayList<Arena> arena5sUsed = new ArrayList<Arena>();
	ArrayList<Arena> arena2sFree = new ArrayList<Arena>();
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

		//@TODO: here we need to add check that all players are alive

		if (possibleArenas.size() > 0) {
			int r = ThreadLocalRandom.current().nextInt(possibleArenas.size());

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


		updateELO(winners, losers);
		
		//TODO do this after a 5 second delay
		for(UUID p:winners.getPlayers()){
			if(Bukkit.getPlayer(p).isOnline()){
				if(Bukkit.getPlayer(p).getHealth() != 0){
					dataManager.getPlayer(p).setStatus(Status.FREE);
					dataManager.getPlayer(p).setFocus(null);
					dataManager.getPlayer(p).setArena(null);
					dataManager.getPlayer(p).loadState(Bukkit.getPlayer(p));
					Bukkit.getPlayer(p).teleport(dataManager.getPlayer(p).getLocation());
					Bukkit.getPlayer(p).sendMessage("You have been teleported out of the arena");
				}
			}
		}
		arena.clean();
	}

	public void recordDeath(Player player){
		if(dataManager.getPlayer(player).getArena() != null)
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

	public void updateELO(ArenaTeam winners, ArenaTeam losers){
		//TODO refine
		double winnerRating = winners.getRating();
		double loserRating = losers.getRating();
		
		//Adjustable Parameters
		int winningKFactor = winners.getNumberOfGames() < 10 ? 80 : 30; //This is essentially the number of points wagered per game
		int losingKFactor = losers.getNumberOfGames() < 10 ? 80 : 30;
		double logFactor = 500.0; 
		//The logFactor signifies how many points of difference are required for a team to be expected to 
		//win 10 times more often than the other team
		
		double winnerEV = 1.0/(1.0 + Math.pow(10.0, ((loserRating - winnerRating) / logFactor)));
		double loserEV = 1.0 - winnerEV;
		
		
		double winnerPoints = (double)winningKFactor * (1.0 - winnerEV);
		double loserPoints = (double)losingKFactor * (0.0 - loserEV);
		
		winners.addMatch(winnerPoints);
		losers.addMatch(loserPoints);
	}

	public void addArena(Arena arena) {
		arena2sFree.add(arena);
	}
	
	public void addArenas(ArrayList<Arena> arenas) {
		arena2sFree.addAll(arenas);
	}

}
