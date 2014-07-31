package main;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ArenaTeam {
	private final String name;
	private final List<UUID> players = new ArrayList<UUID>();
	private int win, loss, size;
	private double rating;
	public final static DecimalFormat df = new DecimalFormat("00.0");
	private int timeInQueue = 0;
	private final File arenaFile;

	public ArenaTeam(String name, int size) {
		this.name = name;
		this.size = size;
		win = loss = 0;
		rating = 1200;
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
	
	public void save() {
		YamlConfiguration arenaConfiguration = YamlConfiguration.loadConfiguration(arenaFile);
		arenaConfiguration.set("name", name);
		arenaConfiguration.set("win", win);
		arenaConfiguration.set("loss", loss);
		arenaConfiguration.set("rating", rating);
		arenaConfiguration.set("size", size);
		List<String> playerList = new ArrayList<String>();
		for (UUID playerUuid : players) {
			playerList.add(playerUuid.toString());
		}
		arenaConfiguration.set("players", playerList);
		try {
			arenaConfiguration.save(arenaFile);
		} catch (IOException e) {
			Bukkit.getLogger().info("Unable to save Arena file for " + name);
		}
	}

	public ArenaTeam addPlayer(Player player) {
		players.add(player.getUniqueId());
		return this;
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

	public String getRecord() {
		if (win + loss == 0)
			return "Wins: " + win + " Losses: " + loss;

		return "Wins: " + win + " Losses: " + loss + " Win Rate: " + df.format((win * 100.0 / (win + loss))) + "%";
	}
	
	public int getNumberOfGames() {
		return win + loss;
	}

	public String toString() {
		return "Team: " + name + " (" + size + ") Rating: " + rating;
	}

	public List<UUID> getPlayers() {
		return players;
	}

	public void addTime() {
		timeInQueue++;
	}

	public void resetTime() {
		timeInQueue = 0;
	}

	public int getTimeInQueue() {
		if (timeInQueue == 0) {
			return 0;
		}
		return timeInQueue * 10 - 5;
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
			Bukkit.getPlayer(p).sendMessage(getRecord());
		}
	}
}
