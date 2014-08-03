package arena;

import java.util.HashSet;
import java.util.Set;

import main.QueueManager;

public class ArenaMatch implements Runnable {
	private final QueueManager queueManager;
	private final ArenaTeam team1, team2;
	private int taskId = -1;
	
	public ArenaMatch(QueueManager queueManager, ArenaTeam team1, ArenaTeam team2) {
		this.queueManager = queueManager;
		this.team1 = team1;
		this.team2 = team2;
	}
	
	public Set<ArenaTeam> getTeams() {
		Set<ArenaTeam> teams = new HashSet<ArenaTeam>();
		teams.add(team1);
		teams.add(team2);
		return teams;
	}
	
	public ArenaTeam getTeamOne() {
		return team1;
	}
	
	public ArenaTeam getTeamTwo() {
		return team2;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
