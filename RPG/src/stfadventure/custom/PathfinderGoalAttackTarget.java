package stfadventure.custom;

import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.PathfinderGoalTarget;

public class PathfinderGoalAttackTarget extends PathfinderGoalTarget {
	NecroMinion a;
	EntityLiving b;
	private int e;

	public PathfinderGoalAttackTarget(NecroMinion minion) {
		super((EntityCreature) minion, false);
		this.a = minion;
		this.a(1);
	}

	public boolean a() {
		EntityLiving entityliving = (EntityLiving) this.a.getOwner();

		if (entityliving == null) {
			return false;
		} else {
			this.b = entityliving.aL();
			int i = entityliving.aM();
			return i != this.e && this.a(this.b, false) && this.a.getOwner() != this.b;
		}
	}

	public void c() {
		this.c.setGoalTarget(this.b);
		EntityLiving entityliving = (EntityLiving) this.a.getOwner();

		if (entityliving != null) {
			this.e = entityliving.aM();
		}

		super.c();
	}
}
