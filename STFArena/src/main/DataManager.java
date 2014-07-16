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
	HashMap<String, ArenaTeam> beingCreated;


	public DataManager(Main plugin) {
		this.plugin = plugin;
		arenaTeams = new HashMap<String, ArenaTeam>();
		arenaPlayers = new HashMap<Player, ArenaPlayer>();
		beingCreated = new HashMap<String, ArenaTeam>();
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
		plugin.getCommandManager().cancel(player);
		arenaPlayers.remove(player.getUniqueId());
	}


	public boolean playerIsOnTeam(Player player, String teamName){
		return arenaPlayers.get(player).getTeams().contains(teamName);
	}


	public void disband(String string) {
		// TODO Auto-generated method stub
	}


	public boolean isNameUnique(String string){
		if(arenaTeams.get(string) != null)
			return false;
		if(beingCreated.get(string) != null)
			return false;

		return true;
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
	public void saveTest(Player player){
		arenaPlayers.get(player).saveState(player);
	}public void loadTest(Player player){
		arenaPlayers.get(player).loadState();
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

	public ArenaTeam getTempTeam(String teamName){
		return beingCreated.get(teamName);
	}

	public void createTeam(Player player, String string, int parseInt) {
		arenaPlayers.get(player).setStatus(Status.CREATING);
		arenaPlayers.get(player).setFocus(string);
		beingCreated.put(string, new ArenaTeam(string, parseInt));
		beingCreated.get(string).addPlayer(player);
	}


	public void cancelCreateTeam(Player player){
		ArenaTeam toCancel = beingCreated.get(getFocus(player));
		if(toCancel != null){
			for(UUID p : toCancel.getPlayers()){
				if(plugin.getServer().getPlayer(p) != null){
					if(plugin.getServer().getPlayer(p).isOnline()){
						plugin.getServer().getPlayer(p).sendMessage("Creation of the team " + toCancel.getName() + " has been canceled");
						setFocus(plugin.getServer().getPlayer(p), null);
						setStatus(plugin.getServer().getPlayer(p), Status.FREE);
					}
				}
			}
			beingCreated.remove(toCancel.getName());
		}else{
			plugin.getLogger().info("Seriouse problem canceling a team creation! team not found!");
		}
	}


	public void acceptInvite(Player player) {
		ArenaTeam toCreate = beingCreated.get(getFocus(player));
		if(toCreate != null){
			for(UUID p:toCreate.getPlayers()){
				if(plugin.getServer().getPlayer(p) != null){
					if(plugin.getServer().getPlayer(p).isOnline()){
						plugin.getServer().getPlayer(p).sendMessage("Player " + player.getName() + " has accepted your team invite");
					}
				}
			}
			player.sendMessage("You have accepted the team invitation to " + getFocus(player));
			toCreate.addPlayer(player);
			if(toCreate.isFull()){
				for(UUID p:toCreate.getPlayers()){
					if(plugin.getServer().getPlayer(p) != null){
						if(plugin.getServer().getPlayer(p).isOnline()){
							plugin.getServer().getPlayer(p).sendMessage("Your " +  toCreate.getSize() + "s team " + toCreate.getName() + " has been created!");
							addTeamToPlayer(plugin.getServer().getPlayer(p));
							setFocus(plugin.getServer().getPlayer(p), null);
							setStatus(plugin.getServer().getPlayer(p), Status.FREE);
						}
					}
				}
				arenaTeams.put(toCreate.getName(), toCreate);
				beingCreated.remove(toCreate.getName());
			}else{
				setStatus(player, Status.CREATING);
			}
			
		}else{
			player.sendMessage("Invite accept has failed, the team has disbanded");
			setFocus(player, null);
			setStatus(player, Status.FREE);
		}


	}

	public void cancelInvite(Player player) {
		if(beingCreated.get(getFocus(player)) != null){
				for(UUID p:beingCreated.get(getFocus(player)).getPlayers()){
					if(plugin.getServer().getPlayer(p) != null){
						if(plugin.getServer().getPlayer(p).isOnline()){
							plugin.getServer().getPlayer(p).sendMessage("Player " + player.getName() + " has declined your team invite");
						}
					}
				}
			
		}
		setFocus(player, null);
		setStatus(player, Status.FREE);
		player.sendMessage("You have cancelled your team Invite");
	}


	public ArenaPlayer getArenaPlayer(String name) {
		for(ArenaPlayer p:arenaPlayers.values()){
			if(p.getName().equalsIgnoreCase(name))
				return p;
		}

		return null;
	}
	
	public void addTeamToPlayer(Player player){
		arenaPlayers.get(player).addTeam(getFocus(player));
	}

	//TODO needs serious refinement
	public void sendInvitation(Player sender, ArenaPlayer arenaPlayer, String focus) {
		arenaPlayer.setFocus(focus);
		arenaPlayer.setStatus(Status.INVITED);
		Bukkit.getServer().getPlayer(arenaPlayer.getUUID()).sendMessage("You were just invited to join the " + getTempTeam(focus).getSize() + "s team " + focus ); //Gets to here, throws a null (obviously getting player by UUID)
		Bukkit.getServer().getPlayer(arenaPlayer.getUUID()).sendMessage("by " + sender.getName() + " please type");
		Bukkit.getServer().getPlayer(arenaPlayer.getUUID()).sendMessage("/arena accept or /arena cancel");
		sender.sendMessage("You have successfully invited " + arenaPlayer.getName());
	}




}
