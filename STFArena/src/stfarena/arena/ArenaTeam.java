package stfarena.arena;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import stfarena.arena.ArenaPlayer.Status;
import stfarena.main.FileManager;

public class ArenaTeam {
	private final String name;
	private final Set<ArenaPlayer> players = new HashSet<ArenaPlayer>();
	private final int size;
	private int win = 0, loss = 0;
	private double rating = 1200;
	public final static DecimalFormat df = new DecimalFormat("00.0");
	private long timeInQueue = System.currentTimeMillis();
	private final File arenaFile;

	public ArenaTeam(String name, int size) {
		this.name = name;
		this.size = size;
		arenaFile = new File(FileManager.getTeamsFolder().getPath() + "/" + name + ".yml");
	}
	
	public ArenaTeam(File arenaFile) {
		this.arenaFile = arenaFile;
		YamlConfiguration arenaConfiguration = YamlConfiguration.loadConfiguration(arenaFile);
		this.name = arenaConfiguration.getString("name");
		this.size = arenaConfiguration.getInt("size");
		this.win = arenaConfiguration.getInt("win");
		this.loss = arenaConfiguration.getInt("loss");
		this.rating = arenaConfiguration.getDouble("rating");
		for (String id : arenaConfiguration.getStringList("players")) {
			players.add(new ArenaPlayer(Bukkit.getOfflinePlayer(UUID.fromString(id))));
		}
	}
	
	public void saveTeam() {
		YamlConfiguration arenaConfiguration = YamlConfiguration.loadConfiguration(arenaFile);
		arenaConfiguration.set("name", name);
		arenaConfiguration.set("size", size);
		arenaConfiguration.set("win", win);
		arenaConfiguration.set("loss", loss);
		arenaConfiguration.set("rating", rating);
		List<UUID> playerIds = new ArrayList<UUID>();
		for (ArenaPlayer player : players) {
			playerIds.add(player.getUUID());
		}
		arenaConfiguration.set("players", playerIds);
		try {
			arenaConfiguration.save(arenaFile);
		} catch (IOException e) {
			Bukkit.getLogger().info("Unable to save Arena Teams File for " + name);
		}
	}

	public ArenaTeam addPlayer(Player player) {
		players.add(new ArenaPlayer(player));
		return this;
	}
	
	public boolean hasPlayer(Player player) {
		for (ArenaPlayer arenaPlayer : players) {
			if (arenaPlayer.getUUID().equals(player.getUniqueId())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasPlayer(UUID id) {
		for (ArenaPlayer arenaPlayer : players) {
			if (arenaPlayer.getUUID().equals(id)) {
				return true;
			}
		}
		return false;
	}
	
	public ArenaPlayer getPlayer(Player player) {
		for (ArenaPlayer arenaPlayer : players) {
			if (arenaPlayer.getUUID().equals(player.getUniqueId())) {
				return arenaPlayer;
			}
		}
		return null;
	}
	
	public ArenaPlayer getPlayer(UUID id) {
		for (ArenaPlayer arenaPlayer : players) {
			if (arenaPlayer.getUUID().equals(id)) {
				return arenaPlayer;
			}
		}
		return null;
	}
	
	public void setStatus(Status status) {
		for (ArenaPlayer player : players) {
			player.setStatus(status);
		}
	}

	public int getSize() {
		return size;
	}

	public boolean isFull() {
		return players.size() == size;
	}

	public double getRating() {
		return rating;
	}

	public String getName() {
		return name;
	}

	public String record() {
		if (win + loss == 0)
			return "Wins: " + win + " Losses: " + loss;

		return "Wins: " + win + " Losses: " + loss + " Win Rate: " + df.format((win * 100.0 / (win + loss))) + "%";
	}
	
	public int getNumberOfGames() {
		return win + loss;
	}
	
	public int getNumberOfWins() {
		return win;
	}
	
	public int getNumberOfLosses() {
		return loss;
	}

	public String toString() {
		return "Team: " + name + " (" + size + ") Rating: " + rating;
	}

	public Set<ArenaPlayer> getPlayers() {
		return players;
	}

	public void resetTimeInQueue() {
		timeInQueue = 0;
	}
	
	public long getTimeInQueue() {
		return (System.currentTimeMillis() - timeInQueue) / 1000;
	}
	
	public void sendMessage(String message) {
		for (ArenaPlayer player : players) {
			player.sendMessage(message);
		}
	}

	public void addMatch(double eloChange) {
		if (eloChange > 0) {
			win++;
		}
		else {
			loss++;
		}
		rating += eloChange;
		for(ArenaPlayer player : players){
			player.sendMessage("Your arena team " + name + " now has a " + (int) rating + " rating");
			player.sendMessage(record());
		}
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (getClass() != obj.getClass()) {
	        return false;
	    }
	    final ArenaTeam team = (ArenaTeam) obj;
	    return name.toLowerCase().equals(team.getName().toLowerCase());
	}
	
	@Override
	public int hashCode() {
		return name.toLowerCase().hashCode();		
	}
}
