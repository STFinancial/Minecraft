package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.Timer;

import org.bukkit.entity.Player;

public class TechData implements ActionListener {
	private int exp = 0;
	private int nextLevelUp = 60;
	private final Timer expTimer;
	private final UUID player;
	private final DataManager dataManager;

	public TechData(Player player, DataManager dataManager) {
		expTimer = new Timer(60000, this); //exp every minute
		expTimer.setActionCommand("exp");
		expTimer.setRepeats(true);
		this.player = player.getUniqueId();
		this.dataManager = dataManager;
	}

	public TechData(Player player, DataManager dataManager, int exp) { //load current EXP from player
		this(player,dataManager);
		this.exp = exp;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		switch(event.getActionCommand()) {
		case "exp":
			if(dataManager.isPlayerAFK(player)){
				exp++;
				if(exp%5 == 0){
					//@TODO i want to send a player a message saying they are 5/60 in exp or w/e
				}
				if(exp > nextLevelUp){
					exp -= nextLevelUp;
					//Set Tech in Progress to complete
					//Send player complete message
					//Assign next tech to work on by default
					//Send player a message to pick new tech if wanted
					//update permissions?
				}
			}
			break;
		default:
			break;				
		}
	}
}
