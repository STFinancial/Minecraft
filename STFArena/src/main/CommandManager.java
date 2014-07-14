package main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {
	private final Main plugin;
	private final DataManager dataManager;

	public CommandManager(Main plugin, DataManager dataManager) {
		this.plugin = plugin;
		this.dataManager = dataManager;
	}
	
	public void quit(){
		plugin.getCommand("arena").setExecutor(null);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player;
		if(sender instanceof Player){
			player = (Player) sender;

			if(args.length == 0){
				help(player, args);
			}else{
				switch(args[0].toLowerCase()){
				case "queue":
					queue(player,args);
					break;
				case "create":
					create(player,args);
					break;	
				case "invite":
					invite(player,args);
					break;	
				case "accept":
					accept(player,args);
					break;	
				case "cancel":
				case "decline":
					cancel(player,args);
					break;
				case "leave":
					leave(player,args);
					break;
				default:
					help(player, args);
					break;
				}
			}

		}

		return true;
	}

	private void leave(Player player, String[] args) {
		if(args.length > 1){
			if(dataManager.playerIsOnTeam(player, args[1])){
				dataManager.disband(args[1]);
			}else{
				player.sendMessage("Failed to leave team: " + args[1]);
				player.sendMessage("Invalid team name given.");
			}
		}else{
			player.sendMessage("Use this command to leave a team you are currently on");
			player.sendMessage("/arena leave [team name]");
		}
	}

	private void cancel(Player player, String[] args) {
		switch(dataManager.getStatus(player)){
		case CREATING:

			break;
		case QUEUED:
			dataManager.dodgeQueue(player);
			break;
		case TRYING_TO_QUEUE:
			dataManager.stopQueue(player);
			break;
		case IN_GAME:
			dataManager.forfeit(player);
			break;
		case INVITED:
			dataManager.declineInvite(player);
			break;
		case FREE:
			player.sendMessage("You are not currently invited or doing anything in Arena");
			break;
		}
	}

	private void accept(Player player, String[] args) {
		switch(dataManager.getStatus(player)){
		case QUEUED:
			dataManager.acceptQueue(player);
			break;
		case INVITED:
			dataManager.acceptInvite(player);
			break;
		case FREE:
			player.sendMessage("You are not currently invited or doing anything in Arena");
			break;
		}
	}

	private void invite(Player player, String[] args) {
		if(args.length > 1){
			//make sure inviter is creating team
			//make sure invited is free
			//make sure invited is on less than 5 teams
		}else{
			player.sendMessage("Invalid invitation, please use");
			player.sendMessage("/arena invite [playerName]");
		}
	}

	private void create(Player player, String[] args) {
		if(args.length > 2){
			//make sure user is on less than 5 teams
			//make sure team name is valid
			//make sure size is 2,3 or 5
		}else{
			player.sendMessage("Invalid creation, please use");
			player.sendMessage("/arena create [teamName] [teamSize]");
		}
		
	}

	private void queue(Player player, String[] args) {
		if(args.length > 1){
			//make sure player is on team
			//make sure all team members are free or trying to queue for this team
		}else{
			player.sendMessage("Invalid queue, please use");
			player.sendMessage("/arena queue [teamName]");
		}
		
	}

	private void help(Player player, String[] args) {
		if(args.length > 1){
			switch(args[1].toLowerCase()){
			case "queue":
				player.sendMessage("Use the ? command to ");
				return;
			case "create":
				player.sendMessage("Use the ? command to ");
				return;	
			case "invite":
				player.sendMessage("Use the ? command to ");
				return;	
			case "accept":
				player.sendMessage("Use the ? command to ");
				return;	
			case "cancel":
			case "decline":
				player.sendMessage("Use the ? command to ");
				return;
			case "leave":
				player.sendMessage("Use the ? command to ");
				return;
			default:
				break;
			}
		}
		player.sendMessage("The Following Sub-Commands are Available");
		player.sendMessage("/arena queue [teamName]");
		player.sendMessage("/arena leave [teamName]");
		player.sendMessage("/arena invite [playerName]");
		player.sendMessage("/arena create [teamName] [teamSize]");
		player.sendMessage("/arena accept");
		player.sendMessage("/arena cancel");
	}
	
	
}