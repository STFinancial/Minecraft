package combat;

import java.util.HashMap;
import java.util.Map;

import main.GamePlayer;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import util.Util;
import util.Util.Weapon;
import wizard.FrostMage;

public class EventManager implements Listener {
	private static Map<Player, GamePlayer> players = new HashMap<Player, GamePlayer>();

	public static void addGamePlayer(Player player) {
		players.put(player, new FrostMage(player, 1, 0));		
	}

	public static void removeGamePlayer(Player player) {
		players.remove(player);		
	}

	@EventHandler
	private static void playerLogin(PlayerLoginEvent event) {
		addGamePlayer(event.getPlayer());
	}

	@EventHandler
	private static void playerQuit(PlayerQuitEvent event) {
		removeGamePlayer(event.getPlayer());
	}

	@EventHandler
	private static void projectileAttack(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Projectile) {
			Projectile missile = (Projectile) event.getEntity();
			if (missile.getShooter() instanceof Player) {
				switch (missile.getType().name().toLowerCase()) {
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
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player player = event.getPlayer();
			if (Util.getWeapon(player) != Weapon.NONE) {
				if (player.isSneaking()) {
					players.get(player).specialAttack(Util.getWeapon(player));
				}
				else if (event.getClickedBlock() == null || Util.isInteractable(event.getClickedBlock()) == false) {
					players.get(player).secondaryAttack(Util.getWeapon(player));
				}				
			}
		}
	}
	
	@EventHandler
	private static void wizzardPrimaryAttack(Player player) {
		
	}
}
