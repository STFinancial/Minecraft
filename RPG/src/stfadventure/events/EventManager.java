package stfadventure.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import main.RPGClass;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import stfadventure.wizard.Wizard;

public class EventManager implements Listener {
	private Map<UUID, RPGClass> players = new HashMap<UUID, RPGClass>();

	public boolean addPlayer(UUID id) {
		return players.put(UUID, null)
	}
	
	public boolean addPlayer(Player player) {
		players.add
	}
	
	public void removePlayer() {
		
	}
}
