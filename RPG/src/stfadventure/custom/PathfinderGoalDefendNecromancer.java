package stfadventure.custom;

import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.PathfinderGoalTarget;

public class PathfinderGoalDefendNecromancer extends PathfinderGoalTarget {
	NecroMinion a;
	EntityLiving b;
	private int e;

	public PathfinderGoalDefendNecromancer(NecroMinion minion) {
		super((EntityCreature) minion, false);
		this.a = minion;
		this.a(1);
	}

	public boolean a() {
		EntityLiving entityliving = (EntityLiving) this.a.getOwner();

		if (entityliving == null) {
			return false;
		} else {
			this.b = entityliving.getLastDamager();
			int i = entityliving.aK();
			return i != this.e && this.a(this.b, false) && this.a.getOwner() != this.b;
		}
	}

	public void c() {
		this.c.setGoalTarget(this.b);
		EntityLiving entityliving = (EntityLiving) this.a.getOwner();

		if (entityliving != null) {
			this.e = entityliving.aK();
		}
		super.c();
	}
}
