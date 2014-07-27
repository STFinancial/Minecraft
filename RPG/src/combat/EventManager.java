package combat;

import java.util.HashMap;
import java.util.Map;

import main.RPGClass;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import wizard.Wizard;

public class EventManager implements Listener {
	private static Map<Player, RPGClass> players = new HashMap<Player, RPGClass>();

	public static void addPlayer(Player player) {
		players.put(player, new Wizard(player, 100, 0));		
	}
	
	public static void addPlayer(Player player, RPGClass rpgClass){
		players.put(player, rpgClass);
	}
	

	public static void removePlayer(Player player) {
		players.remove(player);		
	}
	
	public static RPGClass getPlayer(Player player) {
		return players.get(player);
	}

	@EventHandler
	private static void playerLogin(PlayerLoginEvent event) {
		addPlayer(event.getPlayer());
	}

	@EventHandler
	private static void playerQuit(PlayerQuitEvent event) {
		removePlayer(event.getPlayer());
	}
}
