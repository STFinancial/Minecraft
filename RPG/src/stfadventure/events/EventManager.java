package stfadventure.events;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;

import stfadventure.main.Main;
import stfadventure.main.managers.PlayerManager;

public class EventManager implements Listener {
	private final ProtocolManager protocolManager;
	private final Main plugin;
	
	public EventManager(Main plugin, ProtocolManager protocolManager) {
		this.plugin = plugin;
		this.protocolManager = protocolManager;
	}

	@EventHandler
	private void playerJoinEvent(PlayerJoinEvent event) {
		PlayerManager.getAdventureClass(event.getPlayer()).getSkill(event);
	}
	
	@EventHandler
	private void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if (player.hasPermission("STFAdventure")) {
				PlayerManager.getAdventureClass(player).getSkill(event);
			}
		}
		else if (event.getDamager() instanceof Projectile) {
			Player player = (Player) ((Projectile) event.getDamager()).getShooter();
			if (player.hasPermission("STFAdventure")) {
				PlayerManager.getAdventureClass(player).getSkill(event);
			}
		}
	}
	
	@EventHandler
	private void playerInteractEvent(PlayerInteractEvent event) {
		if (event.getPlayer().hasPermission("STFAdventure")) {
			PlayerManager.getAdventureClass(event.getPlayer()).getSkill(event);
		}
	}
}
