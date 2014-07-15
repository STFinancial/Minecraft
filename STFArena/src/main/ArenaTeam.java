package main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

public class ArenaTeam {
	String name;
	ArrayList<UUID> players;
	private int win, loss, rating, size;
	public static DecimalFormat df = new DecimalFormat("00.0");

	public ArenaTeam(String name, int size) {
		this.name = name;
		this.size = size;
		players = new ArrayList<UUID>();
		win = loss = 0;
		rating = 1200;
	}

	public void addPlayer(Player player) {
		players.add(player.getUniqueId());
	}

	public int getSize() {
		return size;
	}

	public boolean isFull() {
		return players.size() == size;
	}

	public int getRating() {
		return rating;
	}

	public String getName() {
		return name;
	}

	public String getRecord() {
		if (win + loss == 0)
			return "Wins: " + win + " Losses: " + loss;

		return "Wins: " + win + " Losses: " + loss + " Win Rate: "
				+ df.format((win * 100.0 / (win + loss))) + "%";
	}

	public String toString() {
		return "Team: " + name + " (" + size + ") Rating: " + rating;
	}

	public ArrayList<UUID> getPlayers() {
		return players;
	}
}
