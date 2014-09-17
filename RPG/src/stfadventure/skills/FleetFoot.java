package stfadventure.skills;

import org.bukkit.event.player.PlayerJoinEvent;

import stfadventure.events.EventManager;

public class FleetFoot extends Skill {
	public FleetFoot() {
		super(0, 0);
		try {
			EventManager.registerSkill(this.getClass().getMethod("run", PlayerJoinEvent.class));
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void run(PlayerJoinEvent event) {
		event.getPlayer().setWalkSpeed(0.3F);
	}
}
