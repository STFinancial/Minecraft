package stfarena.match;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import stfarena.arena.Arena;
import stfarena.arena.ArenaTeam;

public class ArenaMatch implements Runnable {
	private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	private final Arena arena;
	private final ArenaTeam redTeam, blueTeam;
	private Team rTeam, bTeam;
	private final Set<UUID> redPlayersAlive = new HashSet<UUID>();
	private final Set<UUID> bluePlayersAlive = new HashSet<UUID>();
	private int taskId = -1, timeToTeleport = 15, timeToStart = 10;
	private final MatchManager matchManager;
	
	public ArenaMatch(MatchManager matchManager, Arena arena, ArenaTeam redTeam, ArenaTeam blueTeam) {
		this.matchManager = matchManager;
		this.redTeam = redTeam;
		this.blueTeam = blueTeam;
		this.arena = arena;
		buildScoreboard();
		arena.teleportTeams(redTeam, blueTeam);
	}
	
	public void buildScoreboard() {
		rTeam = scoreboard.registerNewTeam("Red Team");
		bTeam = scoreboard.registerNewTeam("Blue Team");
		rTeam.setAllowFriendlyFire(false);
		bTeam.setAllowFriendlyFire(false);
		rTeam.setPrefix(ChatColor.RED + "Red Team: ");
		bTeam.setPrefix(ChatColor.BLUE + "Blue Team: ");
		assignScoreboard();
	}
	
	private void assignScoreboard() {
		for (UUID id : redTeam.getPlayers()) {
			rTeam.addPlayer(Bukkit.getPlayer(id));
			Bukkit.getPlayer(id).setScoreboard(scoreboard);
		}
		for (UUID id : blueTeam.getPlayers()) {
			bTeam.addPlayer(Bukkit.getPlayer(id));
			Bukkit.getPlayer(id).setScoreboard(scoreboard);
		}
		Objective objective = scoreboard.registerNewObjective("Health", "health");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
			
	public ArenaTeam getRedTeam() {
		return redTeam;
	}
	
	public ArenaTeam getBlueTeam() {
		return blueTeam;
	}
	
	public Arena getArena() {
		return arena;
	}
	
	private void messagePlayers(String message) {
		for (UUID p : redTeam.getPlayers()) {
			if(Bukkit.getPlayer(p) != null){
				if(Bukkit.getPlayer(p).isOnline())
					Bukkit.getPlayer(p).sendMessage(message);
			}
		}
		for (UUID p : blueTeam.getPlayers()) {
			if(Bukkit.getPlayer(p) != null){
				if(Bukkit.getPlayer(p).isOnline())
					Bukkit.getPlayer(p).sendMessage(message);
			}
		}
	}
	
	public MatchStatus recordDeath(UUID id) {
		if (redPlayersAlive.contains(id)){
			redPlayersAlive.remove(id);
			messagePlayers(Bukkit.getPlayer(id).getName() + " on Red team has been slain!");
			if(redPlayersAlive.isEmpty()){
				messagePlayers(ChatColor.GREEN + "All members of Red Team have been slain!");
				messagePlayers(ChatColor.GREEN + "Match over, teleporting in 5 seconds");
				return MatchStatus.BLUE_WON;
			}
		}
		if (bluePlayersAlive.contains(id)){
			bluePlayersAlive.remove(id);
			messagePlayers(Bukkit.getPlayer(id).getName() + " on Blue team has been slain!");
			if(bluePlayersAlive.isEmpty()){
				messagePlayers(ChatColor.GREEN + "All members of Blue Team have been slain!");
				messagePlayers(ChatColor.GREEN + "Match over, teleporting in 5 seconds");
				return MatchStatus.RED_WON;
			}
		}
		return MatchStatus.IN_PROGRESS;
	}
	
	public void removeScoreboard() {
		for (UUID id : redTeam.getPlayers()) {
			rTeam.removePlayer(Bukkit.getPlayer(id));
			Bukkit.getPlayer(id).setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		}
		for (UUID id : blueTeam.getPlayers()) {
			bTeam.removePlayer(Bukkit.getPlayer(id));
			Bukkit.getPlayer(id).setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		}
		scoreboard.getObjective(DisplaySlot.SIDEBAR).unregister();
	}
	
	public void matchStart(){
		Player player = Bukkit.getPlayer(uuid);
		ItemStack[] allowed = player.getInventory().getContents();
		Set<ItemStack> banned = new HashSet<ItemStack>();
		banned.add(new ItemStack(Material.POTION, 1, (short) 8238));
		banned.add(new ItemStack(Material.POTION, 1, (short) 8270));
		banned.add(new ItemStack(Material.POTION, 1, (short) 16430));
		banned.add(new ItemStack(Material.POTION, 1, (short) 16462));
		banned.add(new ItemStack(Material.FLINT_AND_STEEL));
		banned.add(new ItemStack(Material.BUCKET));
		banned.add(new ItemStack(Material.LAVA_BUCKET));
		banned.add(new ItemStack(Material.WATER_BUCKET));
		int potionsAllowed = POTIONLIMIT;
		for (int i = 0; i < allowed.length; i++) {
			if (allowed[i] != null) {
				if (banned.contains(allowed[i])) {
					player.sendMessage("removed banned item " + allowed[i].getType().name());
					player.getInventory().setItem(i, null);	
				}
				else {
					if (allowed[i].getType().equals(Material.POTION)) {
						if (potionsAllowed > 0) {
							potionsAllowed--;
						}
						else {
							player.sendMessage("You have exceeded potion limit!");
							player.getInventory().setItem(i, null);	
						}
					}
					else if (allowed[i].getType().equals(Material.ENDER_PEARL)) {
						player.sendMessage("removed banned item " + allowed[i].getType().name());
						player.getInventory().setItem(i, null);
					}
					else if (allowed[i].getType().equals(Material.GOLDEN_APPLE)) {
						player.sendMessage("removed banned item " + allowed[i].getType().name());
						player.getInventory().setItem(i, null);
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

	@Override
	public void run() {
		if (timeToTeleport > 0) {
			if (timeToTeleport <= 5) {
				messagePlayers("Teleporting to " + arena.name() + " in " + timeToTeleport + " seconds");
			}
			timeToTeleport--;
		}
		else if (timeToTeleport == 0 && timeToStart > 0) {
			if (arena.teleportTeams(redTeam, blueTeam)) {
				matchManager;
			}
		}
		else {
			
		}
		
	}
}
