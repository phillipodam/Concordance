package com.stelligent.concordance.test.util;

/**
 * @author phillipodam
 *
 * Utilities to help with placing executing code in a sleepy state
 */
public class Rest {
	/**
	 * Ensure thread waits for at least as long as the number of requested milliseconds
	 * 
	 * @param millis number of milliseconds to sleep
	 */
	public static void sleep(long millis) {
		long now = System.currentTimeMillis();
		long riseAt = now + millis;
		do {
			try {
				now = System.currentTimeMillis();
				Thread.sleep(riseAt > now ? riseAt - now : 0);
			} catch (Exception e) { }
		} while (System.currentTimeMillis() < riseAt);
	}
}