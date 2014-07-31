package arena;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DataManager {
	private final Main plugin;
	Map<String, ArenaTeam> arenaTeams;
	Map<UUID, ArenaPlayer> arenaPlayers;
	Map<String, ArenaTeam> beingCreated;
	ArrayList<ArenaTeam> arenaLadder;

	public DataManager(Main plugin) {
		this.plugin = plugin;
		arenaPlayers = new HashMap<UUID, ArenaPlayer>();
		beingCreated = new HashMap<String, ArenaTeam>();
		arenaLadder = getLadder();
		load();
	}

	private void load() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			add(player);
		}
	}

	public void quit() {
		for (ArenaTeam team : arenaTeams.values()) {
			team.save();
		}
		for (Player player : Bukkit.getOnlinePlayers()) {
			remove(player);
		}
	}

	public void add(Player player) {
		arenaPlayers.put(player.getUniqueId(), new ArenaPlayer(player));
		for (ArenaTeam t : arenaTeams.values()) {
			if (t.getPlayers().contains(player.getUniqueId()))
				getPlayer(player).addTeam(t.getName());
		}
	}

	public void remove(Player player) {
		plugin.getCommandManager().cancel(player);
		arenaPlayers.remove(player.getUniqueId());
	}

	public ArenaPlayer getPlayer(Player player) {
		return arenaPlayers.get(player.getUniqueId());
	}

	public ArenaPlayer getPlayer(String name) {
		for (ArenaPlayer p : arenaPlayers.values()) {
			if (p.getName().equalsIgnoreCase(name))
				return p;
		}

		return null;
	}

	public ArenaPlayer getPlayer(UUID id) {
		return arenaPlayers.get(id);
	}

	public boolean playerIsOnTeam(Player player, String teamName) {
		return getPlayer(player).getTeams().contains(teamName);
	}

	public void disband(String name) {
		ArenaTeam t = getTeam(name);
		for (UUID p : t.getPlayers()) {
			if (Bukkit.getPlayer(p) != null) {
				if (Bukkit.getPlayer(p).isOnline()) {
					Bukkit.getPlayer(p).sendMessage("Your arena team " + name + " has been disbanded");
					getPlayer(p).removeTeam(name);
					if (getPlayer(p).getFocus() == name) {
						plugin.getCommandManager().cancel(Bukkit.getPlayer(p));
					}
				}
			}
		}

	}

	public boolean isNameUnique(String string) {
		if (arenaTeams.get(string) != null)
			return false;
		if (beingCreated.get(string) != null)
			return false;

		return true;
	}

	public void forfeit(Player player) {
		plugin.getMatchManager().recordDeath(player);
	}

	public void exitQueue(Player player) {
		ArenaTeam t = getTeam(getPlayer(player).getFocus());
		for (UUID p : t.getPlayers()) {
			if (Bukkit.getPlayer(p) != null && Bukkit.getPlayer(p).isOnline()) {
				Bukkit.getPlayer(p).sendMessage("Your team " + getPlayer(player).getFocus() + " has exited the queue");
				getPlayer(p).setStatus(Status.FREE);
				getPlayer(p).setFocus(null);

			}
		}
		plugin.getQueueManager().removeTeamFromQueue(t);
	}

	public void cancelQueue(Player player) {
		ArenaTeam t = getTeam(getPlayer(player).getFocus());
		for (UUID p : t.getPlayers()) {
			if (Bukkit.getPlayer(p) != null && Bukkit.getPlayer(p).isOnline()) {
				Bukkit.getPlayer(p).sendMessage("Your team " + getPlayer(player).getFocus() + " has stopped trying to queue");
				if (getPlayer(p).getStatus() == Status.TRYING_TO_QUEUE) {
					getPlayer(p).setStatus(Status.FREE);
					getPlayer(p).setFocus(null);
				}
			}
		}
	}

	public void queue(Player player, String teamName) {

		getPlayer(player).setStatus(Status.TRYING_TO_QUEUE);
		getPlayer(player).setFocus(teamName);
		ArenaTeam t = getTeam(teamName);
		for (UUID p : t.getPlayers()) {
			Bukkit.getPlayer(p).sendMessage(player.getName() + " has queued for " + t.getSize() + "s on: " + teamName);
		}
		if (teamReadyToQueue(teamName)) {
			for (UUID p : t.getPlayers()) {
				Bukkit.getPlayer(p).sendMessage("Your team " + t.getName() + " has entered queue for " + t.getSize() + "s");
				getPlayer(p).setStatus(Status.QUEUED);
			}
			plugin.getQueueManager().addTeamToQueue(t);
		}

	}

	public void saveTest(Player player) {
		getPlayer(player).saveState();
	}

	public void loadTest(Player player) {
		getPlayer(player).loadState(player);
	}

	public ArenaTeam getTeam(String t) {
		return arenaTeams.get(t);
	}

	public ArenaTeam getTempTeam(String teamName) {
		return beingCreated.get(teamName);
	}

	public void createTeam(Player player, String string, int parseInt) {
		getPlayer(player).setStatus(Status.CREATING);
		getPlayer(player).setFocus(string);
		beingCreated.put(string, new ArenaTeam(string, parseInt));
		beingCreated.get(string).addPlayer(player);
	}

	public void cancelCreateTeam(Player player) {
		ArenaTeam t = beingCreated.get(getPlayer(player).getFocus());
		if (t != null) {
			for (UUID p : t.getPlayers()) {
				if (Bukkit.getPlayer(p) != null) {
					if (Bukkit.getPlayer(p).isOnline()) {
						Bukkit.getPlayer(p).sendMessage("Creation of the team " + t.getName() + " has been canceled");
						getPlayer(p).setFocus(null);
						getPlayer(p).setStatus(Status.FREE);
					}
				}
			}
			beingCreated.remove(t.getName());
		} else {
			plugin.getLogger().info("Seriouse problem canceling a team creation! team not found!");
		}
	}

	public void acceptInvite(Player player) {
		ArenaTeam t = beingCreated.get(getPlayer(player).getFocus());
		if (t != null) {
			for (UUID p : t.getPlayers()) {
				if (Bukkit.getPlayer(p) != null) {
					if (Bukkit.getPlayer(p).isOnline()) {
						Bukkit.getPlayer(p).sendMessage("Player " + player.getName() + " has accepted your team invite");
					}
				}
			}
			player.sendMessage("You have accepted the team invitation to " + getPlayer(player).getFocus());
			t.addPlayer(player);
			if (t.isFull()) {
				for (UUID p : t.getPlayers()) {
					if (Bukkit.getPlayer(p) != null) {
						if (Bukkit.getPlayer(p).isOnline()) {
							Bukkit.getPlayer(p).sendMessage("Your " + t.getSize() + "s team " + t.getName() + " has been created!");
							addTeamToPlayer(Bukkit.getPlayer(p));
							getPlayer(p).setFocus(null);
							getPlayer(p).setStatus(Status.FREE);
						}
					}
				}
				arenaTeams.put(t.getName(), t);
				beingCreated.remove(t.getName());
			} else {
				getPlayer(player).setStatus(Status.CREATING);
			}

		} else {
			player.sendMessage("Invite accept has failed, the team has disbanded");
			getPlayer(player).setFocus(null);
			getPlayer(player).setStatus(Status.FREE);
		}

	}

	public void cancelInvite(Player player) {
		ArenaTeam t = beingCreated.get(getPlayer(player).getFocus());
		if (t != null) {
			for (UUID p : t.getPlayers()) {
				if (Bukkit.getPlayer(p) != null) {
					if (Bukkit.getPlayer(p).isOnline()) {
						Bukkit.getPlayer(p).sendMessage("Player " + player.getName() + " has declined your team invite");
					}
				}
			}

		}
		getPlayer(player).setFocus(null);
		getPlayer(player).setStatus(Status.FREE);
		player.sendMessage("You have cancelled your team Invite");
	}

	public void addTeamToPlayer(Player player) {
		getPlayer(player).addTeam(getPlayer(player).getFocus());
	}

	public void sendInvitation(Player sender, ArenaPlayer arenaPlayer, String focus) {
		arenaPlayer.setFocus(focus);
		arenaPlayer.setStatus(Status.INVITED);
		Bukkit.getPlayer(arenaPlayer.getUUID()).sendMessage("You were just invited to join the " + getTempTeam(focus).getSize() + "s team " + focus);
		Bukkit.getPlayer(arenaPlayer.getUUID()).sendMessage("by " + sender.getName() + " please type");
		Bukkit.getPlayer(arenaPlayer.getUUID()).sendMessage("/arena accept or /arena cancel");
		sender.sendMessage("You have successfully invited " + arenaPlayer.getName());
	}

	public boolean teamAvailable(String name) {

		ArenaTeam t = getTeam(name);
		for (UUID p : t.getPlayers()) {
			if (getPlayer(p) == null) {
				return false;
			} else if (getPlayer(p).getStatus() == Status.FREE) {
			} else if (getPlayer(p).getStatus() == Status.TRYING_TO_QUEUE) {
				if (getPlayer(p).getFocus().equals(name)) {

				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		return true;
	}

	public boolean teamReadyToQueue(String name) {

		ArenaTeam t = getTeam(name);
		for (UUID p : t.getPlayers()) {
			if (getPlayer(p) == null) {
				return false;

			} else if (getPlayer(p).getStatus() == Status.TRYING_TO_QUEUE) {
				if (getPlayer(p).getFocus().equals(name)) {

				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		return true;
	}

	public boolean areAllies(Player p1, Player p2) {
		if(getPlayer(p1).getFocus() == null || getPlayer(p1).getFocus() == null){
			return false;
		}
		return getPlayer(p1).getFocus().equals(getPlayer(p2).getFocus());
	}
	
	public ArrayList<ArenaTeam> getLadder() {
		ArrayList<ArenaTeam> ladder = new ArrayList<ArenaTeam>();
		
		for (Map.Entry<String, ArenaTeam> entry: arenaTeams.entrySet()) {
			ladder.add(entry.getValue());
		}
		boolean swapped = true;
		int length = arenaTeams.size();
		while (!swapped) {
			swapped = false;
			for (int i = 1; i < length; ++i) {
				if (ladder.get(i - 1).getRating() < ladder.get(i).getRating()) {
					Collections.swap(ladder, i, i-1);
					swapped = true;
				}
			}
			length = length - 1;
		}
		
		return ladder;
	}
	 

}
