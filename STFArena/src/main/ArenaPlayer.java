package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

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

	public void saveState() {
		Player player = Bukkit.getPlayer(uuid);
		
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
		playerData.set("potionEffects", potionEffects);
		
		Map<String, Object> stats = new HashMap<String, Object>();
		stats.put("exhaustion", player.getExhaustion());
		stats.put("foodLevel", player.getFoodLevel());
		stats.put("saturation", player.getSaturation());
		stats.put("totalExp", player.getTotalExperience());
		stats.put("health", player.getHealth());
		stats.put("remainingAir", player.getRemainingAir());
		stats.put("fallDistance", player.getFallDistance());
		stats.put("fireTicks", player.getFireTicks());		
		playerData.set("stats", stats);
		
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
				switch (key) {
				case "exhaustion":
					player.setExhaustion(playerData.getLong(key));
					break;
				case "foodLevel":
					player.setFoodLevel(playerData.getInt(key));
					break;
				case "saturation":
					player.setSaturation(playerData.getLong(key));
					break;
				case "totalExp":
					player.setTotalExperience(playerData.getInt(key));
					break;
				case "health":
					player.setHealth(playerData.getDouble(key));
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
				default:
					break;
				}
			}
			
			saved = false;
		}
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
