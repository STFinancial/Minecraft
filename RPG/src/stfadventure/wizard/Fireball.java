package stfadventure.wizard;

import net.minecraft.server.v1_7_R4.EntitySmallFireball;
import net.minecraft.server.v1_7_R4.MovingObjectPosition;
import net.minecraft.server.v1_7_R4.World;

public class Fireball extends EntitySmallFireball {

	public Fireball(World world) {
		super(world);
	}

	@Override
	protected void a(MovingObjectPosition movingobjectposition) {
		if (!this.world.isStatic) {
			this.die();
		}
	}
}
