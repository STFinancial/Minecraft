package arena;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.lang.model.element.Element;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.base.CaseFormat;

import main.MatchManager;
import main.QueueManager;
import main.STFArena;

public class ArenaMatch implements Runnable {
	private final MatchManager matchManager;
	private final ArenaTeam teamOne, teamTwo;
	private final STFArena plugin;
	private final Arena arena;
	private int taskId = -1;
	private int time = 0;
	private boolean matchOver = false;

	public ArenaMatch(STFArena plugin, Arena arena, MatchManager matchManager, ArenaTeam teamOne, ArenaTeam teamTwo) {
		this.plugin = plugin;
		this.arena = arena;
		this.matchManager = matchManager;
		this.teamOne = teamOne;
		this.teamTwo = teamTwo;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 20);
	}

	public Set<ArenaTeam> getTeams() {
		Set<ArenaTeam> teams = new HashSet<ArenaTeam>();
		teams.add(teamOne);
		teams.add(teamTwo);
		return teams;
	}

	public Arena getArena() {
		return arena;
	}

	public ArenaTeam getTeamOne() {
		return teamOne;
	}

	public ArenaTeam getTeamTwo() {
		return teamTwo;
	}

	public void cancelMatch() {
		Bukkit.getScheduler().cancelTask(taskId);
		teamOne.messagePlayers("Match canceled due to invalid player");
		teamTwo.messagePlayers("Match canceled due to invalid player");
		matchManager.sendQueueRequest(this);
	}
	
	public void endMatch() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 20);
	}

	@Override
	public void run() {
		if (matchOver) {
			if (time == 5) {
				teamOne
			}
			

		}
		else {
			switch (time) {
			case 0:
				teamOne.messagePlayers("Match will begin in 15 seconds");
				teamTwo.messagePlayers("Match will begin in 15 seconds");
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
				teamOne.messagePlayers("Match will begin in " + (15 - time) + "seconds");
				break;
			case 15:
				boolean teamOneValid = true;
				boolean teamTwoValid = true;
				for (UUID player : teamOne.getPlayers()) {
					if (Bukkit.getPlayer(player).isValid() == false) {
						cancelMatch();
						teamOneValid = false;
						break;
					}
				}
				for (UUID player : teamTwo.getPlayers()) {
					if (Bukkit.getPlayer(player).isValid() == false) {
						cancelMatch();
						teamTwoValid = false;
						break;
					}
				}
				if (teamOneValid && teamTwoValid) {
					arena.startMatch();
					time = 0;
				}			
				break;		
			}		
		}

		time = (time + 20) / 20;
	}

}
