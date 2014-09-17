package stfadventure.archer;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import stfadventure.classes.Archer;
import stfadventure.main.Main;

public class Hunter extends Archer {

	public Hunter(Main plugin, Player player, YamlConfiguration playerConfig) {
		super(plugin, player, playerConfig);
	}

}
