package stfadventure.custom;

import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityOwnable;

public interface NecroMinion extends EntityOwnable {
	
	public void setOwner(EntityHuman human);
}
