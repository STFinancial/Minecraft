package main;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;

public class Main extends JavaPlugin {
	EventManager eventManager;
	DataManager dataManager;
	public static final int ENERGY_PER_SWING = 40;
	public static final int ENERGY_PER_TICK = 1;
	public static final int TICKS_PER_UPDATE = 5;
	public static final Set<Material> SWORDS = new HashSet<Material>(
			Arrays.asList(new Material[] { Material.DIAMOND_SWORD,
					Material.IRON_SWORD, Material.GOLD_SWORD,
					Material.STONE_SWORD, Material.WOOD_SWORD }));
	@Override
	public void onEnable() {
		ProtocolManager pManager = ProtocolLibrary.getProtocolManager();		
		eventManager = new EventManager(this);
		dataManager = new DataManager(this);
		this.getServer().getPluginManager().registerEvents(eventManager, this);
//		pManager.addPacketListener(new PacketAdapter(this, PacketType.Play.Server.ENTITY_METADATA) {
//			@Override
//			public void onPacketSending(PacketEvent event) {
//				Entity entity = event.getPacket().getEntityModifier(event).read(0);
//                
//                // Disable "personal" potion effect particles.
//                if (event.getPlayer().equals(entity)) {
//                    modifyWatchable(event, 7, (int) 0);
//                }
//				
//			}
//			
//		});
	}
	
	private void modifyWatchable(PacketEvent event, int index, Object value) {
        // Determine if we need to modify this packet
        if (hasIndex(getWatchable(event), index)) {
            // We do - clone it first, as it might have been broadcasted
            event.setPacket(event.getPacket().deepClone());
            
            for (WrappedWatchableObject object : getWatchable(event)) {
                if (object.getIndex() == index) {
                    object.setValue(value);
                }
            }
        }
    }
	
	private boolean hasIndex(List<WrappedWatchableObject> list, int index) {
        for (WrappedWatchableObject object : list) {
            if (object.getIndex() == index) {
                return true;
            }
        }
        return false;
    }
	
	private List<WrappedWatchableObject> getWatchable(PacketEvent event) {
        return event.getPacket().getWatchableCollectionModifier().read(0);
    }
	
	@Override
	public void onDisable() {
		eventManager.quit();
	}

	public DataManager getDataManager() {
		return dataManager;
	}
	
}
