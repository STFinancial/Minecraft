package stfadventure.base;

public class Skill {
	private long start = System.currentTimeMillis();
	private long cooldown = 0;
	
	public Skill(long cooldown) {
		this.cooldown = cooldown;
	}
	
	public boolean ready() {
		return System.currentTimeMillis() - start <= cooldown;
	}
	
	public void cooldown() {
		start = System.currentTimeMillis();
	}
}
