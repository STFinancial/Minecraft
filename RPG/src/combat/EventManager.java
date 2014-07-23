package combat;

import java.util.HashMap;
import java.util.Map;

import main.GamePlayer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import wizard.FrostMage;
import wizard.Wizzard;

public class EventManager implements Listener {
	private static Map<Player, GamePlayer> players = new HashMap<Player, GamePlayer>();
	
	@EventHandler
	private static void login(PlayerLoginEvent event) {
		players.put(event.getPlayer(), new FrostMage(event.getPlayer(), 1, 0));
	}
	
	@EventHandler
	private static void projectileAttack(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Projectile) {
			Projectile missile = (Projectile) event.getEntity();
			if (missile.getShooter() instanceof Player) {
				switch(missile.getType().name().toLowerCase()) {
				case "arrow":
				case "snowball":
				case "small_fireball":
				case "large_fireball":
				case "wither_skull":
				default:
				}
			}
		}
	}
	
	@EventHandler
	private static void secondaryAttack(PlayerInteractEvent event) {
		if (event.getPlayer().getItemInHand().getType().equals(Material.DIAMOND_HOE)) {
			if (event.getPlayer().isSneaking()) {
				players.get(event.getPlayer()).specialAttack();
			}
			else {
				players.get(event.getPlayer()).secondaryAttack();
			}
		}
	}
	
//	private String getWeapon(Player player) {
//		String[] weapon = player.getItemInHand().getType().name().split("_");
//		if (weapon.length == 1) {
//			return weapon[0].equalsIgnoreCase("bow")? "bow" : "none";
//		}
//		else if (weapon.length == 2) {
//			switch (weapon[1].toLowerCase()) {
//			case "sword":
//			case "spade":
//			case "pickaxe":
//			case "rod":
//			case "hoe":
//				return weapon[1].toLowerCase();
//				
//			}
//		}
//		else {
//			return "none";
//		}
//	}
}
