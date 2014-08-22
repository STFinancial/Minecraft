package stfadventure.util;

public class TimeUtil {
	
	public static long convertSecondsToTicks(long seconds) {
		return seconds * 20;
	}
	
	public static long convertMinutesToTicks(long minutes) {
		return minutes * 60 * 20;
	}
	
	public static long convertSecondsToMilli(long seconds) {
		return seconds * 1000;
	}
}
