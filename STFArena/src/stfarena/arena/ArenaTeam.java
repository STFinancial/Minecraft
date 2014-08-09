package stfarena.arena;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import stfarena.main.FileManager;

public class ArenaTeam {
	private final String name;
	private final List<UUID> players = new ArrayList<UUID>();
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
		for (String uuid : arenaConfiguration.getStringList("players")) {
			players.add(UUID.fromString(uuid));
		}
	}
	
	public void saveTeam() {
		YamlConfiguration arenaConfiguration = YamlConfiguration.loadConfiguration(arenaFile);
		arenaConfiguration.set("name", name);
		arenaConfiguration.set("size", size);
		arenaConfiguration.set("win", win);
		arenaConfiguration.set("loss", loss);
		arenaConfiguration.set("rating", rating);
		arenaConfiguration.set("players", players);
		try {
			arenaConfiguration.save(arenaFile);
		} catch (IOException e) {
			Bukkit.getLogger().info("Unable to save Arena Teams File for " + name);
		}
	}

	public ArenaTeam addPlayer(Player player) {
		players.add(player.getUniqueId());
		return this;
	}

	public int size() {
		return size;
	}

	public boolean isFull() {
		return players.size() == size;
	}

	public double rating() {
		return rating;
	}

	public String name() {
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

	public List<UUID> getPlayers() {
		return players;
	}

	public void resetTimeInQueue() {
		timeInQueue = 0;
	}
	
	public long getTimeInQueue() {
		return (System.currentTimeMillis() - timeInQueue) / 1000;
	}
	
	public void sendMessage(String message) {
		for (UUID id : players) {
			Bukkit.getPlayer(id).sendMessage(message);
		}
	}

	public void addMatch(double eloChange) {
		if(eloChange > 0 ){
			win++;
		}else{
			loss++;
		}
		rating+=eloChange;
		for(UUID p :getPlayers()){
			Bukkit.getPlayer(p).sendMessage("Your arena team " + name + " now has a " + (int) rating + " rating");
			Bukkit.getPlayer(p).sendMessage(record());
		}
	}
	
	public boolean equals(ArenaTeam team) {
		return team.name().equals(name);
	}
	
	public static boolean inEloRange(ArenaTeam redTeam, ArenaTeam blueTeam) {
		double ratingFactor = Math.abs(redTeam.rating() - blueTeam.rating());		
		return ratingFactor < (10 + redTeam.getTimeInQueue() + blueTeam.getTimeInQueue());
	}
}
