package stfadventure.archer;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import stfadventure.main.Main;

public class Ranger extends Archer{

	public Ranger(Main plugin, Player player, YamlConfiguration playerConfig) {
		super(plugin, player, playerConfig);
	}

}
