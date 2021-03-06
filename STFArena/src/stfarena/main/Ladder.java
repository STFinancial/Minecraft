package stfarena.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Ladder {
	
	private ArrayList<ArenaTeam> ladder;

	public Ladder(Map<String, ArenaTeam> arenaTeams) {
		ladder = new ArrayList<ArenaTeam>();
		
		for (Map.Entry<String, ArenaTeam> entry: arenaTeams.entrySet()) {
			ladder.add(entry.getValue());
		}
		
		sortLadder();
	}
	
	public void addTeam(ArenaTeam team) {
		//addTeam sorts the list
		ladder.add(team);
		sortLadder();
	}
	
	public ArrayList<ArenaTeam> getLadder() {
		return ladder;
	}
	
	public List<ArenaTeam> getTop() {	
		return ladder.subList(0, 5);		
	}
	

	public void removeTeam(ArenaTeam team) {
		ladder.remove(team);
	}
	
	
	public void sortLadder() {
		boolean swapped = true;
		int length = ladder.size();
		while (swapped) {
			swapped = false;
			for (int i = 1; i < length; ++i) {
				if (ladder.get(i - 1).getRating() < ladder.get(i).getRating()) {
					Collections.swap(ladder, i, i-1);
					swapped = true;
				}
			}
			length = length - 1;
		}
	}
}
