package stfarena.arena;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import stfarena.main.FileManager;

public class ArenaPlayer {
	public enum Status {
		QUEUED, TRYING_TO_QUEUE, IN_GAME, INVITED, CREATING, FREE;
	}
	private Status status = Status.FREE;
	private final List<String> teams = new ArrayList<String>();
	private String currentTeam;
	private final String name;
	private final UUID id;
	private final File playerFile;

	public ArenaPlayer(Player player) {
		name = player.getName();
		id = player.getUniqueId();
		playerFile = new File(FileManager.getPlayersFolder().getPath() + "/" + name + ".yml");
		if (playerFile.exists()) {
			loadState();
		}
	}

	public void saveState() {
		Player player = Bukkit.getPlayer(id);
		Location location = player.getLocation();
		YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

		playerData.createSection("location", saveLocation(location));

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
	}

	@SuppressWarnings("unchecked")
	public void loadState() {
		Player player = Bukkit.getPlayer(id);
		YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
		if (player.isValid()) {
			player.teleport(loadLocation(playerData));
		}

		List<ItemStack> inventory = new ArrayList<ItemStack>();
		for (Object item : playerData.getList("inventory")) {
			inventory.add(ItemStack.deserialize((Map<String, Object>) item));
		}
		player.getInventory().setContents(inventory.toArray(new ItemStack[inventory.size()]));

		List<ItemStack> armor = new ArrayList<ItemStack>();
		for (Object item : playerData.getList("armor")) {
			armor.add(ItemStack.deserialize((Map<String, Object>) item));
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

		playerFile.delete();
	}

	private Map<String, Object> saveLocation(Location playerLocation) {
		Map<String, Object> locationData = new HashMap<String, Object>();
		locationData.put("world", playerLocation.getWorld().getName());
		locationData.put("x", playerLocation.getX());
		locationData.put("y", playerLocation.getY());
		locationData.put("z", playerLocation.getZ());
		locationData.put("yaw", playerLocation.getYaw());
		locationData.put("pitch", playerLocation.getPitch());
		return locationData;
	}

	private Location loadLocation(YamlConfiguration playerData) {
		double x, y, z;
		float yaw, pitch;
		World world = Bukkit.getWorld(playerData.getString("location.world"));
		x = playerData.getDouble("location.x");
		y = playerData.getDouble("location.y");
		z = playerData.getDouble("location.z");
		yaw = playerData.getLong("location.yaw");
		pitch = playerData.getLong("location.pitch");

		try {
			return new Location(world, x, y, z, yaw, pitch);
		}
		catch (Exception e) {
			try {
				Bukkit.getLogger().info("Error loading location of " + name);
				return Bukkit.getPlayer(id).getBedSpawnLocation();
			}
			catch (NullPointerException n) {
				Bukkit.getLogger().info("defaulted to world spawn");
				return Bukkit.getWorlds().get(0).getSpawnLocation();
			}
		}
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getFocus() {
		return currentTeam;
	}

	public void setFocus(String teamName) {
		currentTeam = teamName;
	}

	public void addTeam(String teamName) {
		teams.add(teamName);
	}

	public void removeTeam(String teamName) {
		teams.remove(teamName);
	}

	public List<String> getTeams() {
		return teams;
	}
}
