package main;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

public class PermissionsManager implements Listener {
	private HashMap<UUID, PermissionAttachment> permissions = new HashMap<UUID, PermissionAttachment>();
	private final Main plugin;
	
	public PermissionsManager(Main main) {
		plugin = main;
		new EventManager(main, this);
	}
	
	@EventHandler
	private void onLogin(PlayerJoinEvent event) {
		permissions.put(event.getPlayer().getUniqueId(), event.getPlayer().addAttachment(plugin));
	}
	
	@EventHandler
	private void onQuit(PlayerQuitEvent event) {
		permissions.remove(event.getPlayer().getUniqueId());
	}
	
	public void clearPermissions() {
		permissions.clear();
	}
	
	public boolean check(Player player, int id) {
		PermissionAttachment attachment = permissions.get(player.getUniqueId());
		if (attachment.getPermissions().containsKey(Integer.toString(id)) == false) {
			return true;
		}
		
		return attachment.getPermissions().get(Integer.toString(id));
	}
	
	public boolean check(Player player, Entity entity) {
		PermissionAttachment attachment = permissions.get(player.getUniqueId());
		if (attachment.getPermissions().containsKey(entity.getType().name()) == false) {
			return true;
		}
		
		return attachment.getPermissions().get(entity.getType().name());
	}
	
	public boolean check(Player player, Material material) {
		PermissionAttachment attachment = permissions.get(player.getUniqueId());
		if (attachment.getPermissions().containsKey(material.ordinal()) == false) {
			return true;
		}
		
		return attachment.getPermissions().get(material.name());
	}
}
