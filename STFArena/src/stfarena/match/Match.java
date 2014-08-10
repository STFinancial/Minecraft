package stfarena.match;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.avaje.ebean.annotation.PrivateOwned;

import stfarena.arena.Arena;
import stfarena.arena.ArenaPlayer;
import stfarena.arena.ArenaTeam;
import stfarena.arena.ArenaPlayer.Status;
import stfarena.main.Main;

public class Match extends BukkitRunnable {
	public enum MatchStatus {
		IN_PROGRESS, RED_WON, BLUE_WON;
	}
	private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	private final Arena arena;
	private final ArenaTeam redTeam, blueTeam;
	private Team rTeam, bTeam;
	private final MatchListener matchListener;
	private final Set<UUID> redPlayersAlive = new HashSet<UUID>();
	private final Set<UUID> bluePlayersAlive = new HashSet<UUID>();
	private int timeToTeleport = 15, timeToStart = 10;
	private final MatchManager matchManager;
	
	public Match(Main plugin, MatchManager matchManager, Arena arena, ArenaTeam redTeam, ArenaTeam blueTeam) {
		this.matchManager = matchManager;
		this.redTeam = redTeam;
		this.blueTeam = blueTeam;
		this.arena = arena;
		for (ArenaPlayer arenaPlayer : redTeam.getPlayers()) {
			redPlayersAlive.add(arenaPlayer.getUUID());
		}
		for (ArenaPlayer arenaPlayer : blueTeam.getPlayers()) {
			bluePlayersAlive.add(arenaPlayer.getUUID());
		}
		buildScoreboard();
		this.runTaskTimer(plugin, 0, 20);
		matchListener = new MatchListener(this);
		Bukkit.getPluginManager().registerEvents(matchListener, plugin);
	}
	
	private void buildScoreboard() {
		rTeam = scoreboard.registerNewTeam("Red Team");
		bTeam = scoreboard.registerNewTeam("Blue Team");
		rTeam.setAllowFriendlyFire(false);
		bTeam.setAllowFriendlyFire(false);
		rTeam.setPrefix(ChatColor.RED + "Red Team: ");
		bTeam.setPrefix(ChatColor.BLUE + "Blue Team: ");
	}
	
	private void assignScoreboard() {
		for (ArenaPlayer player : redTeam.getPlayers()) {
			rTeam.addPlayer(player.getPlayer());
			player.getPlayer().setScoreboard(scoreboard);
		}
		for (ArenaPlayer player : blueTeam.getPlayers()) {
			bTeam.addPlayer(player.getPlayer());
			player.getPlayer().setScoreboard(scoreboard);
		}
		Objective objective = scoreboard.registerNewObjective("Health", "health");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	private void messagePlayers(String message) {
		for (ArenaPlayer player : redTeam.getPlayers()) {
			player.sendMessage(message);
		}
		for (ArenaPlayer player : blueTeam.getPlayers()) {
			player.sendMessage(message);
		}
	}
	

	
	public void recordDeath(Player player) {
		if (redPlayersAlive.contains(player.getUniqueId())) {
			redPlayersAlive.remove(player.getUniqueId());
			removeScoreboard(player);
			messagePlayers(player.getName() + " on Red team has been slain!");
			returnPlayer(player);
			if (redPlayersAlive.isEmpty()){
				messagePlayers(ChatColor.GREEN + "All members of Red Team have been slain!");
				messagePlayers(ChatColor.GREEN + "Match over, teleporting in 5 seconds");
				for (ArenaPlayer arenaPlayer : blueTeam.getPlayers()) {
					removeScoreboard(arenaPlayer.getPlayer());
				}
				return MatchStatus.BLUE_WON;
			}
		}
		if (bluePlayersAlive.contains(player.getUniqueId())){
			bluePlayersAlive.remove(player.getUniqueId());
			removeScoreboard(player);
			messagePlayers(player.getName() + " on Blue team has been slain!");
			returnPlayer(player);
			if (bluePlayersAlive.isEmpty()){
				messagePlayers(ChatColor.GREEN + "All members of Blue Team have been slain!");
				messagePlayers(ChatColor.GREEN + "Match over, teleporting in 5 seconds");
				for (ArenaPlayer arenaPlayer : redTeam.getPlayers()) {
					removeScoreboard(arenaPlayer.getPlayer());
				}
				return MatchStatus.RED_WON;
			}
		}
		return MatchStatus.IN_PROGRESS;
	}
	
	public void returnPlayer(Player player) {
		ArenaPlayer arenaPlayer = null;
		if (redTeam.hasPlayer(player)) {
			arenaPlayer = redTeam.getPlayer(player);
		}
		if (blueTeam.hasPlayer(player)) {
			arenaPlayer = blueTeam.getPlayer(player);
		}
		arenaPlayer.setFocus(null);
		arenaPlayer.setStatus(Status.FREE);
		arenaPlayer.loadState();
		dataManager.getPlayer(event.getPlayer()).loadState(event.getPlayer());
		event.getPlayer().sendMessage("You have respawned and left the arena");
	}
	
	public void removeScoreboard(Player player) {
		rTeam.removePlayer(player);
		bTeam.removePlayer(player);
		player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		scoreboard.getObjective(DisplaySlot.SIDEBAR).unregister();
	}
	
	public void endMatch() {
		HandlerList.unregisterAll(matchListener);
	}

	@Override
	public void run() {
		if (timeToTeleport > 0) {
			if (timeToTeleport <= 5) {
				messagePlayers("Teleporting to " + arena.getName() + " in " + timeToTeleport + " seconds");
			}
			timeToTeleport--;
		}
		else if (timeToTeleport == 0 && timeToStart > 0) {
			boolean playersValid = true;
			for (ArenaPlayer player : redTeam.getPlayers()) {
				if (Bukkit.getPlayer(id).isValid() == false) {
					redTeam.sendMessage(ChatColor.RED + Bukkit.getPlayer(id).getName() + " is unable to teleport. Match canceled!");
					playersValid = false;
					redTeam.resetTimeInQueue();
				}
			}
			for (UUID id : blueTeam.getPlayers()) {
				if (Bukkit.getPlayer(id).isValid() == false) {
					blueTeam.sendMessage(ChatColor.RED + Bukkit.getPlayer(id).getName() + " is unable to teleport. Match canceled!");
					playersValid = false;
				}
			}
			if (playersValid) {
				arena.teleportTeams(redTeam, blueTeam);
				redTeam.resetTimeInQueue();
				for (UUID id : redTeam.getPlayers()) {
					
				}
				blueTeam.resetTimeInQueue();
			}
			else {
				matchManager.addArena(arena);
				matchManager.cancelMatch(this);
			}
		}
		else {
			
		}
		
	}
}
