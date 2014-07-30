package main;

import java.util.HashSet;
import java.util.Set;

public class ArenaMatch {
	private final ArenaTeam team1, team2;
	
	public ArenaMatch(ArenaTeam team1, ArenaTeam team2) {
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

}
