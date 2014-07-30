package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.google.common.cache.AbstractCache.StatsCounter;

public class ArenaPlayer {
	private Status status;
	private String teamFocused;
	private String matchLocation;
	boolean saved;
	private final ArrayList<String> teams;
	private final String name;
	private final UUID uuid;
	private final File playerFile;
	private final YamlConfiguration playerData;
	private Location location = null;

	public ArenaPlayer(Player player) {
		status = Status.FREE;
		teamFocused = null;
		saved = false;
		teams = new ArrayList<String>();
		name = player.getName();
		uuid = player.getUniqueId();
		playerFile = new File(FileManager.getPlayersFolder().getPath() + "/" + name + ".yml");
		playerData = YamlConfiguration.loadConfiguration(playerFile);
	}
	
	public void matchStart(){
		Player player = Bukkit.getPlayer(uuid);
	
		//TODO items checking go here
	
	
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setSaturation(10);
		player.setFireTicks(0);
		for (PotionEffect effect : player.getActivePotionEffects())
	   		player.removePotionEffect(effect.getType());
	}

	public void saveState() {
		Player player = Bukkit.getPlayer(uuid);
		location = player.getLocation();
		List<Object> inventory = new ArrayList<Object>();
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null) {
				inventory.add(item.serialize());
			}
		}
		playerData.set("inventory", inventory);
		
		List<Object> armor = new ArrayList<Object>();
		for (ItemStack item : player.getInventory().getArmorContents()) {
			if (item != null) {
				armor.add(item.serialize());
			}
		}
		playerData.set("armor", armor);
		
		List<Object> potionEffects = new ArrayList<Object>();
		for (PotionEffect potionEffect : player.getActivePotionEffects()) {
			potionEffects.add(potionEffect.serialize());
		}
		playerData.set("potionEffects", potionEffects);//Threw null pointer last game
		
		Map<String, Object> stats = new HashMap<String, Object>();
		stats.put("exhaustion", player.getExhaustion());
		stats.put("foodLevel", player.getFoodLevel());
		stats.put("saturation", player.getSaturation());
		stats.put("exp", player.getExp());
		stats.put("level", player.getLevel());
		stats.put("health", player.getHealth());
		stats.put("remainingAir", player.getRemainingAir());
		stats.put("fallDistance", player.getFallDistance());
		stats.put("fireTicks", player.getFireTicks());	
		
		playerData.createSection("stats", stats);
		
		try {
			playerData.save(playerFile);
		} catch (IOException e) {
			Bukkit.getLogger().info("Unable to save player data for " + name);
		}
		
		saved = true;
	}

	@SuppressWarnings("unchecked")
	public void loadState(Player player) {
		if (saved) {
			
			List<ItemStack> inventory = new ArrayList<ItemStack>();
			for (Object item : playerData.getList("inventory")) {
				inventory.add(ItemStack.deserialize((Map<String, Object>) item));
			}
			player.getInventory().setContents(inventory.toArray(new ItemStack[inventory.size()]));
			
			List<ItemStack> armor = new ArrayList<ItemStack>();
			for (Object item : playerData.getList("armor")) {
				inventory.add(ItemStack.deserialize((Map<String, Object>) item));
			}			
			player.getInventory().setArmorContents(armor.toArray(new ItemStack[armor.size()]));
			
			List<PotionEffect> potionEffects = new ArrayList<PotionEffect>();
			for (Object potionEffect : playerData.getList("potionEffects")) {
				potionEffects.add(new PotionEffect((Map<String, Object>) potionEffect));
			}
			player.addPotionEffects(potionEffects);
			
			for (String key : playerData.getConfigurationSection("stats").getKeys(false)) {
				String rawKey = key;
				key = "stats." + key;
				switch (rawKey) {
				case "exhaustion":
					player.setExhaustion(playerData.getLong(key));
					break;
				case "foodLevel":
					player.setFoodLevel(playerData.getInt(key));
					break;
				case "saturation":
					player.setSaturation(playerData.getLong(key));
					break;
				case "exp":
					player.setExp(playerData.getLong(key));
					break;
				case "level":
					player.setLevel(playerData.getInt(key));
					break;
				case "health":
					player.setHealth(playerData.getDouble(key));
					Bukkit.getLogger().info("" + playerData.getDouble(key));
					break;
				case "remainingAir":
					player.setRemainingAir(playerData.getInt(key));
					break;
				case "fallDistance":
					player.setFallDistance(playerData.getLong(key));
					break;
				case "fireTicks":
					player.setFireTicks(playerData.getInt(key));
					break;
				}
			}
			
			saved = false;
		}
	}
	

	public Location getLocation(){
		return location;	
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getFocus() {
		return teamFocused;
	}

	public void setFocus(String teamName) {
		teamFocused = teamName;
	}

	public void addTeam(String teamName) {
		teams.add(teamName);
	}

	public void removeTeam(String teamName) {
		teams.remove(teamName);
	}

	public ArrayList<String> getTeams() {
		return teams;
	}

	public String getName() {
		return name;
	}

	public UUID getUUID() {
		return uuid;
	}

	public String getArena(){
		return matchLocation;
	}
	public void setArena(String name){
		matchLocation = name;
	}	
}
