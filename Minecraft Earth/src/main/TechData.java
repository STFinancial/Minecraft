package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.swing.Timer;

import org.bukkit.entity.Player;

public class TechData implements ActionListener {
	private int exp = 0;
	private int nextLevelUp = 60;
	private String currentTech = "none";
	private List<String> techs = new LinkedList<String>();
	private final Timer expTimer;
	private final UUID player;
	private final DataManager dataManager;

	public TechData(Player player, DataManager dataManager) {
		expTimer = new Timer(60000, this); //exp every minute
		expTimer.setRepeats(true);
		this.player = player.getUniqueId();
		this.dataManager = dataManager;
	}

	public TechData(Player player, DataManager dataManager, int exp, String currentTech, List<String> techs) { //load current EXP from player
		this(player,dataManager);
		this.exp = exp;
		this.currentTech = currentTech;
		this.techs = techs;
	}
	
	public int getExp() {
		return exp;
	}
	
	public String getCurrentTech() {
		return currentTech;
	}
	
	public List<String> getTechs() {
		return techs;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (dataManager.isPlayerAFK(player)) {
			exp++;
			if (exp % 5 == 0) {
				// @TODO i want to send a player a message saying they are 5/60
				// in exp or w/e
			}
			if (exp > nextLevelUp) {
				exp -= nextLevelUp;
				// Set Tech in Progress to complete
				// Send player complete message
				// Assign next tech to work on by default
				// Send player a message to pick new tech if wanted
				// update permissions?
			}
		}
	}
}
