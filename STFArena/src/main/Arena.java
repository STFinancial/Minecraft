package main;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

//@TODO massive work in progress
public class Arena {
	String name;
	int size;
	Location redSpawn;
	Location blueSpawn;
	int doorBlockID;
	ArenaTeam redTeam;
	ArenaTeam blueTeam;
	
	public Arena(String name, int size, int redX, int redY, int redZ, int blueX, int blueY, int blueZ, int doorBlockID) {
		this.name = name;
		this.size = size;
		redSpawn = new Location(Bukkit.getWorld("Arena"), redX, redY, redZ);
		redSpawn = new Location(Bukkit.getWorld("Arena"), blueX, blueY, blueZ);
		this.doorBlockID = doorBlockID;
	}
	
	public boolean isEmpty(){
		return (redTeam == null && blueTeam == null);
	}

	public void add(ArenaTeam t1, ArenaTeam t2) {
		redTeam = t1;
		blueTeam = t2;
	}

	public String getName() {
		return name;
	}
	
	public int getSize(){
		return size;
	}

	public ArenaTeam getRedTeam(){
		return redTeam;
	}
	public ArenaTeam getBlueTeam(){
		return blueTeam;
	}
	

}
