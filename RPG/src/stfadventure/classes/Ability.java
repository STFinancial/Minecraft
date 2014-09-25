package stfadventure.classes;

public abstract class Ability {
	private long start = System.currentTimeMillis();
	private long cooldown = 0;
	private int cost = 0;
	private final String name;
	
	public Ability(String name) {
		this.name = name;
	}
	
	public Ability(String name, long cooldown, int cost) {
		this.name = name;
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
