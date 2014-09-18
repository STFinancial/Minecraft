package stfadventure.classes;

public class Skill {
	private long start = System.currentTimeMillis();
	private long cooldown = 0;
	private int cost = 0;
	
	public Skill(long cooldown, int cost) {
		this.cooldown = cooldown;
		this.cost = cost;
	}
	
	public boolean ready() {
		return System.currentTimeMillis() - start <= cooldown;
	}
	
	public int cost() {
		return cost;
	}
	
	public void use() {
		start = System.currentTimeMillis();
	}
	
	public long cooldownRemaining() {
		return (System.currentTimeMillis() - start) / 1000;
	}
}
