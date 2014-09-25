package stfadventure.main;

public class TimeUtil {
	public enum TimeUnit {
		MILLI, SEC, MIN, TICK;
	}
	
	public static long convertTime(long time, TimeUnit in, TimeUnit out) {
		switch (in) {
		case MILLI:
			switch (out) {
			case MIN:
				return time / 60000;
			case SEC:
				return time / 1000;
			case TICK:
				return time / 50;
			default:
				break;
			}
		case SEC:
			switch (out) {
			case MIN:
				return time / 60;
			case MILLI:
				return time * 1000;
			case TICK:
				return time * 20;
			default:
				break;
			}
		case MIN:
			switch (out) {
			case SEC:
				return time * 60;
			case MILLI:
				return time * 60000;
			case TICK:
				return time * 1200;
			default:
				break;
			}
		case TICK:
			switch (out) {
			case MIN:
				return time / 1200;
			case MILLI:
				return time * 50;
			case SEC:
				return time / 20;
			default:
				break;
			}
		default:
			return time;
		}
	}
}
