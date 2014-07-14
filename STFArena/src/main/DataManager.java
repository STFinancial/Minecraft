package main;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DataManager {
	private final Main plugin;
	HashMap<String, ArenaTeam> arenaTeams;
	HashMap<UUID, ArenaPlayer> arenaPlayers;

	public DataManager(Main plugin) {
		this.plugin = plugin;
		load();
	}

	private void load() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			add(player);
		}
	}

	public void quit() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			remove(player);
		}
	}

	public void remove(Player player) {
		arenaPlayers.put(player.getUniqueId(), new ArenaPlayer(player));
	}

	public void add(Player player) {
		arenaPlayers.remove(player.getUniqueId());
	}
	
	public boolean playerIsOnTeam(Player player, String teamName){
		return false;
	}

	public void disband(String string) {
		// TODO Auto-generated method stub
	}
	
	public Status getStatus(Player player){
		return arenaPlayers.get(player.getUniqueId()).getStatus();
	}

	public void declineInvite(Player player) {
		// TODO Auto-generated method stub
		
	}

	public void forfeit(Player player) {
		// TODO Auto-generated method stub
		
	}

	public void dodgeQueue(Player player) {
		// TODO Auto-generated method stub
		
	}

	public void stopQueue(Player player) {
		// TODO Auto-generated method stub
		
	}

	public void acceptQueue(Player player) {
		// TODO Auto-generated method stub
		
	}

	public void acceptInvite(Player player) {
		// TODO Auto-generated method stub
		
	}

	
}
