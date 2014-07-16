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
					cancel(player);
					break;
				case "leave":
					leave(player,args);
					break;
				case "me":
					me(player);
					break;
				case "save":
					dataManager.saveTest(player);
					break;
				case "load":
					dataManager.loadTest(player);
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


	void cancel(Player player) {
		switch(dataManager.getStatus(player)){
		case CREATING:
			dataManager.cancelCreateTeam(player);
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
			dataManager.cancelInvite(player);
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
		default:
			player.sendMessage("You are not currently invited or doing anything to Accept in Arena");
		}
	}


	private void invite(Player player, String[] args) {
		if(args.length > 1){
			if(dataManager.getStatus(player) != Status.CREATING){
				player.sendMessage("Invalid invitation");
				player.sendMessage("Create a team before you invite players");
			}else if(dataManager.getArenaPlayer(args[1]) == null){ //@TODO currently failing
				player.sendMessage("Invalid invitation");
				player.sendMessage("Player " + args[1] + " was not found");
			}else if(dataManager.getArenaPlayer(args[1]).getStatus() != Status.FREE){
				player.sendMessage("Invalid invitation");
				player.sendMessage("Player " + args[1] + " is currently " + dataManager.getArenaPlayer(args[1]).getStatus());
			}else if(dataManager.getArenaPlayer(args[1]).getTeams().size() > 5 ){
				player.sendMessage("Invalid invitation");
				player.sendMessage("Player " + args[1] + " is currently on too many teams");
			}else{
				dataManager.sendInvitation(player, dataManager.getArenaPlayer(args[1]), dataManager.getFocus(player));
			}
		}else{
			player.sendMessage("Invalid invitation, please use");
			player.sendMessage("/arena invite [playerName]");
		}
	}


	private void create(Player player, String[] args) {
		if(args.length > 2){
			if(dataManager.getStatus(player) != Status.FREE){
				player.sendMessage("Invalid creation, Busy");
				player.sendMessage(doing(player));
			}else if(dataManager.getTeams(player).size() > 5){
				player.sendMessage("Invalid creation");
				player.sendMessage("You are already on 5 Arena Teams, to disband one type");
				player.sendMessage("/arena leave [teamName]");
			}else if(args[1].matches("^[a-zA-Z0-9]*$") == false){
				player.sendMessage("Invalid creation");
				player.sendMessage("You are not allowed to use special characters");
				player.sendMessage("Only numbers and letters");
			}else if(args[1].length() > 12 || args[1].length() < 3){
				player.sendMessage("Invalid creation");
				player.sendMessage("Team name is invalid size");
				player.sendMessage("Must be between 3 and 12 characters");
			}else if(dataManager.isNameUnique(args[1]) == false){
				player.sendMessage("Invalid creation");
				player.sendMessage("Team name is in use");
				player.sendMessage("Please select a unique name");
			}else if(args[2].matches("-?\\d+") == false){
				player.sendMessage("Invalid creation");
				player.sendMessage("Improper team Size given, please use");
				player.sendMessage("/arena create [teamName] [teamSize]");
			}else if(Integer.parseInt(args[2]) != 2 && Integer.parseInt(args[2]) != 3 && Integer.parseInt(args[2]) != 5 ){
				player.sendMessage("Invalid creation");
				player.sendMessage("a team size of 2, 3 or 5");
				player.sendMessage("Improper team Size given, please use");
			}else{
				dataManager.createTeam(player, args[1], Integer.parseInt(args[2]));
				player.sendMessage("Successful creation");
				player.sendMessage("You now need to invite players to your team");
			}
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


	private void me(Player player) {
		player.sendMessage("Arena Profile for Player: " + player.getName());
		player.sendMessage(doing(player));
		for(String t : dataManager.getTeams(player)){
			player.sendMessage("On Team: "+ dataManager.getTeam(t));
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
		player.sendMessage("/arena me");
		player.sendMessage("/arena invite [playerName]");
		player.sendMessage("/arena create [teamName] [teamSize]");
		player.sendMessage("/arena accept");
		player.sendMessage("/arena cancel");
	}


	private String doing(Player player){
		if(dataManager.getStatus(player) == Status.FREE){
			return "You are currently Free" ;
		}else{
			return "You are currently "+ dataManager.getStatus(player) + " for " + dataManager.getFocus(player);
		}
	}




}
