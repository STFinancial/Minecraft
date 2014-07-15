package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DataManager {
	private final Main plugin;
	HashMap<String, ArenaTeam> arenaTeams;
	HashMap<Player, ArenaPlayer> arenaPlayers;
	ArrayList<ArenaTeam> beingCreated;

	public DataManager(Main plugin) {
		this.plugin = plugin;
		arenaTeams = new HashMap<String, ArenaTeam>();
		arenaPlayers = new HashMap<Player, ArenaPlayer>();
		beingCreated = new ArrayList<ArenaTeam>();
		load();
	}

	private void load() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			add(player);
		}
	}

	public void quit() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			remove(player);
		}
	}

	public void add(Player player) {
		arenaPlayers.put(player, new ArenaPlayer(player));
		for(ArenaTeam t: arenaTeams.values()){
			if(t.getPlayers().contains(player.getUniqueId()))
				arenaPlayers.get(player).addTeam(t.getName());
		}
	}

	public void remove(Player player) {
		arenaPlayers.remove(player.getUniqueId());
	}

	public boolean playerIsOnTeam(Player player, String teamName){
		return arenaPlayers.get(player).getTeams().contains(teamName);
	}

	public void disband(String string) {
		// TODO Auto-generated method stub
	}
	
	public boolean isNameUnique(String string){
		for(ArenaTeam t:arenaTeams.values()){
			if(string == t.getName())
				return false;
		}
		for(ArenaTeam t: beingCreated){
			if(string == t.getName())
				return false;
		}
		
		return true;
	}

	public void declineInvite(Player player) {
		// TODO Auto-generated method stub

	}

	public void forfeit(Player player) {
		// TODO Auto-generated method stub

	}

	public void dodgeQueue(Player player) {
		// TODO Auto-generated method stub

	}

	public void stopQueue(Player player) {
		// TODO Auto-generated method stub

	}

	public void acceptQueue(Player player) {
		// TODO Auto-generated method stub

	}

	public Status getStatus(Player player){
		return arenaPlayers.get(player).getStatus();
	}
	private void setStatus(Player player, Status status) {
		arenaPlayers.get(player).setStatus(status);
	}
	public String getFocus(Player player) {
		return arenaPlayers.get(player).getFocus();
	}
	public void setFocus(Player player, String teamName) {
		arenaPlayers.get(player).setFocus(teamName);
	}

	public ArrayList<String> getTeams(Player player) {
		return arenaPlayers.get(player).getTeams();
	}

	public ArenaTeam getTeam(String t) {
		return arenaTeams.get(t);
	}

	public void createTeam(Player player, String string, int parseInt) {
		arenaPlayers.get(player).setStatus(Status.CREATING);
		arenaPlayers.get(player).setFocus(string);
		beingCreated.add(new ArenaTeam(string, parseInt));
		beingCreated.get(beingCreated.size() - 1).addPlayer(player);
	}
	
	public void cancelCreateTeam(Player player){
		int toDelete = -1;
		for(int i = 0; i < beingCreated.size(); i++){
			if(beingCreated.get(i).getName() == getFocus(player)){
				toDelete = i;
				for(UUID p:beingCreated.get(i).getPlayers()){
					if(plugin.getServer().getPlayer(p) != null){
						if(plugin.getServer().getPlayer(p).isOnline()){
							setFocus(plugin.getServer().getPlayer(p), null);
							setStatus(plugin.getServer().getPlayer(p), Status.FREE);
							player.sendMessage("Creation of the team " + beingCreated.get(toDelete).getName() + " has been canceled");
							//@TODO this removes those players who accepted invites, need to compensate/ fix cancels for invited/accepted but not formed
						}
					}
				}
			}
		}
		if(toDelete != -1){
			beingCreated.remove(toDelete);
		}else{
			plugin.getLogger().info("Seriouse problem canceling a team creation! team not found!");
		}
	}

	public void acceptInvite(Player player) {
		// TODO Auto-generated method stub

	}

	public ArenaPlayer getArenaPlayer(String name) {
		for(ArenaPlayer p:arenaPlayers.values()){
			if(p.getName() == name)
				return p;
		}
		
		return null;
	}

	@SuppressWarnings("deprecation")
	public void sendInvitation(Player sender, ArenaPlayer arenaPlayer, String focus) {
		arenaPlayer.setFocus(focus);
		arenaPlayer.setStatus(Status.INVITED);
		Bukkit.getServer().getPlayer(arenaPlayer.getName()).sendMessage("You were just invited to join the team " + focus );
		Bukkit.getServer().getPlayer(arenaPlayer.getName()).sendMessage("by " + sender.getName() + " please type");
		Bukkit.getServer().getPlayer(arenaPlayer.getName()).sendMessage("/arena accept or /arena cancel");
	}


}
