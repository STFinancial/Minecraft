package stfarena.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import stfarena.arena.ArenaPlayer;
import stfarena.arena.ArenaTeam;
import stfarena.match.MatchManager;

public class QueueManager implements Runnable, Listener {
	private final Main plugin;
	private final CommandManager commandManager;
	private final MatchManager matchManager;
	private final TeamCreator teamCreator;
	private final Map<String, ArenaTeam> arenaTeams = new HashMap<String, ArenaTeam>();
	private final Map<UUID, ArenaPlayer> arenaPlayers = new HashMap<UUID, ArenaPlayer>();
	private final Queue<ArenaTeam> queue = new ConcurrentLinkedQueue<ArenaTeam>(); 
	private int taskID = -1;

	public QueueManager(Main plugin) {
		this.plugin = plugin;
		commandManager = new CommandManager(plugin, this);
		plugin.getCommand("arena").setExecutor(commandManager);
		matchManager = new MatchManager(plugin, this);
		teamCreator = new TeamCreator(this);
		Bukkit.getPluginManager().registerEvents(this, plugin);
		for (File file : FileManager.getTeamsFolder().listFiles()) {
			if (file.isFile() && file.getPath().contains(".yml")) {
				ArenaTeam team = new ArenaTeam(file);
				arenaTeams.put(team.name(), team);
			}
		}
	}
	
	@EventHandler
	private void addArenaPlayer(PlayerJoinEvent event) {
		arenaPlayers.put(event.getPlayer().getUniqueId(), new ArenaPlayer(event.getPlayer()));
	}
	
	@EventHandler
	private void removeArenaPlayer(PlayerQuitEvent event) {
		arenaPlayers.remove(event.getPlayer().getUniqueId());
	}
	

	@EventHandler
	private void preventBlockBreak(BlockBreakEvent event) {
		if (inArenaWorld(event.getPlayer())) {
			if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE) == false) {
				event.setCancelled(true);
			}
		}
	}
	
	public ArenaPlayer getPlayer(UUID id) {
		return arenaPlayers.get(id);
	}
	
	public void queueTeam(ArenaTeam team) {
		queue.add(team);
		if (taskID != -1 && queue.size() > 1) {
			checkQueue();
		}
	}
	
	public void removeTeam(ArenaTeam team) {
		queue.remove(team);
	}

	@Override
	public void run() {
		Queue<ArenaTeam> currentQueue = new ConcurrentLinkedQueue<ArenaTeam>();
		currentQueue.addAll(queue);
		int n = currentQueue.size() * (currentQueue.size() - 1) / 2;
		for (int i = 0; i < n || currentQueue.isEmpty(); i++) {
			ArenaTeam redTeam = currentQueue.poll();
			ArenaTeam matchedTeam = null;
			for (ArenaTeam blueTeam : queue) {
				if (redTeam.size() == blueTeam.size()) {
					if (matchedTeam == null) {
						matchedTeam = blueTeam;
					}
					else if (eloRange(redTeam, blueTeam) < eloRange(redTeam, matchedTeam)) {
						matchedTeam = blueTeam;
					}
					else {
						blueTeam.sendMessage("You have been in queue for " + redTeam.getTimeInQueue() + " seconds");
					}
				}
			}
			if (matchedTeam != null) {
				matchManager.createMatch(redTeam, matchedTeam);
				queue.remove(matchedTeam);
			}
			else {
				redTeam.sendMessage("You have been in queue for " + redTeam.getTimeInQueue() + " seconds");
				currentQueue.add(redTeam);
			}
		}
	}
	
	private double eloRange(ArenaTeam redTeam, ArenaTeam blueTeam) {
		double ratingFactor = Math.abs(redTeam.rating() - blueTeam.rating());		
		return ratingFactor * (30 - redTeam.getTimeInQueue() - blueTeam.getTimeInQueue());
	}
	
	private void checkQueue() {
		Set<Integer> teamSizes = new HashSet<Integer>();
		for (ArenaTeam team : queue) {
			if (teamSizes.contains(team.size())) {
				Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 20);
				break;
			}
			else {
				teamSizes.add(team.size());
			}
		}
	}

}
