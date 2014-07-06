package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.Timer;

import org.bukkit.entity.Player;

public class TechData implements ActionListener {
	private int exp = 0;
	private final Timer expTimer;
	private final Timer updateTimer;	
	private final UUID player;
	
	public TechData(Player player) {
		expTimer = new Timer(3600000, this);
		expTimer.setActionCommand("exp");
		expTimer.setRepeats(true);
		updateTimer = new Timer(300000, this);
		updateTimer.setRepeats(true);
		this.player = player.getUniqueId();
	}
	
	public TechData(Player player, int exp) {
		this(player);
		this.exp = exp;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		switch(event.getActionCommand()) {
		case "exp":
			
			break;
		default:
			break;				
		}
	}
}
