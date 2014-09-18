package stfadventure.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import stfadventure.main.managers.PlayerManager;

public class EventManager implements Listener {

	@EventHandler
	private void playerJoinEvent(PlayerJoinEvent event) {
		PlayerManager.getAdventureClass(event.getPlayer()).getSkill(event);
	}
}
