package stfadventure.classes.necromancer;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_7_R4.util.UnsafeList;

import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntitySkeleton;
import net.minecraft.server.v1_7_R4.PathfinderGoalArrowAttack;
import net.minecraft.server.v1_7_R4.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R4.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R4.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R4.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_7_R4.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R4.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R4.World;

public class NecroSkeleton extends EntitySkeleton implements NecroMinion {
	private EntityHuman owner;
	
	public NecroSkeleton(World world) {
		super(world);
	}

	public void setOwner(EntityHuman necromancer) {
		this.owner = necromancer;
		this.bC();
		try {
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
			this.goalSelector.a(1, new PathfinderGoalFloat(this));
			this.goalSelector.a(2, new PathfinderGoalArrowAttack(this, 1.0D, 20, 60, 15.0F));
			this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1.2D, false));
			this.goalSelector.a(3, new PathfinderGoalFollowNecromancer(this, 1.0D, 5.0F, 2.0F));
			this.goalSelector.a(4, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
			this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
			this.targetSelector.a(1, new PathfinderGoalDefendNecromancer(this));
			this.targetSelector.a(2, new PathfinderGoalAttackTarget(this));
			this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Entity getOwner() {
		return owner;
	}

	@Override
	public String getOwnerUUID() {
		return owner == null? null : owner.getUniqueID().toString();
	}
}
