package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AFKWatch implements ActionListener  {
	Location lastLocation;
	int durationAFK = 0;
	int tickTimer = 10000; //ten seconds
	boolean AFK = false;
	Timer updateTimer;
	Player player;

	public AFKWatch(Player player) {
		lastLocation = player.getLocation();
		updateTimer = new Timer(tickTimer, this);
		updateTimer.setRepeats(true);
		updateTimer.start();
		this.player = player;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (lastLocation.equals(player.getLocation()) == false) {
			durationAFK = 0;
		} else {
			durationAFK++;
			if (durationAFK > 5 && durationAFK % 5 == 0){
				AFK = true;
				player.sendMessage("You are now AFK");
			}
			if (durationAFK > 30)
				player.kickPlayer("Removed for being AFK too long");
		}
		lastLocation = player.getLocation();
	}
}
