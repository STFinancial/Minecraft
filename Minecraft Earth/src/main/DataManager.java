package main;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

public class DataManager implements Listener {
	private HashMap<UUID, PermissionAttachment> permissions = new HashMap<UUID, PermissionAttachment>();
	private HashMap<UUID, TechData> playerTechs = new HashMap<UUID, TechData>();
	private HashMap<UUID, AFKWatch> playerAFK = new HashMap<UUID,AFKWatch>();
	private HashMap<String, String> techs;
	private final Main plugin;
	private final FileManager fileManager;
	private final EventManager eventManager;
	
	public DataManager(Main main) {
		plugin = main;
		eventManager = new EventManager(main, this);
		plugin.getServer().getPluginManager().registerEvents(eventManager, plugin);
		fileManager = new FileManager(main);
		techs = fileManager.parseTechs();
		load();
	}
	
	@EventHandler
	private void onLogin(PlayerJoinEvent event) {
		loadPlayerData(event.getPlayer());
	}
	
	@EventHandler
	private void onQuit(PlayerQuitEvent event) {
		removePlayerData(event.getPlayer());
	}
	
	public void load() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			loadPlayerData(player);
		}
	}
	
	public void quit() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			removePlayerData(player);
		}
		permissions.clear();
		playerTechs.clear();
		playerAFK.clear();
	}
	
	private void loadPlayerData(Player player) {
		permissions.put(player.getUniqueId(), player.addAttachment(plugin));
		playerTechs.put(player.getUniqueId(), new TechData(player, this));
		playerAFK.put(player.getUniqueId(), new AFKWatch(player));
	}
	
	private void removePlayerData(Player player) {
		permissions.remove(player.getUniqueId());
		playerTechs.remove(player.getUniqueId());
		playerAFK.remove(player.getUniqueId());
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
	
	public boolean isPlayerAFK(UUID ID){
		if(playerAFK.get(ID) != null){
			return playerAFK.get(ID).AFK;
		}
		return false;
	}
}
