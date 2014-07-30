package main;

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
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
		if (playerFile.exists()) {
			saved = true;
			loadState(player);
		}
	}

	public void matchStart(){
		Player player = Bukkit.getPlayer(uuid);

		//TODO items checking go here
		Inventory inv = player.getInventory();
		ItemStack[] contents = inv.getContents();
		ItemStack[] banned = new ItemStack[11];
		banned[0] = new ItemStack(Material.ENDER_PEARL, 1);
		banned[1] = new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1);
		banned[2] = new ItemStack(Material.GOLDEN_APPLE, 1, (short) 0);
		banned[3] = new ItemStack(Material.POTION, 1, (short) 8238);
		banned[4] = new ItemStack(Material.POTION, 1, (short) 8270);
		banned[5] = new ItemStack(Material.POTION, 1, (short) 16430);
		banned[6] = new ItemStack(Material.POTION, 1, (short) 16462);
		banned[7] = new ItemStack(Material.FLINT_AND_STEEL);
		banned[8] = new ItemStack(Material.BUCKET);
		banned[9] = new ItemStack(Material.LAVA_BUCKET);
		banned[10] = new ItemStack(Material.WATER_BUCKET);
		
		for (int i = 0; i < contents.length; i++) {
			ItemStack item = contents[i];

			if (item == null) {

			}else{
				for(ItemStack b:banned){
					if(item != null){
						if(item.getType().equals(b.getType())){
							if(item.getType() == Material.FLINT_AND_STEEL){
								player.sendMessage("removed banned item " + b.getType());
								player.getInventory().remove(new ItemStack(item.getType(), item.getAmount(), item.getDurability()));
							}else if(item.getDurability() == b.getDurability()){
								player.sendMessage("removed banned item " + b.getType() + ":"  + b.getDurability());
								player.getInventory().remove(new ItemStack(item.getType(), item.getAmount(), item.getDurability()));
							}
						}
					}
				}
			} 
		}
		contents = inv.getContents();
		int counter = 0;
		for (int i = 0; i < contents.length; i++) {
			ItemStack item = contents[i];

			if (item == null) {

			}else{
				if(item.getType().equals(Material.POTION)){
					counter++;
					if(counter > 3){
						player.sendMessage("You exceeded 3 potion limit");
						player.getInventory().remove(new ItemStack(item.getType(), item.getAmount(), item.getDurability()));
					}
				}

			} 
		}

		player.setHealth(20);
		player.setFoodLevel(20);
		player.setSaturation(1);
		player.setFireTicks(0);
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
	}

	public void saveState() {
		Player player = Bukkit.getPlayer(uuid);
		location = player.getLocation();

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

		saved = true;
	}

	@SuppressWarnings("unchecked")
	public void loadState(Player player) {
		if (saved) {

			location = loadLocation();
			
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
			saved = false;
		}
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

	private Location loadLocation() {
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
				return Bukkit.getPlayer(uuid).getBedSpawnLocation();
			}
			catch (NullPointerException n) {
				Bukkit.getLogger().info("defaulted to world spawn");
				return Bukkit.getWorlds().get(0).getSpawnLocation();
			}
		}
	}

	public Location getLocation() {
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
