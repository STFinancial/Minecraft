package stfadventure.necromancer;

import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.IBlockAccess;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.Navigation;
import net.minecraft.server.v1_7_R4.PathfinderGoal;
import net.minecraft.server.v1_7_R4.World;

public class PathfinderGoalFollowNecromancer extends PathfinderGoal {
	private NecroMinion d;
	private EntityCreature m;
	private EntityLiving e;
	World a;
	private double f;
	private Navigation g;
	private int h;
	float b;
	float c;
	private boolean i;

	public PathfinderGoalFollowNecromancer(NecroMinion minion, double d0, float f, float f1) {
		this.d = minion;
		this.m = (EntityCreature) minion;
		this.a = m.world;
		this.f = d0;
		this.g = m.getNavigation();
		this.c = f;
		this.b = f1;
		this.a(3);
	}

	public boolean a() {
		EntityLiving entityliving = (EntityLiving) this.d.getOwner();

		if (entityliving == null) {
			return false;
		} else if (this.m.f(entityliving) < (double) (this.c * this.c)) {
			return false;
		} else {
			this.e = entityliving;
			return true;
		}
	}

	public boolean b() {
		return !this.g.g() && this.m.f(this.e) > (double) (this.b * this.b);
	}

	public void c() {
		this.h = 0;
		this.i = this.m.getNavigation().a();
		this.m.getNavigation().a(false);
	}

	public void d() {
		this.e = null;
		this.g.h();
		this.m.getNavigation().a(this.i);
	}

	public void e() {
		this.m.getControllerLook().a(this.e, 10.0F, (float) this.m.x());
		if (--this.h <= 0) {
			this.h = 10;
			if (!this.g.a((Entity) this.e, this.f)) {
				if (!this.m.bN()) {
					if (this.m.f(this.e) >= 144.0D) {
						int i = MathHelper.floor(this.e.locX) - 2;
						int j = MathHelper.floor(this.e.locZ) - 2;
						int k = MathHelper.floor(this.e.boundingBox.b);

						for (int l = 0; l <= 4; ++l) {
							for (int i1 = 0; i1 <= 4; ++i1) {
								if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && World.a((IBlockAccess) this.a, i + l, k - 1, j + i1) && !this.a.getType(i + l, k, j + i1).r() && !this.a.getType(i + l, k + 1, j + i1).r()) {
									this.m.setPositionRotation((double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), this.m.yaw, this.m.pitch);
									this.g.h();
									return;
								}
							}
						}
					}
				}
			}
		}
	}
}
