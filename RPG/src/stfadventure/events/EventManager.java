package stfadventure.events;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;

public class EventManager implements Listener {

	private static List<Method> skills = new ArrayList<Method>();

	@EventHandler
	private static void fireMethods(PlayerEvent event) {
		for (Method skill : skills) {
			if (skill.getParameterTypes()[0].equals(event.getClass())) {
				try {
					skill.invoke(null, event);
					Bukkit.getPluginManager().registerEvent(arg0, arg1, arg2, arg3, arg4)
				} catch (Exception e) {

				}
			}
		}
	}
	
	public static void registerSkill(Method method) {
		skills.add(method);
	}
}
